package com.robomorphine.log.filter.bool;

import junit.framework.TestCase;

import com.robomorphine.log.Log;

public class TrueFilterTest extends TestCase {
    public void testTrue() {
        TrueFilter filter = new TrueFilter();
        assertTrue(filter.apply(Log.DEBUG, "tag", "message"));
        assertTrue(filter.apply(Log.FATAL, "anothertag", "anothermessage"));
    }
}
