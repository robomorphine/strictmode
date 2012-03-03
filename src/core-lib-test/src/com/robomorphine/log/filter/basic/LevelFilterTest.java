package com.robomorphine.log.filter.basic;

import com.robomorphine.log.Log;

import junit.framework.TestCase;

public class LevelFilterTest extends TestCase {
    
    public void testLevels() {
        for(int level : Log.LEVELS) {
            LevelFilter levelFilter = new LevelFilter(level);
            for(int curLevel : Log.LEVELS) {
                boolean res = levelFilter.apply(curLevel, "tag", "message");
                assertEquals(curLevel >= level, res);
            }
        }
    }
}
