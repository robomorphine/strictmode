package com.robomorphine.strictmode.violator.fragment;

import com.robomorphine.strictmode.violator.adapter.ViolationListAdapter;
import com.robomorphine.strictmode.violator.violation.DiskReadReceiverViolation;
import com.robomorphine.strictmode.violator.violation.DiskReadViolation;
import com.robomorphine.strictmode.violator.violation.DiskWriteViolation;
import com.robomorphine.strictmode.violator.violation.ExplicitTerminationViolation;
import com.robomorphine.strictmode.violator.violation.FakeSlowCallViolation;
import com.robomorphine.strictmode.violator.violation.InstanceCountViolation;
import com.robomorphine.strictmode.violator.violation.MultipleSlowCallsViolation;
import com.robomorphine.strictmode.violator.violation.NetworkViolation;
import com.robomorphine.strictmode.violator.violation.SharedPreferencesCommitViolation;
import com.robomorphine.strictmode.violator.violation.Violation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class ViolationListFragment extends ListFragment {
    
    private ViolationListAdapter mAdapter;
    
    private static List<Violation> createViolations(Context ctx) {
        LinkedList<Violation> violations = new LinkedList<Violation>();
        violations.add(new DiskReadViolation(ctx));
        violations.add(new DiskWriteViolation(ctx));
        violations.add(new NetworkViolation(ctx));
        violations.add(new FakeSlowCallViolation(ctx));        
        violations.add(new ExplicitTerminationViolation(ctx));
        violations.add(new InstanceCountViolation(ctx));
        violations.add(new SharedPreferencesCommitViolation(ctx));
        violations.add(new DiskReadReceiverViolation(ctx));
        violations.add(new MultipleSlowCallsViolation(ctx));        
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
        violation.violate();
    }
}
