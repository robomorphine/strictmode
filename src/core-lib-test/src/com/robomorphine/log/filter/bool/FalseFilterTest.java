package com.robomorphine.log.filter.bool;

import junit.framework.TestCase;

import com.robomorphine.log.Log;

public class FalseFilterTest extends TestCase {
    public void testFalse() {
        FalseFilter filter = new FalseFilter();
        assertFalse(filter.apply(Log.DEBUG, "tag", "message"));
        assertFalse(filter.apply(Log.FATAL, "anothertag", "anothermessage"));
    }
}
