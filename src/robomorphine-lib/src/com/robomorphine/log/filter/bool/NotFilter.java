package com.robomorphine.log.filter.bool;

import com.google.common.base.Preconditions;
import com.robomorphine.log.filter.Filter;

public class NotFilter implements Filter {

    private final Filter mFilter;

    public NotFilter(Filter filter) {
        Preconditions.checkNotNull(filter);
        mFilter = filter;
    }

    @Override
    public boolean apply(int level, String tag, String msg) {
        return !mFilter.apply(level, tag, msg);
    }
    
    @Override
    public String toString() {
        return "not [ " + mFilter.toString() + " ]";
    }
}	
