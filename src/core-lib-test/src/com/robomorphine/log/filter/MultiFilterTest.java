package com.robomorphine.log.filter;

import com.robomorphine.log.filter.bool.TrueFilter;

import junit.framework.TestCase;

public class MultiFilterTest extends TestCase {
    
    private static class MultiFilterUnderTest extends MultiFilter {
        @Override
        public boolean apply(int level, String tag, String msg) {            
            return false;
        }        
    }
    
    public void testAdd() {
        TrueFilter afilter = new TrueFilter();
        TrueFilter bfilter = new TrueFilter();
        TrueFilter cfilter = new TrueFilter();
        
        MultiFilter mfilter = new MultiFilterUnderTest();
        mfilter.add(afilter, bfilter, cfilter);
        assertEquals(3, mfilter.size());
        assertSame(afilter, mfilter.get(0));
        assertSame(bfilter, mfilter.get(1));
        assertSame(cfilter, mfilter.get(2));        
    }
}
