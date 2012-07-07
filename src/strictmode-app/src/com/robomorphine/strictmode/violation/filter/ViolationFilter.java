package com.robomorphine.strictmode.violation.filter;

import com.robomorphine.strictmode.violation.Violation;

import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * Each filter must be serializable. 
 * Filter is not required to maintain version compatibility. 
 * There will be no serialized instances of older versions that are deserialized.
 * 
 * @author inazaruk
 */
public interface ViolationFilter extends Serializable {
    
    public static final String PROPERTY_PACKAGE = "package";
    public static final String PROPERTY_TIMESTAMP = "timestamp";
        
    public boolean matches(@Nonnull Violation violation);
    
    public boolean usesProperty(@Nonnull String propertyName);
}
