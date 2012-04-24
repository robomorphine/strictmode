package com.robomorphine.strictmode.violation;

import java.util.Map;

public class CustomThreadViolation extends ThreadViolation {
    
    private static final long serialVersionUID = 1L;
    
    public static class CustomThreadViolationFactory extends ThreadViolationFactory {
        @Override
        ThreadViolation onCreate(Map<String, String> headers,
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
