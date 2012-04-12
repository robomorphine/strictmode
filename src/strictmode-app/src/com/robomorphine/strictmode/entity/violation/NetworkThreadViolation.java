package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class NetworkThreadViolation extends ThreadViolation {
    public NetworkThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
