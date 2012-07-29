package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.adapter.ViolationHeadersAdapter;
import com.robomorphine.strictmode.violation.Violation;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class ViolationHeadersFragment extends ListFragment {
    
    private Violation mViolation;//NOPMD
    private ViolationHeadersAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViolation = ViolationFragmentHelper.getViolation(getArguments());
        mAdapter = new ViolationHeadersAdapter(getActivity(), mViolation);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
        getListView().setFastScrollEnabled(true);
    }
    
}
