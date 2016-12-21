package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.Deque;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc862.valkyrie.Constants;

public class LogWriter implements Loop {
    private BufferedWriter writer;
    private ArrayBlockingQueue<String> buffer;
    private Vector<String> drain;

    public LogWriter(String file, int buffer_depth) {
        buffer = new ArrayBlockingQueue<String>(buffer_depth);
        drain = new Vector<String>(buffer_depth);
        
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public LogWriter(String fname) {
        this(fname, Constants.logDepth);
    }
    
    public void onLoop() {
        try {
            buffer.drainTo(drain);
            for (String msg : drain) {
                writer.write(msg);
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error writing buffer");
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logString(String s) {
        buffer.offer(s);
    }
    
    @Override
    public void onStart() { }

    @Override
    public void onStop() {
        flush();
    }
}