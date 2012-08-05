package com.robomorphine.strictmode.viewer.violation.filter;

import com.robomorphine.strictmode.viewer.violation.Violation;

public class TimestampViolationFilter implements ViolationFilter {
        
    private static final long serialVersionUID = 1L;
    
    private final long mTimestamp;
    private final int mHashCode;
    public TimestampViolationFilter(long timestamp) {
        mTimestamp = timestamp;
        mHashCode = Long.valueOf(mTimestamp).hashCode();
    }
    
    @Override
    public boolean matches(Violation violation) {
        return mTimestamp < violation.getTimestamp();
    }
    
    @Override
    public boolean usesProperty(String propertyName) {
        if(propertyName.equals(PROPERTY_TIMESTAMP)) {
            return mTimestamp > 0;
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TimestampViolationFilter) {
            TimestampViolationFilter other = (TimestampViolationFilter)o;
            return mTimestamp == other.mTimestamp;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return mHashCode;
    }
}
