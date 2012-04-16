package com.robomorphine.strictmode.entity.violation;

import java.util.Map;

public class InstanceCountVmViolation extends VmViolation {
    
    private final static String HEADER_KEY_INSTANCE_COUNT = "Instance-Count";
    private final static String VIOLATION_EXCEPTION_CLASS_NAME = "android.os.StrictMode$InstanceCountViolation";
    
    
    static class InstanceCountVmViolationFactory extends VmViolationFactory {
        InstanceCountVmViolation create(Map<String, String> headers, ViolationException exception) {
            if(VIOLATION_EXCEPTION_CLASS_NAME.equals(exception.getClassName()) && 
               headers.containsKey(HEADER_KEY_INSTANCE_COUNT)) {
                return new InstanceCountVmViolation(headers, exception);
            }
            return null;
        }
    }
    
    public InstanceCountVmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
