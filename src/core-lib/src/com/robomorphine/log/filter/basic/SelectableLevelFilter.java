package com.robomorphine.log.filter.basic;

import com.robomorphine.log.Log;
import com.robomorphine.log.filter.Filter;

import java.util.HashMap;
import java.util.Map;

public class SelectableLevelFilter implements Filter {

    private final Map<Integer, Boolean> mLevels = new HashMap<Integer, Boolean>();


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
        return (enabled == null || enabled);
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
