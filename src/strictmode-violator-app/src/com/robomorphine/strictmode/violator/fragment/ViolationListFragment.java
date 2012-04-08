package com.robomorphine.strictmode.violator.fragment;

import com.robomorphine.strictmode.violator.adapter.ViolationListAdapter;
import com.robomorphine.strictmode.violator.violation.DiskReadViolation;
import com.robomorphine.strictmode.violator.violation.DiskWriteViolation;
import com.robomorphine.strictmode.violator.violation.FakeSlowCallViolation;
import com.robomorphine.strictmode.violator.violation.SharedPreferencesCommitViolation;
import com.robomorphine.strictmode.violator.violation.StackTraceRandomizer;
import com.robomorphine.strictmode.violator.violation.Violation;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class ViolationListFragment extends ListFragment {

    private StackTraceRandomizer mStackTraceRandomizer = new StackTraceRandomizer();
    private ViolationListAdapter mAdapter;
    
    private static List<Violation> createViolations(Context ctx) {
        LinkedList<Violation> violations = new LinkedList<Violation>();
        violations.add(new SharedPreferencesCommitViolation(ctx));
        violations.add(new DiskReadViolation(ctx));
        violations.add(new DiskWriteViolation(ctx));
        violations.add(new FakeSlowCallViolation(ctx));
        
        return violations;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ViolationListAdapter(getActivity(), createViolations(getActivity()));
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setListAdapter(mAdapter);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Violation violation = mAdapter.getItem(position);
        
        mStackTraceRandomizer.call(10, new Runnable() {
            @Override
            public void run() {
                violation.violate();        
            }
        });
        
    }
}
