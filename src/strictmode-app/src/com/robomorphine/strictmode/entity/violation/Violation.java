package com.robomorphine.strictmode.entity.violation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Violation {    
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
