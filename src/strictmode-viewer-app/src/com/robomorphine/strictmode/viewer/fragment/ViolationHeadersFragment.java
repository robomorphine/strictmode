package com.robomorphine.strictmode.viewer.fragment;

import com.actionbarsherlock.app.SherlockListFragment;
import com.robomorphine.strictmode.viewer.adapter.ViolationHeadersAdapter;
import com.robomorphine.strictmode.viewer.violation.Violation;

import android.os.Bundle;

public class ViolationHeadersFragment extends SherlockListFragment {
    
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
