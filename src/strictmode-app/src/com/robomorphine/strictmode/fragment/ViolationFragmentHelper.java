package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;

public class ViolationFragmentHelper {

    /**
     * Type: ViolationGroup
     */
    public final static String EXTRA_VIOLATION = "violation";
    
    
    public static ViolationGroup getViolationGroup(Bundle args) {
        ViolationGroup violationGroup  = null;
        if(args != null) {
            violationGroup = (ViolationGroup)args.getSerializable(EXTRA_VIOLATION);    
        }
        
        if(violationGroup == null) {
            throw new IllegalArgumentException("Violation not specified.");
        }
        return violationGroup;
    }
}
