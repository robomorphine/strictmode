package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class ThreadViolation extends Violation {
    public ThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
