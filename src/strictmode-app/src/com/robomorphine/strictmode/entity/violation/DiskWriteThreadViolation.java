package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class DiskWriteThreadViolation extends ThreadViolation {
    public DiskWriteThreadViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
