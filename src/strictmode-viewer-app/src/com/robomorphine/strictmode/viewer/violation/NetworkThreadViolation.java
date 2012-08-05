package com.robomorphine.strictmode.viewer.violation;

import java.util.Map;

public class NetworkThreadViolation extends ThreadViolation {
    
    private static final long serialVersionUID = 1L;
    
    static class NetworkThreadViolationFactory extends ThreadViolationFactory {
        @Override
        protected ThreadViolation onCreate(Map<String, String> headers,
                                           ViolationException exception,
                                           int policy, int violation) {
            
            if(violation == VIOLATION_NETWORK) {
                return new NetworkThreadViolation(headers, exception);
            }
            return null;
        }
    }
    
    public NetworkThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
