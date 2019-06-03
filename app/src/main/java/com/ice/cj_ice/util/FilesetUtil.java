package com.ice.cj_ice.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2019/5/23.
 */

public class FilesetUtil {

    public void writeFloatToData(float[] verts, String gcodeFile) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(gcodeFile, "rw");
            FileChannel outChannel = aFile.getChannel();
            //one float 4 bytes
            ByteBuffer buf = ByteBuffer.allocate(4 * 5);
            buf.clear();
            buf.asFloatBuffer().put(verts);
            //while(buf.hasRemaining())
            {
                outChannel.write(buf);
            }
            //outChannel.close();
            buf.rewind();
            outChannel.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    public float[] readFloatFromData(String gcodeFile) {
        float[] verts = new float[5];
        try {
            RandomAccessFile rFile = new RandomAccessFile(gcodeFile, "rw");
            FileChannel inChannel = rFile.getChannel();
            ByteBuffer buf_in = ByteBuffer.allocate(5 * 4);
            buf_in.clear();
            inChannel.read(buf_in);
            buf_in.rewind();
            buf_in.asFloatBuffer().get(verts);
            inChannel.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return verts;
    }
}
