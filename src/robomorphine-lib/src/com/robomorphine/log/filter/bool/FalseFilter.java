package com.robomorphine.log.filter.bool;

import com.robomorphine.log.filter.Filter;

public class FalseFilter implements Filter {
    @Override
    public boolean apply(int level, String tag, String msg) {
        return false;
    }
    
    @Override
    public String toString() {
        return "false";
    }
}
