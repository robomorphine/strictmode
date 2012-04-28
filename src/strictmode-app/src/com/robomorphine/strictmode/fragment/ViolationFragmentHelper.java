package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;

public class ViolationFragmentHelper {

    /**
     * Type: ViolationGroup
     */
    public final static String EXTRA_VIOLATION_GROUP = "violation_group";
    
    /**
     * Type: Violation
     */
    public final static String EXTRA_VIOLATION = "violation";
    
    
    public static ViolationGroup getViolationGroup(Bundle args) {
        ViolationGroup violationGroup  = null;
        if(args != null) {
            violationGroup = (ViolationGroup)args.getSerializable(EXTRA_VIOLATION_GROUP);    
        }
        
        if(violationGroup == null) {
            throw new IllegalArgumentException("ViolationGroup not specified.");
        }
        return violationGroup;
    }
    
    public static Violation getViolation(Bundle args) {
        Violation violation = null;
        if(args != null) {
            violation = (Violation)args.getSerializable(EXTRA_VIOLATION);    
        }
        
        if(violation == null) {
            throw new IllegalArgumentException("Violation not specified.");
        }
        return violation;
    }
}
