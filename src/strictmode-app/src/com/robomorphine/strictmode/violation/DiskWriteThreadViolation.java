package com.robomorphine.strictmode.violation;

import java.util.Map;

public class DiskWriteThreadViolation extends ThreadViolation {
    
    private static final long serialVersionUID = 1L;
    
    static class DiskWriteThreadViolationFactory extends ThreadViolationFactory {
        @Override
        protected ThreadViolation onCreate(Map<String, String> headers,
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
