package com.robomorphine.log.filter.bool;

import com.robomorphine.log.Log;

import junit.framework.TestCase;

public class OrFilterTest extends TestCase {
    
    public void testEmpty() {
        OrFilter empty = new OrFilter();        
        assertFalse(empty.apply(Log.ASSERT, "tag", "message"));
        assertFalse(empty.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
    
    public void testFalse() {
        OrFilter multi = new OrFilter();
        FalseFilter aFilter = new FalseFilter();
        FalseFilter bFilter = new FalseFilter();
        multi.add(aFilter, bFilter);
        
        assertFalse(multi.apply(Log.ASSERT, "tag", "message"));
        assertFalse(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
    
    public void testTrue() {
        OrFilter multi = new OrFilter();
        TrueFilter aFilter = new TrueFilter();
        FalseFilter bFilter = new FalseFilter();
        
        multi.clear();
        multi.add(aFilter, bFilter);        
        assertTrue(multi.apply(Log.ASSERT, "tag", "message"));
        assertTrue(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
        
        multi.clear();
        multi.add(bFilter, aFilter);
        assertTrue(multi.apply(Log.ASSERT, "tag", "message"));
        assertTrue(multi.apply(Log.ASSERT, "anothertag", "anothermessage"));
    }
}
