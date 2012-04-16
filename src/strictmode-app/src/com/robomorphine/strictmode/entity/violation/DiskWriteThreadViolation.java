package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class DiskWriteThreadViolation extends ThreadViolation {
    
    public static class DiskWriteThreadViolationFactory extends ThreadViolationFactory {
        @Override
        ThreadViolation onCreate(Map<String, String> headers,
                                 ViolationException exception,
                                 int policy, int violation) {
            
            if(violation == VIOLATION_DISK_WRITE) {
                return new DiskWriteThreadViolation(headers, exception);
            }
            return null;
        }
    }
    
    public DiskWriteThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
