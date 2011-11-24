package com.robomorphine.log.filter.bool;

import junit.framework.TestCase;

import com.robomorphine.log.Log;

public class NotFilterTest extends TestCase {
    public void testNot() {
        FalseFilter falseFilter = new FalseFilter();
        TrueFilter trueFilter = new TrueFilter();
        
        NotFilter notFilter = new NotFilter(falseFilter);
        assertTrue(notFilter.apply(Log.DEBUG, "tag", "message"));
        assertTrue(notFilter.apply(Log.FATAL, "anothertag", "anothermessage"));
        
        notFilter = new NotFilter(trueFilter);
        assertFalse(notFilter.apply(Log.DEBUG, "tag", "message"));
        assertFalse(notFilter.apply(Log.FATAL, "anothertag", "anothermessage"));
    }
}
