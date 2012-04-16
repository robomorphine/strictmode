package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class NetworkThreadViolation extends ThreadViolation {
    
    public static class NetworkThreadViolationFactory extends ThreadViolationFactory {
        @Override
        ThreadViolation onCreate(Map<String, String> headers,
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
