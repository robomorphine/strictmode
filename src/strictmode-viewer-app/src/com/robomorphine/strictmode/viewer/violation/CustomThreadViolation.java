package com.robomorphine.strictmode.viewer.violation;

import java.util.Map;

public class CustomThreadViolation extends ThreadViolation {
    
    private static final long serialVersionUID = 1L;
    
    static class CustomThreadViolationFactory extends ThreadViolationFactory {
        @Override
        protected ThreadViolation onCreate(Map<String, String> headers,
                                           ViolationException exception,
                                           int policy, int violation) {
            
            if(violation == VIOLATION_CUSTOM) {
                return new CustomThreadViolation(headers, exception);
            }
            return null;
        }
    }
    
    public CustomThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
