package com.robomorphine.log.filter.basic;

import android.util.Log;
import junit.framework.TestCase;

public class MsgFilterTest extends TestCase {
    
    public void testMsg() {
        MsgFilter filter = new MsgFilter("*a*");
        
        assertFalse(filter.apply(Log.DEBUG, "tag", "xyz"));
        assertFalse(filter.apply(Log.ERROR, "anothertag", "xyz"));
        
        assertTrue(filter.apply(Log.DEBUG, "tag", "xaz"));
        assertTrue(filter.apply(Log.ERROR, "anothertag", "xaz"));
        
        filter = new MsgFilter("abc*");
        
        assertFalse(filter.apply(Log.DEBUG, "tag", "xyz"));
        assertFalse(filter.apply(Log.ERROR, "anothertag", "xyz"));
        
        assertTrue(filter.apply(Log.DEBUG, "tag", "abcxyz"));
        assertTrue(filter.apply(Log.ERROR, "anothertag", "abcxyz"));
    }
}
