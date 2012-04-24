package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ViolationDetailsFragment extends Fragment {

    /**
     * Type: ViolationGroup
     */
    public final static String EXTRA_VIOLATION = "violation";
    
    
    private ViolationGroup mViolationGroup;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        if(args != null) {
            mViolationGroup = (ViolationGroup)args.getSerializable(EXTRA_VIOLATION);    
        }
        
        if(mViolationGroup == null) {
            throw new IllegalArgumentException("Violation not specified.");
        }
    }
    
    protected ViolationGroup getViolation() {
        return mViolationGroup;
    }
    
}
