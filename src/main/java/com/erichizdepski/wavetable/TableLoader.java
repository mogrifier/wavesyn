package com.erichizdepski.wavetable;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TableLoader {

    public static final int HEADER = 44;
    public static final int TABLE_SIZE = 32768;

    public List<String> getTableNames(String root) throws IOException
    {
        //to read jar files as well as in IDE, use this.
        Class clazz = TableLoader.class;
        URL resource = clazz.getResource(root);
        List<String> files = null;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        //from jar this prints out:
        //jar:file:/E:/java_work/wavesyn/target/wavesyn-1.0-SNAPSHOT-jar-with-dependencies.jar!/com/erichizdepski/wavetable/
        if (resource.toString().contains("jar!"))
        {
            //running from within a jar- do some magic
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            files = new ArrayList<String>(100);
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.endsWith(".WAV")) { //filter according to the path

                    //must remove package from heading I think. this contains com/erichizdepski/wavetable/AAHWOHYE.WAV
                    files.add(name.substring(name.lastIndexOf('/') + 1));
                }
            }
            jar.close();
        }
        else {
            //not running from in a jar
            files = IOUtils.readLines(clazz.getResourceAsStream(root), Charset.defaultCharset());
        }
        return files;
    }

    public List<ByteBuffer> loadTables(List<String> files)
    {
        /*iterate through the wavetable files and create a shortbuffer for each one.
        The wav files are standard. Start with a 44 byte header. data size is 32768 (32kb)
         */
        List<ByteBuffer> tables = new ArrayList(files.size());
        AtomicInteger index = new AtomicInteger(-1);

        files.forEach((temp) -> {
            System.out.println(index.incrementAndGet() + " " + temp);
            //load each file as inputstream and read
            InputStream is = TableLoader.class.getResourceAsStream(temp);
            byte data[] = new byte[TABLE_SIZE];
            try
            {
                //skip the header of bytes
                is.skip(HEADER);
                is.read(data, 0, TABLE_SIZE);
                //create bytebuffer
                tables.add(ByteBuffer.wrap(data));
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        });

        return tables;
    }
}
