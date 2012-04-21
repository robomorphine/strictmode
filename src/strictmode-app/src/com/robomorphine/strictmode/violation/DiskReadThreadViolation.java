package com.robomorphine.strictmode.violation;

import java.util.Map;

public class DiskReadThreadViolation extends ThreadViolation {
    
    public static class DiskReadThreadViolationFactory extends ThreadViolationFactory {
        @Override
        ThreadViolation onCreate(Map<String, String> headers,
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
