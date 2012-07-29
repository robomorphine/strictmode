package com.robomorphine.strictmode.fragment;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.ViolationStacktraceAdapter;
import com.robomorphine.strictmode.violation.ViolationException;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class ViolationStacktraceFragment extends ListFragment {
    
    private ViolationGroup mViolationGroup;
    private ViolationStacktraceAdapter mAdapter;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mViolationGroup = ViolationFragmentHelper.getViolationGroup(getArguments());        
        ViolationException exception = mViolationGroup.getViolation().getException();
        mAdapter = new ViolationStacktraceAdapter(getActivity(), exception);
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
        getListView().setFastScrollEnabled(true);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.violation_stacktrace_fragment, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logcat) {
            Log.e(getString(R.string.app_name),
                  getString(R.string.violation_stacktrace_logcat_message), 
                  mViolationGroup.getViolation().getException());
            
            int msgId = R.string.violation_stacktrace_printed_to_logcat;
            Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
       
}
