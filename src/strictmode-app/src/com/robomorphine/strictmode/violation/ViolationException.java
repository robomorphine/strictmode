package com.robomorphine.strictmode.violation;

import com.google.common.base.Objects;

import java.util.Arrays;

import javax.annotation.Nullable;

public class ViolationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private final String mClassName;
   
    public ViolationException(String className, String message, ViolationException cause) {
        super(message, cause);
        mClassName = className;
        setStackTrace(new StackTraceElement[0]);
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
    public boolean equals(Object o) {
        if(o instanceof ViolationException) {
            ViolationException other = (ViolationException)o;
               
            StackTraceElement [] elements = getStackTrace();
            StackTraceElement [] otherElements = other.getStackTrace();
            return Objects.equal(getMessage(), other.getMessage()) && 
                   Objects.equal(mClassName, other.mClassName) &&
                   Arrays.equals(elements, otherElements) && 
                   Objects.equal(getCause(), other.getCause());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hashCode = Objects.hashCode(getMessage(),
                                        mClassName,
                                        Arrays.hashCode(getStackTrace()),
                                        getCause());
        return hashCode;
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
