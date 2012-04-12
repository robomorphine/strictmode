package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class DiskReadThreadViolation extends ThreadViolation {
    public DiskReadThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
