package com.robomorphine.strictmode.violation;

import java.util.Map;

public class ExplicitTerminationVmViolation extends VmViolation {
    
    private static final long serialVersionUID = 1L;
    
    private static final String VIOLATION_EXCEPTION_CLASS_NAME = Throwable.class.getName();
    private static final String VIOLATION_MESSAGE_PREFIX = "Explicit termination method '"; 
    private static final String VIOLATION_MESSAGE_POSTFIX = "' not called";
        
    static class ExplicitTerminationVmViolationFactory extends VmViolationFactory {
        ExplicitTerminationVmViolation create(Map<String, String> headers, ViolationException exception) {
            String message = exception.getMessage();
            if(VIOLATION_EXCEPTION_CLASS_NAME.equalsIgnoreCase(exception.getClassName()) &&
               message != null && 
               message.startsWith(VIOLATION_MESSAGE_PREFIX) && 
               message.endsWith(VIOLATION_MESSAGE_POSTFIX)) {
                return new ExplicitTerminationVmViolation(headers, exception);                
            }
            return null;
        }
    }
    
    public ExplicitTerminationVmViolation(Map<String, String> headers, ViolationException exception) {
        super(headers, exception);
    }
}
