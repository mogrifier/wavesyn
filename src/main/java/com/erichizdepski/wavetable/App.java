package com.erichizdepski.wavetable;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static AudioFormat MONO_WAV =
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    44100, 16, 1, 2, 44100, false);

    public static void main( String[] args )
    {
        App app = new App();

        TableLoader loader = new TableLoader();
        List<String> files = null;
        List<ByteBuffer> tables = null;
        WavePlayer player = new WavePlayer();
        ByteBuffer bigBuffer = ByteBuffer.allocate(5000 * 32768);
        int index = 0;
        int repeat = 0;
        int tablePick = 0;
        try {
            files = loader.getTableNames("/com/erichizdepski/wavetable/");
            tables = loader.loadTables(files);

            for (int k = 0; k < 40; k++) {
                //now lets play an audio file


                for (int i = 7; i < 60; i++) {
                    //build big buffer- make it a patch
                    //index = 35 + (int)Math.floor(Math.random() * 3);
                    tablePick = 50 +  (int) Math.floor(Math.random() * 3);
                    repeat = 5 + (int) Math.floor(Math.random() * 10);
                    //super fast
                    repeat = 10;
                    for (int j = 0; j < repeat; j++) {
                        bigBuffer.put(app.getWaveForm(tables.get(69), i));
                    }
                }


                //now run through backwards

                for (int i = 58; i > 7; i--) {
                    //build big buffer- make it a patch
                    //index = 35 + (int)Math.floor(Math.random() * 3);
                    tablePick = 50 +  (int) Math.floor(Math.random() * 3);
                    repeat = 5 + (int) Math.floor(Math.random() * 10);
                    //super fast
                    repeat = 10;
                    for (int j = 0; j < repeat; j++) {
                        bigBuffer.put(app.getWaveForm(tables.get(69), i));
                    }
                }

            }

            //simple to move up and down an octave as shown below. It is the in between steps that are tricky.


            //player.play(bigBuffer.array());

            //now raise an octave
            byte[] data = bigBuffer.array();
            byte[] upOctave = new byte[data.length/2];

            int j = 0;
            for (int i = 0; i < data.length; i += 2)
            {
                upOctave[j++] = data[i];
            }

           //player.play(upOctave);

            //lower an octave??
            byte[] downOctave = new byte[data.length * 2];

            j = 0;
            for (int i = 0; i < data.length; i++)
            {
                downOctave[j++] = data[i];
                downOctave[j++] = data[i];
               //downOctave[j++] = data[i];
                //downOctave[j++] = data[i];
            }

            player.play(downOctave);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public byte[] getWaveForm( ByteBuffer table, int index)
    {
        byte[] data = table.array();
        byte[] wave = new byte[512];
        System.arraycopy(data,index * 512, wave, 0, 512);

        return wave;
    }
}
