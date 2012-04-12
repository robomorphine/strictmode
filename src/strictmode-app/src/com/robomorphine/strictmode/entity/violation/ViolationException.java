package com.robomorphine.strictmode.entity.violation;

public class ViolationException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String mClassName;
   
    public ViolationException(String className, String message, Throwable cause) {
        super(message, cause);
        mClassName = className;
    }
    
    public ViolationException(String className, String message) {
        this(className, message, null);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", mClassName, getMessage());
    }
}
