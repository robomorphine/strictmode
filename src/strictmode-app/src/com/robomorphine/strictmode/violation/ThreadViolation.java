package com.robomorphine.strictmode.violation;

import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

public class ThreadViolation extends Violation {
    
    public static final String HEADER_KEY_DURATION = "Duration-Millis";
    
    public static final String HEADER_KEY_POLICY = "policy";
    public static final String HEADER_KEY_VIOLATION = "violation";
    public static final String HEADER_KEY_MESSAGE = "msg";
        
    public static final int VIOLATION_DISK_WRITE = 0x01;
    public static final int VIOLATION_DISK_READ = 0x02;
    public static final int VIOLATION_NETWORK = 0x04;
    public static final int VIOLATION_CUSTOM = 0x08; 
    
    public static class ThreadViolationFactory extends ViolationFactory {
        /**
         * @return new instance if factory knows how to create violation from provided information,
         *         or returns null if violation can not be created using provided information.
         */
        final ThreadViolation create(Map<String, String> headers, ViolationException exception) {
            /**
             * This is ThreadViolation if and only if: 
             *  1) There is a header: HEADER_KEY_DURATION
             *  2) There are headers in exception message:
             *      a) HEADER_KEY_POLICY
             *      b) HEADER_KEY_VIOLATION
             */
            headers = new HashMap<String, String>(headers);
            String message = exception.getMessage();
            if(message != null) {
                headers.putAll(parseExceptionMessage(message));
            }
            
            if(headers.containsKey(HEADER_KEY_DURATION) && 
               headers.containsKey(HEADER_KEY_POLICY) && 
               headers.containsKey(HEADER_KEY_VIOLATION)) {
                
                int policy = parsePolicy(headers.get(HEADER_KEY_POLICY));
                int violation = parseViolation(headers.get(HEADER_KEY_VIOLATION));
                if(policy >= 0 && violation >= 0) {
                    return onCreate(headers, exception, policy, violation);
                }
            }
            return null;
        }
        
        ThreadViolation onCreate(Map<String, String> headers, ViolationException exception,
                                 int policy, int violation) {
            return new ThreadViolation(headers, exception);
        }
    }
    
    /**
     * Parses exception message to extract extra headers. The message has format:
     *      key1=value1 key2=value2 key3=value3 
     * 
     * The problem is that value may contain spaces, but keys will not. So there might be cases:
     *      key1=value1 with spaces key2=value2 with spaces key3=value3 with spaces
     */
    @VisibleForTesting
    static Map<String, String> parseExceptionMessage(String message) {
        final Character assigner = '=';
        final Character separator = ' ';
        
        Map<String, String> map = new HashMap<String, String>();
                
        int index = message.length() - 1;
        while(index >= 0) {
            String key = null;
            String value = null;
            
            int assignerIndex = message.lastIndexOf(assigner, index);
            if(assignerIndex < 0) {
                break;
            }
            
            int separatorIndex = message.lastIndexOf(separator, assignerIndex);
            assignerIndex = message.indexOf(assigner, separatorIndex + 1);
            key = message.substring(separatorIndex + 1, assignerIndex);
            if(assignerIndex < message.length() + 1) {
                value = message.substring(assignerIndex + 1, index + 1);
            }
            
            if(key != null) {
                key = key.trim();
            }
            
            if(value != null) {
                value = value.trim();
            }
            
            map.put(key, value);
            
            index = separatorIndex;
        }
        return map;
    }
    
    @VisibleForTesting
    static int parsePolicy(String policy) {
        if(policy == null) {
            return -1;
        }
        
        try {
            return Integer.parseInt(policy);
        } catch(NumberFormatException ex) {
            return -1;
        }
    }
    
    @VisibleForTesting
    static int parseViolation(String violation) {
        if(violation == null) {
            return -1;
        }
        
        try {
            return Integer.parseInt(violation);
        } catch(NumberFormatException ex) {
            return -1;
        }
    }
    
    public ThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
