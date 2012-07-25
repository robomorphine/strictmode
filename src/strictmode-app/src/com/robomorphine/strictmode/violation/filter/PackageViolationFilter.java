package com.robomorphine.strictmode.violation.filter;

import com.google.common.base.Objects;
import com.robomorphine.strictmode.violation.Violation;

import android.text.TextUtils;

import javax.annotation.Nullable;

public class PackageViolationFilter implements ViolationFilter {
        
    private static final long serialVersionUID = 1L;
    
    private final String mPackageName;
    public PackageViolationFilter(@Nullable String packageName) {
        mPackageName = packageName;
    }
    
    @Override
    public boolean matches(Violation violation) {
        return TextUtils.isEmpty(mPackageName) || mPackageName.equals(violation.getPackage());
    }
    
    @Override
    public boolean usesProperty(String propertyName) {
        if(propertyName.equals(PROPERTY_PACKAGE)) {
            return !TextUtils.isEmpty(mPackageName);
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof PackageViolationFilter) {
            PackageViolationFilter other = (PackageViolationFilter)o;
            return Objects.equal(mPackageName, other.mPackageName);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return  Objects.hashCode(mPackageName);
    }
}
