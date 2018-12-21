package com.erichizdepski.wavetable;

/*
 * Player.java
 *
 * Created on June 14, 2005, 8:19 PM
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * This class plays 16-bit, mono sample data using Java Sound.
 *
 * @author Erich Izdepski
 */
public class WavePlayer {


    public void play(byte[] buffer) {
        //now create a playback piece
        DataLine.Info info = null;
        SourceDataLine line = null;

        try {
            info = new DataLine.Info(SourceDataLine.class, App.MONO_WAV);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(App.MONO_WAV, buffer.length);
            line.start();
            //write data for a while, then quit
            line.write(buffer, 0, buffer.length);


        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }  finally {
            line.stop();
        }
    }
}