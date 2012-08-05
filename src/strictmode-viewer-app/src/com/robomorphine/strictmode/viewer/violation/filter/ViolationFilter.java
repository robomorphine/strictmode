package com.robomorphine.strictmode.viewer.violation.filter;

import com.robomorphine.strictmode.viewer.violation.Violation;

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
    String PROPERTY_PACKAGE = "package";
    String PROPERTY_TIMESTAMP = "timestamp";
        
    boolean matches(@Nonnull Violation violation);
    
    boolean usesProperty(@Nonnull String propertyName);
}
