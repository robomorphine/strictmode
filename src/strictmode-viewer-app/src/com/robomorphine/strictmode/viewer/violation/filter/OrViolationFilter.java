package com.robomorphine.strictmode.viewer.violation.filter;

import com.robomorphine.strictmode.viewer.violation.Violation;

public class OrViolationFilter extends MultiFilter {
       
    private static final long serialVersionUID = 1L;
       
    public OrViolationFilter(ViolationFilter...filters) {
        super(filters);
    }
    
    @Override
    public boolean matches(Violation violation) {
        for(ViolationFilter filter : getFilters()) {
            if(filter.matches(violation)) {
                return true;
            }
        }
        return false;
    }
}
