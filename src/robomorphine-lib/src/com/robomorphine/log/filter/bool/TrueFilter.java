package com.robomorphine.log.filter.bool;

import com.robomorphine.log.filter.Filter;

public class TrueFilter implements Filter {
    @Override
    public boolean apply(int level, String tag, String msg) {
        return true;
    }
    
    @Override
    public String toString() {
        return "true";
    }
}
