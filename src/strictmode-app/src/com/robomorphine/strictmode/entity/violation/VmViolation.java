package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class VmViolation extends Violation {
    public VmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
