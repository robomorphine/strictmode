package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class ExplicitCloseVmViolation extends VmViolation {
    public ExplicitCloseVmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
