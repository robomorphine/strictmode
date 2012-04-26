package com.robomorphine.strictmode.violation;

import java.util.Map;

public class DiskReadThreadViolation extends ThreadViolation {
    
    private static final long serialVersionUID = 1L;
    
    static class DiskReadThreadViolationFactory extends ThreadViolationFactory {
        @Override
        protected ThreadViolation onCreate(Map<String, String> headers,
                                           ViolationException exception,
                                           int policy, int violation) {
            
            if(violation == VIOLATION_DISK_READ) {
                return new DiskReadThreadViolation(headers, exception);
            }
            return null;
        }
    }
    
    public DiskReadThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
