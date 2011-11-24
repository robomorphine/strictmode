package com.robomorphine.log.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ForwardingList;

public abstract class MultiFilter extends ForwardingList<Filter> implements Filter {
    
    private final LinkedList<Filter> mFilters = new LinkedList<Filter>();
    
    public MultiFilter() {
    }    
    
    @Override
    protected List<Filter> delegate() {
        return mFilters;
    }

    public void add(Filter... filters) {
        for (Filter filter : filters) {
            add(filter);
        }
    }
}
