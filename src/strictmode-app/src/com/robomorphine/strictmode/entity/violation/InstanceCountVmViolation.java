package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class InstanceCountVmViolation extends VmViolation {
    public InstanceCountVmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
