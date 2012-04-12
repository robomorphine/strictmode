package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class CustomThreadViolation extends ThreadViolation {
    public CustomThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
