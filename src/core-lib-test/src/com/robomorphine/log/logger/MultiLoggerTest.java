package com.robomorphine.log.logger;

import android.util.Log;
import junit.framework.TestCase;

public class MultiLoggerTest extends TestCase {
       
    
    
    public void testAdd() {
        TestLogger alogger = new TestLogger();
        TestLogger blogger = new TestLogger();
        TestLogger clogger = new TestLogger();
        
        MultiLogger mlogger = new MultiLogger();
        mlogger.add(alogger, blogger, clogger);
        assertEquals(3, mlogger.size());
        assertSame(alogger, mlogger.get(0));
        assertSame(blogger, mlogger.get(1));
        assertSame(clogger, mlogger.get(2));        
    }
    
    public void testPrint() {
        TestLogger alogger = new TestLogger();
        TestLogger blogger = new TestLogger();
        TestLogger clogger = new TestLogger();
        
        MultiLogger mlogger = new MultiLogger();
        mlogger.add(alogger, blogger, clogger);
        
        mlogger.print(Log.ASSERT, "tag", "msg");
        assertEquals(1, alogger.getCount());
        assertEquals(1, blogger.getCount());
        assertEquals(1, clogger.getCount());
    }
    
    
}
