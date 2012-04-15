package com.robomorphine.strictmode.entity.violation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Violation {    
    
    public static class ViolationFactory {
        /**
         * @return new instance if factory knows how to create violation from provided information,
         *         or returns null if violation can not be created using provided information.
         */
        Violation create(Map<String, String> headers, ViolationException exception) {
            return new Violation(headers, exception);
        }
    }
    
    
    private final Map<String, String> mHeaders;
    private final ViolationException mException;
    
    public Violation(Map<String, String> headers, ViolationException exception) {
        mHeaders = Collections.unmodifiableMap(new HashMap<String, String>(headers));
        mException = exception;
    }
    
    public Map<String, String> getHeaders() {
        return mHeaders;
    }
    
    public ViolationException getException() {
        return mException;
    }
}
