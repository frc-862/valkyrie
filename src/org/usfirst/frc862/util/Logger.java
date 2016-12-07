package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Logger {
    private static final int FREQUENCY = 500;
    private static final int BUFFER_DEPTH = 500;

    public static final int TRACE = 0;
    public static final int DEBUG = 10;
    public static final int INFO = 20;
    public static final int WARN = 30;
    public static final int ERROR = 40;

    private static final int FLUSH_COUNT = 100;

    private static int level = DEBUG;
    private static Logger logger;

    private BufferedWriter writer;
    private static int counter = 0;
    private ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<String>(BUFFER_DEPTH);

    private static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    private void write_buffered_message() {
        try {
            String msg = buffer.poll(FREQUENCY, TimeUnit.MILLISECONDS);
            if (msg != null) {
                writer.write(msg);
                writer.newLine();
                ++counter;
            } 
            
            if (counter > FLUSH_COUNT || msg == null) {
                writer.flush();
                counter = 0;
            }
        } catch (Exception e) {
            System.err.println("Error writing buffer");
            e.printStackTrace();
        }
    }

    private Logger() {
        try {
            File file = logFileName();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

            Thread logging_thread = new Thread(() -> {
                //noinspection InfiniteLoopStatement
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

    private File logFileName() {
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

    private void logString(String s) {
        buffer.offer(s);
    }
    private void logString(String format, Object... args) {
        logString(String.format(format, args));
    }

    public static void log(String s) {
        getLogger().logString(s);
    }
    
    public static void setLevel(int l) {
        level = l;
    }

    public static void trace(String s) {
        if (level <= TRACE)
            getLogger().logString(s);
    }

    public static void trace(String format, Object... args) {
        if (level <= TRACE)
            getLogger().logString(format, args);
    }

    public static void debug(String s) {
        if (level <= DEBUG)
            getLogger().logString(s);
    }

    public static void debug(String format, Object... args) {
        if (level <= DEBUG)
            getLogger().logString(format, args);
    }

    public static void info(String s) {
        if (level <= INFO)
            getLogger().logString(s);
    }

    public static void info(String format, Object... args) {
        if (level <= INFO)
            getLogger().logString(format, args);
    }

    public static void warn(String s) {
        if (level <= WARN) {
            System.err.println(s);
            getLogger().logString(s);
        }
    }

    public static void warn(String s, Object... args) {
        if (level <= WARN) {
            String msg = String.format(s, args);
            System.err.println(msg);
            getLogger().logString(msg);
        }
    }

    public static void error(String s) {
        System.err.println(s);
        if (level <= ERROR) getLogger().logString(s);
    }

    public static void error(String s, Object... args) {
        String msg = String.format(s, args);
        System.err.println(msg);
        if (level <= ERROR) getLogger().logString(msg);
    }

    public static void flush() {
        try {
            getLogger().writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Error flushing logger stream");
            e.printStackTrace();
        }
    }
}
