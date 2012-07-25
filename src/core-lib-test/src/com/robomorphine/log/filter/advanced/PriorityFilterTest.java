package com.robomorphine.log.filter.advanced;

import junit.framework.TestCase;

import com.robomorphine.log.Log;
import com.robomorphine.log.filter.Filter;
import com.robomorphine.log.filter.advanced.PriorityFilter.FilterAction;
import com.robomorphine.log.filter.basic.TagFilter;

public class PriorityFilterTest extends TestCase {
        
    public void testCtor() {        
        PriorityFilter<Filter> pfilter;
        
        pfilter = new PriorityFilter<Filter>(FilterAction.Exclude);
        assertEquals(FilterAction.Exclude, pfilter.getDefaultAction());
        
        pfilter = new PriorityFilter<Filter>(FilterAction.Include);
        assertEquals(FilterAction.Include, pfilter.getDefaultAction());
        
        pfilter = new PriorityFilter<Filter>();
        assertEquals(FilterAction.Include, pfilter.getDefaultAction());
        
        try {
            new PriorityFilter<Filter>(FilterAction.Ignore);
            fail();
        } catch (IllegalArgumentException ex) {
            // ok
        }
        
        try {
            new PriorityFilter<Filter>(null);
            fail();
        } catch(NullPointerException ex) {
            //ok
        }
    }
    
    public void testGetSetDefaultAction() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.setDefaultAction(FilterAction.Exclude);
        assertEquals(FilterAction.Exclude, pfilter.getDefaultAction());
        
        pfilter.setDefaultAction(FilterAction.Include);
        assertEquals(FilterAction.Include, pfilter.getDefaultAction());
        
