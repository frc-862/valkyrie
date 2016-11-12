package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;

public class Logger {
    public static final double FREQUENCY = 0.5;
    public static final int BUFFER_DEPTH = 500;
    
    public static final int DEBUG = 10;
    public static final int WARN = 20;
    public static final int ERROR = 30;

    private static int level = DEBUG;
    private static Logger logger;
    private Thread logging_thread;

    private File file = null;
    private BufferedWriter writer;
    private ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<String>(BUFFER_DEPTH);
    private double next_write = Timer.getFPGATimestamp() + FREQUENCY;

    private static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    void write_buffered_message() {
        try {
            writer.write(buffer.take());
            writer.newLine();

            if (Timer.getFPGATimestamp() > next_write) {
                writer.flush();
                next_write = Timer.getFPGATimestamp() + FREQUENCY;
            }
        } catch (Exception e) {
            System.err.println("Error writing buffer");
            e.printStackTrace();
        }
    }

    private Logger() {
        try {
            file = logFileName();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

            logging_thread = new Thread(() -> {
                while (true) {
                    write_buffered_message();
                }
            });
            logging_thread.setPriority(Thread.MIN_PRIORITY);
            logging_thread.setName("LoggingThread");
            logging_thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected File logFileName() {
        File base = null;

        // find the mount point
        char mount = 'u';
        while (base == null && mount <= 'z') {
            File f = new File("/" + mount);
            if (f.isDirectory()) {
                base = f;
            }
            ++mount;
        }

        if (base == null) {
            base = new File("/home/lvuser");
        }

        base = new File(base, "log");
        base.mkdirs();

        int counter = 0;
        File result = new File(base, String.format("robot-%05d.log", counter));
        while (result.exists()) {
            result = new File(base, String.format("robot-%05d.log", ++counter));
        }

        return result;
    }

    private boolean logString(String s) {
        return buffer.offer(s);
    }

    public static boolean log(String s) {
        return getLogger().logString(s);
    }
    
    public static void setLevel(int l) {
        level = l;
    }
    
    public static void debug(String s) {
        if (level <= DEBUG) getLogger().logString(s);
    }
    
    public static void warn(String s) {
        if (level <= WARN) getLogger().logString(s);        
    }
    
    public static void error(String s) {
        if (level <= ERROR) getLogger().logString(s);
    }
}