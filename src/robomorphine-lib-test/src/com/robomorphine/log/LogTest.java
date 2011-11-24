package com.robomorphine.log;

import java.io.IOException;

import junit.framework.TestCase;

import com.robomorphine.log.logger.TestLogger;

public class LogTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.setLogger(null);
    }
    
    public void testToString() {
        for(int level : Log.LEVELS) {
            assertEquals(Log.LEVEL_NAMES[level], Log.toString(level));    
        }        
        assertEquals(Log.UNKNOWN_LEVEL, Log.toString(Log.VERBOSE - 1));
        assertEquals(Log.UNKNOWN_LEVEL, Log.toString(Log.ASSERT + 1));
    }  
    
    public void testIsValidLevel() {
        for(int level : Log.LEVELS) {
            assertTrue(Log.isValidLevel(level));    
        }
        
        assertFalse(Log.isValidLevel(Log.VERBOSE - 1));
        assertFalse(Log.isValidLevel(Log.ASSERT + 1));
    }
    
    public void testPrint() {
        TestLogger logger = new TestLogger();
        Log.setLogger(logger);
        Log.print(Log.DEBUG, "tag", "msg");
        assertEquals(1, logger.getCount());
    }
    
    
    private Object[] getArgs(Object...args) {
        return args;
    }    
    
    public void testExtractThrowable() {
        assertNull(Log.extractThrowable(null));
        assertNull(Log.extractThrowable(getArgs()));
        assertNull(Log.extractThrowable(getArgs("test")));
        
        IOException ex = new IOException();
        assertSame(ex, Log.extractThrowable(getArgs(ex)));
        assertSame(ex, Log.extractThrowable(getArgs("test", ex)));
        
        assertNull(Log.extractThrowable(getArgs("test", ex, "test")));
    }
    
    public void testGetStackTraceString() {
        assertEquals("", Log.getStackTraceString(null));
        assertNotNull(Log.getStackTraceString(new IOException()));
    }
    
    /********************************/
    /**     test print shortcuts   **/
    /********************************/
    
    interface LogRedirector {
        public void log(String tag, String msg);
        public void log(String tag, Throwable ex);
        public void log(String tag, String msg, Throwable ex);
        public void log(String tag, String format, Object... formatArgs);
    }
    
    public void checkLogs(int expectedLevel, LogRedirector redirector) {
        TestLogger logger = new TestLogger();
        Log.setLogger(logger);
        
        String tag = "tag";
        String msg = "msg";
        IOException ex = new IOException("exception");
        
        logger.reset();
        
        redirector.log(tag, msg);
        assertEquals(1, logger.getCount());
        assertEquals(expectedLevel, logger.getLastLevel());
        assertEquals(tag, logger.getLastTag());
        assertEquals(msg, logger.getLastMsg());
        
        logger.reset();
        
        redirector.log(tag, ex);
        assertEquals(1, logger.getCount());
        assertEquals(expectedLevel, logger.getLastLevel());
        assertEquals(tag, logger.getLastTag());
        assertTrue(logger.getLastMsg().contains(ex.getMessage()));
        
        logger.reset();
        
        redirector.log(tag, msg, ex);
        assertEquals(1, logger.getCount());
        assertEquals(expectedLevel, logger.getLastLevel());
        assertEquals(tag, logger.getLastTag());
        assertTrue(logger.getLastMsg().contains(msg));
        assertTrue(logger.getLastMsg().contains(ex.getMessage()));
        
        logger.reset();
        
        int arg1 = 10;
        double arg2 = 10.123;
        String formatMsg = "test %d and then %f message";        
        String formattedMsg = String.format(formatMsg, arg1, arg2);
        redirector.log(tag, formatMsg, arg1, arg2, ex);
        
        assertEquals(1, logger.getCount());
        assertEquals(expectedLevel, logger.getLastLevel());
        assertEquals(tag, logger.getLastTag());
        assertTrue(logger.getLastMsg().contains(formattedMsg));
        assertTrue(logger.getLastMsg().contains(ex.getMessage()));
    }
    
    public void testVerbose() {
        checkLogs(Log.VERBOSE, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.v(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.v(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.v(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.v(tag, format, formatArgs);
            }            
        });
    }
    
    public void testDebug() {
        checkLogs(Log.DEBUG, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.d(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.d(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.d(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.d(tag, format, formatArgs);
            }            
        });
    }
    
    public void testInfo() {
        checkLogs(Log.INFO, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.i(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.i(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.i(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.i(tag, format, formatArgs);
            }            
        });
    }
    
    public void testWarning() {
        checkLogs(Log.WARN, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.w(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.w(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.w(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.w(tag, format, formatArgs);
            }            
        });
    }
    
    public void testError() {
        checkLogs(Log.ERROR, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.e(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.e(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.e(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.e(tag, format, formatArgs);
            }            
        });
    }
    
    public void testAssert() {
        checkLogs(Log.ASSERT, new LogRedirector() {            
            @Override
            public void log(String tag, String msg) {
                Log.wtf(tag, msg);                
            }
            
            @Override
            public void log(String tag, Throwable ex) {
                Log.wtf(tag, ex);                
            }
            
            @Override
            public void log(String tag, String msg, Throwable ex) {
                Log.wtf(tag, msg, ex);                
            }
            
            @Override
            public void log(String tag, String format, Object... formatArgs) {
                Log.wtf(tag, format, formatArgs);
            }            
        });
    }
}