        try {
            pfilter.setDefaultAction(FilterAction.Ignore);
            fail();
        } catch(IllegalArgumentException ex) {
            //ok
        }
    }
    
    public void testGetSetDefaultAction_nulls() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        try {
            pfilter.setDefaultAction(null);
            fail();
        } catch(NullPointerException ex) {
            //ok
        }
    }
    
    public void testDefaultAction() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.setDefaultAction(FilterAction.Exclude);
        assertFalse(pfilter.apply(Log.FATAL, "testTag", "message"));
        
        pfilter.setDefaultAction(FilterAction.Include);
        assertTrue(pfilter.apply(Log.FATAL, "testTag", "message"));
    }
    
    public void testSingleSubfilter_include() {        
        TagFilter filter = new TagFilter("*a*");
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.add(filter, FilterAction.Include);
        pfilter.setDefaultAction(FilterAction.Exclude);
        
        assertFalse(pfilter.apply(Log.FATAL, "xyz", "test test test"));
        assertTrue(pfilter.apply(Log.FATAL, "bac", "test test test"));        
    }
    
    public void testSingleSubfilter_exclude() {        
        TagFilter filter = new TagFilter("*a*");
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.add(filter, FilterAction.Exclude);
        pfilter.setDefaultAction(FilterAction.Include);
        
        assertTrue(pfilter.apply(Log.FATAL, "xyz", "test test test"));
        assertFalse(pfilter.apply(Log.FATAL, "bac", "test test test"));
    }
    
    public void testSingleSubfilter_ignore() {        
        TagFilter filter = new TagFilter("*a*");
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.add(filter, FilterAction.Ignore);      
        
        pfilter.setDefaultAction(FilterAction.Include);        
        assertTrue(pfilter.apply(Log.FATAL, "bac", "test test test"));
        
        pfilter.setDefaultAction(FilterAction.Exclude);        
        assertFalse(pfilter.apply(Log.FATAL, "bac", "test test test"));
    }
    
    public void testMultipleSubfilters() {
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        
        pfilter.add(afilter, FilterAction.Include);      
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        pfilter.setDefaultAction(FilterAction.Exclude);
        
        /* first filter matches */
        assertTrue(pfilter.apply(Log.FATAL, "abc", "test"));
        
        /* second filter matches */
        assertFalse(pfilter.apply(Log.FATAL, "xbc", "test"));
        
        /* last filter matches, but its ignored */
        assertFalse(pfilter.apply(Log.FATAL, "xyc", "test"));
    }
    
    /***************************
     **    Managing filters   **  
     ***************************/    
    
    public void testIndexOf() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        assertEquals(0, pfilter.indexOf(afilter));
        assertEquals(1, pfilter.indexOf(bfilter));
        assertEquals(2, pfilter.indexOf(cfilter));
    }
    
    public void testContains() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);        
        
        assertTrue(pfilter.contains(afilter));
        assertTrue(pfilter.contains(bfilter));
        assertFalse(pfilter.contains(cfilter));
    }
    
    public void testAdd() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
                
        assertTrue(pfilter.add(afilter, FilterAction.Include));
        assertFalse(pfilter.add(afilter, FilterAction.Exclude));
        assertEquals(FilterAction.Include, pfilter.getAction(pfilter.indexOf(afilter)));
        
        assertTrue(pfilter.add(bfilter, FilterAction.Exclude));        
        assertFalse(pfilter.add(bfilter, FilterAction.Include));        
        assertEquals(FilterAction.Exclude, pfilter.getAction(pfilter.indexOf(bfilter)));
        
        assertEquals(2, pfilter.size());
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
    }
    
    public void testAdd_insert() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
                        
        assertTrue(pfilter.add(afilter, FilterAction.Include));
        assertTrue(pfilter.add(0, bfilter, FilterAction.Exclude));
        assertTrue(pfilter.add(1, cfilter, FilterAction.Exclude));
        
        assertFalse(pfilter.add(0, bfilter, FilterAction.Exclude));
        
        assertEquals(3, pfilter.size());
        assertSame(afilter, pfilter.getFilter(2));
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
    }
    
    public void testAdd_nulls() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        try {
            pfilter.add(null, FilterAction.Include);
            fail();
        } catch(NullPointerException ex) {
            // ok
        }
        
        try {
            pfilter.add(new TagFilter("*a*"), null);
            fail();
        } catch(NullPointerException ex) {
            // ok
        }
    }
    
    public void testRemove() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
                        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Exclude);
        pfilter.remove(pfilter.indexOf(bfilter));        
        
        assertEquals(2, pfilter.size());
        assertSame(afilter, pfilter.getFilter(0));        
        assertSame(cfilter, pfilter.getFilter(1));
    }
    
    public void testClear() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
                        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Exclude);
        
        pfilter.clear();
        
        assertEquals(0, pfilter.size());        
    }
    
    public void testGetFilter() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
                        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        assertEquals(afilter, pfilter.getFilter(0));
        assertEquals(bfilter, pfilter.getFilter(1));
        assertEquals(cfilter, pfilter.getFilter(2));        
    }
    
    public void testSetGetAction() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
                        
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        assertEquals(FilterAction.Include, pfilter.getAction(0));
        assertEquals(FilterAction.Exclude, pfilter.getAction(1));
        assertEquals(FilterAction.Ignore, pfilter.getAction(2));
        
        pfilter.setAction(0, FilterAction.Ignore);
        pfilter.setAction(1, FilterAction.Include);
        pfilter.setAction(2, FilterAction.Exclude);
        
        assertEquals(FilterAction.Ignore, pfilter.getAction(0));
        assertEquals(FilterAction.Include, pfilter.getAction(1));
        assertEquals(FilterAction.Exclude, pfilter.getAction(2));
    }
    
    /***************************
     **    Filter reordering  **  
     ***************************/
    
    public void testSwap() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
                
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.swap(0, 0);
        pfilter.swap(1, 1);
        pfilter.swap(2, 2);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.swap(0, 1);
        
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(afilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.swap(1, 2);
        
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));
        
        pfilter.swap(0, 2);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
        assertSame(bfilter, pfilter.getFilter(2));
    }
    
    public void testSwap_badIndex() {        
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        try {
            pfilter.swap(0, 3);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            //ok
        }        
    }
    
    public void testMoveUp() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        pfilter.moveUp(0);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveUp(1);
        
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(afilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveUp(2);
        
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));        
    }   
    
    public void testMoveDown() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        pfilter.moveDown(2);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveDown(1);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
        assertSame(bfilter, pfilter.getFilter(2));
        
        pfilter.moveDown(0);
        
        assertSame(cfilter, pfilter.getFilter(0));
        assertSame(afilter, pfilter.getFilter(1));
        assertSame(bfilter, pfilter.getFilter(2));        
    }
    
    public void testMoveFirst() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        pfilter.moveFirst(0);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveFirst(1);
        
        assertSame(bfilter, pfilter.getFilter(0));
        assertSame(afilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveFirst(2);
        
        assertSame(cfilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));        
    }
    
    public void testMoveLast() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        pfilter.moveLast(2);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.moveLast(1);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(cfilter, pfilter.getFilter(1));
        assertSame(bfilter, pfilter.getFilter(2));
        
        pfilter.moveLast(0);
        
        assertSame(cfilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));        
    }
    
    public void testReverse() {
        PriorityFilter<Filter> pfilter = new PriorityFilter<Filter>();
        TagFilter afilter = new TagFilter("*a*");
        TagFilter bfilter = new TagFilter("*b*");
        TagFilter cfilter = new TagFilter("*c*");
        TagFilter dfilter = new TagFilter("*d*");
       
        pfilter.add(afilter, FilterAction.Include);
        pfilter.add(bfilter, FilterAction.Exclude);
        pfilter.add(cfilter, FilterAction.Ignore);
        
        assertSame(afilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(cfilter, pfilter.getFilter(2));
        
        pfilter.reverse();
        
        assertSame(cfilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));
        
        pfilter.add(dfilter, FilterAction.Include);
        
        assertSame(cfilter, pfilter.getFilter(0));
        assertSame(bfilter, pfilter.getFilter(1));
        assertSame(afilter, pfilter.getFilter(2));
        assertSame(dfilter, pfilter.getFilter(3));
        
        pfilter.reverse();
        
        assertSame(dfilter, pfilter.getFilter(0));
        assertSame(afilter, pfilter.getFilter(1));
        assertSame(bfilter, pfilter.getFilter(2));
        assertSame(cfilter, pfilter.getFilter(3));
                
    }
}
