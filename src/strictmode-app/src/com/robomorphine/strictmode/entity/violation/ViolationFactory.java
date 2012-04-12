package com.robomorphine.strictmode.entity.violation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViolationFactory {
    
    
    
    
    public Violation createViolation(String data) {        
        List<String> headers = new LinkedList<String>();
        List<String> stackTrace = new LinkedList<String>();
        List<String> lines = headers;
        BufferedReader reader = new BufferedReader(new StringReader(data));
        
        try {
            String line = null;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() == 0) {
                    lines = stackTrace;
                } else {
                    lines.add(line);
                }
            }
        } catch(IOException ex) {
            throw new IllegalArgumentException("Failed to parse violation data", ex);
        }
        return createViolation(headers, stackTrace);
    }
    
    private Violation createViolation(List<String> headers, List<String> stackTrace) {
        /* TODO: detect what violation we're currently dealing with */
        return new Violation(parseHeaders(headers), parseStackTrace(stackTrace));
    }
    
    private Map<String, String> parseHeaders(List<String> headers) {
        return null;
    }
    
    private ViolationException parseStackTrace(List<String> stackTrace) {
        return null;
    }
}

