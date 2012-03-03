package com.robomorphine.log.filter.basic;

import com.robomorphine.log.Log;
import com.robomorphine.log.filter.Filter;

public class LevelFilter implements Filter {
    private int mLevel;

    public LevelFilter() {
        this(Log.VERBOSE);
    }

    public LevelFilter(int level) {
        mLevel = level;
    }
    
    public void setLevel(int level) {
        mLevel = level;
    }
    
    public int getLevel() {
        return mLevel;
    }

    public boolean apply(int level, String tag, String msg) {
        return level >= mLevel;
    };
    
    @Override
    public String toString() {
        
        return "level > " + Log.toString(mLevel);
    }
}
