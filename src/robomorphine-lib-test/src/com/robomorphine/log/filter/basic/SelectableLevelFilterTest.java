package com.robomorphine.log.filter.basic;

import junit.framework.TestCase;

import com.robomorphine.log.Log;

public class SelectableLevelFilterTest extends TestCase {
    
    public void testAllLevels() {
        
        SelectableLevelFilter levelFilter = new SelectableLevelFilter();
        
        for(int curLevel : Log.LEVELS) {            
            assertTrue(levelFilter.apply(curLevel, "tag", "message"));
        }
        
        levelFilter.enableAll();
        
        for(int curLevel : Log.LEVELS) {            
            assertTrue(levelFilter.apply(curLevel, "tag", "message"));
        }
        
        levelFilter.disableAll();
        
        for(int curLevel : Log.LEVELS) {            
            assertFalse(levelFilter.apply(curLevel, "tag", "message"));
        }
    }
    
    public void testLevels() {
        for(int level : Log.LEVELS) {
            SelectableLevelFilter levelFilter = new SelectableLevelFilter();
            levelFilter.enable(level, false);            
            for(int curLevel : Log.LEVELS) {
                boolean res = levelFilter.apply(curLevel, "tag", "message");
                assertEquals(curLevel != level, res);
            }
        }
    }
}
