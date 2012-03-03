package com.robomorphine.log.filter.bool;

import com.robomorphine.log.Log;

import junit.framework.TestCase;

public class AndFilterTest extends TestCase {
    public void testEmpty() {
        AndFilter empty = new AndFilter();        
        assertTrue(empty.apply(Log.ASSERT, "tag", "message"));
        assertTrue(empty.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
    
    public void testTrue() {
        AndFilter multi = new AndFilter();
        TrueFilter aFilter = new TrueFilter();
        TrueFilter bFilter = new TrueFilter();
        multi.add(aFilter, bFilter);
        
        assertTrue(multi.apply(Log.ASSERT, "tag", "message"));
        assertTrue(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
    
    public void testFalse() {
        AndFilter multi = new AndFilter();
        TrueFilter aFilter = new TrueFilter();
        FalseFilter bFilter = new FalseFilter();
        
        multi.clear();
        multi.add(aFilter, bFilter);        
        assertFalse(multi.apply(Log.ASSERT, "tag", "message"));
        assertFalse(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
        
        multi.clear();
        multi.add(bFilter, aFilter);
        assertFalse(multi.apply(Log.ASSERT, "tag", "message"));
        assertFalse(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
}
