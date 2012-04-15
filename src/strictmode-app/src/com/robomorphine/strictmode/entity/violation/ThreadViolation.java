package com.robomorphine.strictmode.entity.violation;

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
    
    public ThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
