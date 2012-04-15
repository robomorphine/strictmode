package com.robomorphine.strictmode.entity.violation;

import javax.annotation.Nullable;

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
    
    @Nullable
    public String getClassName() {
        return mClassName;
    }
    
    @Nullable
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    
    @Override
    public String toString() {
        String message = getMessage();
        if(mClassName == null) {
            return super.toString();
        }
        
        if(message == null) {
            return mClassName;
        }
        
        return String.format("%s: %s", mClassName, getMessage());
    }
}
