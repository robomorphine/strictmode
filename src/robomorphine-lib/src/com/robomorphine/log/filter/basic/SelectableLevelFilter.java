package com.robomorphine.log.filter.basic;

import java.util.HashMap;

import com.robomorphine.log.Log;
import com.robomorphine.log.filter.Filter;

public class SelectableLevelFilter implements Filter {

    private HashMap<Integer, Boolean> mLevels = new HashMap<Integer, Boolean>();

    public SelectableLevelFilter() {
    }

    public void enable(int level, boolean enable) {
        mLevels.put(level, enable);
    }
    
    public void enableAll() {
        for(int level : Log.LEVELS) {
            enable(level, true);
        }
    }
    
    public void disableAll() {
        for(int level : Log.LEVELS) {
            enable(level, false);
        }
    }

    public boolean apply(int level, String tag, String msg) {
        Boolean enabled = mLevels.get(level);
        if (enabled == null || enabled) {
            return true;
        } else {
            return false;
        }
    };
    
    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder();
        builder.append("[levels: ");
        for(int level : Log.LEVELS) {
            
            builder.append(Log.toString(level));
            builder.append("=");
            builder.append(mLevels.get(level));
            builder.append(";");
        }
        builder.append("]");
        return builder.toString();
    }
}
