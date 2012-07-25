package com.robomorphine.log.filter.basic;

import android.util.Log;
import junit.framework.TestCase;

public class TagFilterTest extends TestCase {
    
    public void testTag() {
        TagFilter filter = new TagFilter("*a*");
        
        assertFalse(filter.apply(Log.DEBUG, "xyz", "message"));
        assertFalse(filter.apply(Log.ERROR, "xyz", "anothermessage"));
        
        assertTrue(filter.apply(Log.DEBUG, "xaz", "message"));
        assertTrue(filter.apply(Log.ERROR, "xaz", "anothermessage"));
        
        filter = new TagFilter("abc*");
        
        assertFalse(filter.apply(Log.DEBUG, "xyz", "message"));
        assertFalse(filter.apply(Log.ERROR, "xyz", "anothermessage"));
        
        assertTrue(filter.apply(Log.DEBUG, "abcxyz", "message"));
        assertTrue(filter.apply(Log.ERROR, "abcxyz", "anothermessage"));
    }
}
