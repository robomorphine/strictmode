package com.robomorphine.strictmode.entity.violation;

import com.google.common.annotations.VisibleForTesting;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class ThreadViolation extends Violation {
    
    public static class ThreadViolationFactory {
        /**
         * @return new instance if factory knows how to create violation from provided information,
         *         or returns null if violation can not be created using provided information.
         */
        ThreadViolation create(Map<String, String> headers, ViolationException exception) {
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
    
    public ThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
