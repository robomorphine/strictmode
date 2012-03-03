package com.robomorphine.log.logger;

import junit.framework.TestCase;
import android.util.Log;

import com.robomorphine.log.filter.bool.FalseFilter;
import com.robomorphine.log.filter.bool.TrueFilter;

public class FilterLoggerTest extends TestCase {
    
    /* if no filter is specified, all logs are printed */
    public void testNullFilter() {
        
        TestLogger testLogger = new TestLogger();
        FilterLogger filterLogger = new FilterLogger(testLogger);
                
        filterLogger.print(Log.ASSERT, "tag", "msg");
        
        assertEquals(1, testLogger.getCount());
    }
    
    /* if filter is specified, its applied. */
    public void testFalseFilter() {
        
        TestLogger testLogger = new TestLogger();
        FilterLogger filterLogger = new FilterLogger(testLogger, new FalseFilter());
                
        filterLogger.print(Log.ASSERT, "tag", "msg");
        
        assertEquals(0, testLogger.getCount());
    }
    
    /* if filter is specified, its applied. */
    public void testTrueFilter() {
        
        TestLogger testLogger = new TestLogger();
        FilterLogger filterLogger = new FilterLogger(testLogger, new TrueFilter());
                
        filterLogger.print(Log.ASSERT, "tag", "msg");
        
        assertEquals(1, testLogger.getCount());
    }
    
    public void testGetSetLogger() {
        TestLogger testLogger = new TestLogger();
        TestLogger testLogger2 = new TestLogger();
        
        FilterLogger filterLogger = new FilterLogger(testLogger);
        assertSame(testLogger, filterLogger.getLogger());
        
        filterLogger.setLogger(testLogger2);
        assertSame(testLogger2, filterLogger.getLogger());
    }
    
    public void testGetSetFilter() {
        TestLogger testLogger = new TestLogger();
        TrueFilter afilter = new TrueFilter();
        FalseFilter bfilter = new FalseFilter();
        
        FilterLogger filterLogger = new FilterLogger(testLogger, afilter);
        assertSame(afilter, filterLogger.getFilter());
        
        filterLogger.setFilter(bfilter);
        assertSame(bfilter, filterLogger.getFilter());
        
        filterLogger.setFilter(null);
        assertNull(filterLogger.getFilter());
    }
}
