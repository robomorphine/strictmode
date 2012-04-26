package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.adapter.ViolationStacktraceAdapter;
import com.robomorphine.strictmode.violation.ViolationException;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.support.v4.app.ListFragment;


public class ViolationStacktraceFragment extends ListFragment {
    
    private ViolationGroup mViolationGroup;
    private ViolationStacktraceAdapter mAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mViolationGroup = ViolationFragmentHelper.getViolationGroup(getArguments());        
        ViolationException exception = mViolationGroup.getViolation().getException();
        mAdapter = new ViolationStacktraceAdapter(getActivity(), exception);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
    }
    
    
    
}
