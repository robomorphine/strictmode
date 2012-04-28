package com.robomorphine.strictmode.adapter;


import com.robomorphine.strictmode.fragment.ViolationFragmentHelper;
import com.robomorphine.strictmode.fragment.ViolationHeadersFragment;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViolationHeadersPagerAdapter extends FragmentPagerAdapter {
    
    private final ViolationGroup mViolationGroup;    
    
    public ViolationHeadersPagerAdapter(FragmentManager fragmentManager, ViolationGroup group) {
        super(fragmentManager);       
        mViolationGroup = group;
    }
    
    @Override
    public int getCount() {
        return mViolationGroup.getSize();
    }
    
    @Override
    public Fragment getItem(int position) {
        Violation violation = mViolationGroup.getViolation(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ViolationFragmentHelper.EXTRA_VIOLATION, violation);
        
        ViolationHeadersFragment fragment = new ViolationHeadersFragment();        
        fragment.setArguments(bundle);       
       
        return fragment;
    }
}
