package com.robomorphine.strictmode.violation.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MultiFilter implements ViolationFilter {
    
    private static final long serialVersionUID = 1L;
         
    private final List<ViolationFilter> mFilters = new ArrayList<ViolationFilter>();
    private final List<ViolationFilter> mPublicFilters = Collections.unmodifiableList(mFilters);
    private final int mHashCode;
         
    public MultiFilter(ViolationFilter...filters) {
        for(ViolationFilter filter : filters) {
            mFilters.add(filter);
        }
        
        int hashCode = 0;
        for(ViolationFilter filter : mFilters) {
            hashCode = hashCode * 31 + filter.hashCode();
        }
        mHashCode = hashCode;
    }
    
    protected List<ViolationFilter> getFilters() {
        return mPublicFilters;
    }    
    
    @Override
    public boolean usesProperty(String propertyName) {
        for(ViolationFilter filter : mFilters) {
            if(filter.usesProperty(propertyName)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultiFilter)) {
            return false;
        }
        MultiFilter other = (MultiFilter)o;
        if(mFilters.size() != other.mFilters.size()) {
            return false;
        }        
        
        int size = mFilters.size();
        for(int i = 0; i < size; i++) {
            ViolationFilter a = mFilters.get(i);
            ViolationFilter b = other.mFilters.get(i);
            if(!a.equals(b)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return mHashCode;
    }
}
