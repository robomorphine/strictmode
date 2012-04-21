package com.robomorphine.strictmode.violation;

import java.util.Map;

public class VmViolation extends Violation {
    
    public static abstract class VmViolationFactory extends ViolationFactory {
        VmViolation create(Map<String, String> headers, ViolationException exception) {
            return new VmViolation(headers, exception);
        }
    }
    
    public VmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
