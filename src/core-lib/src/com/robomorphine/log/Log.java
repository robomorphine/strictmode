package com.robomorphine.log;

import com.google.common.annotations.VisibleForTesting;
import com.robomorphine.log.logger.AndroidLogger;
import com.robomorphine.log.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Log { 
    
    /* supported log levels */
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    public static final int FATAL = 5;
    public static final int ASSERT = FATAL;
    public static final int LEVEL_COUNT = FATAL + 1;
    
    public static final int [] LEVELS = new int [] {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    };
    
    public static final String [] LEVEL_NAMES = new String[] {
        "VERBOSE", 
        "DEBUG", 
        "INFO",
        "WARN",
        "ERROR",
        "FATAL"
    };
    
    @VisibleForTesting //NOPMD
    static final String UNKNOWN_LEVEL = "UNKNOWN";
    
    public static String toString(int level) {
        if(level < 0 || level >= LEVEL_NAMES.length) {
            return UNKNOWN_LEVEL;
        }
        return LEVEL_NAMES[level];
    }
    
    public static boolean isValidLevel(int level) {
        for(int curLevel : LEVELS) {
            if(level == curLevel) {
                return true;
            }
        }
        return false;
    }
    
    private Log() {
    }
    
    private static Logger sLogger = new AndroidLogger();//NOPMD
    
    public static void setLogger(Logger logger) {
        sLogger = logger;
    }
    
    public static Logger getLogger() {
        return sLogger;
    }
    
    /**************************
     * 		Common
     **************************/
    public static void print(int level, String tag, String msg) {
        if(sLogger != null) {
            sLogger.print(level, tag, msg);
        }
    }
         
    @VisibleForTesting  //NOPMD
    static Throwable extractThrowable(Object[] formatArgs) {
        if (formatArgs == null || formatArgs.length == 0) {
            return null;
        }

        Object lastArg = formatArgs[formatArgs.length - 1];
        if (lastArg instanceof Throwable) {
            return (Throwable) lastArg;
        }
        return null;
    }
    
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
    
    /**************************
     * 		Verbose
     **************************/
    public static void v(String tag, String msg) {
        print(VERBOSE, tag, msg);
    }

    public static void v(String tag, Throwable ex) {
        v(tag, ex.getMessage(), ex);
    }

    public static void v(String tag, String msg, Throwable ex) {
        v(tag, msg + "\n" + getStackTraceString(ex));
    }
    
    public static void v(String tag, String format, Object... formatArgs) {
        v(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
    
    /**************************
     * 		 Debug
     **************************/
    
    public static void d(String tag, String msg) {
        print(DEBUG, tag, msg);
    }

    public static void d(String tag, Throwable ex) {
        d(tag, ex.getMessage(), ex);
    }
    
    public static void d(String tag, String msg, Throwable ex) {
        d(tag, msg + "\n" + getStackTraceString(ex));
    }

    public static void d(String tag, String format, Object... formatArgs) {
        d(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
    
    /**************************
     * 		   Info
     **************************/
    
    public static void i(String tag, String msg) {
        print(INFO, tag, msg);
    }

    public static void i(String tag, Throwable ex) {
        i(tag, ex.getMessage(), ex);
    }

    public static void i(String tag, String msg, Throwable ex) {
        i(tag, msg + "\n" + getStackTraceString(ex));
    }

    public static void i(String tag, String format, Object... formatArgs) {
        i(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
    
    /**************************
     * 		 Warning
     **************************/
    
    public static void w(String tag, String msg) {
        print(WARN, tag, msg);
    }

    public static void w(String tag, Throwable ex) {
        w(tag, ex.getMessage(), ex);
    }

    public static void w(String tag, String msg, Throwable ex) {
        w(tag, msg + "\n" + getStackTraceString(ex));
    }

    public static void w(String tag, String format, Object... formatArgs) {
        w(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
    
    /**************************
     * 		 Error
     **************************/
    
    public static void e(String tag, String msg) {
        print(ERROR, tag, msg);
    }

    public static void e(String tag, Throwable ex) {
        e(tag, ex.getMessage(), ex);
    }

    public static void e(String tag, String msg, Throwable ex) {
        e(tag, msg + "\n" + getStackTraceString(ex));
    }

    public static void e(String tag, String format, Object... formatArgs) {
        e(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
    
    /**************************
     * 		 Fatal
     **************************/
    
    public static void wtf(String tag, String msg) {
        /* let all filters handle the fatal error */
        print(FATAL, tag, msg);        
    }
    
    public static void wtf(String tag, Throwable ex) {
        wtf(tag, ex.getMessage(), ex);
    }
    
    public static void wtf(String tag, String msg, Throwable ex) {
        wtf(tag, msg + "\n" + getStackTraceString(ex));
    }

    public static void wtf(String tag, String format, Object... formatArgs) {
        wtf(tag, String.format(format, formatArgs), extractThrowable(formatArgs));
    }
}
