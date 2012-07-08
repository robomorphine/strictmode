package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.ViolationListFilterFragment;
import com.robomorphine.strictmode.fragment.ViolationListFilterFragment.OnViolationFilterChangedListener;
import com.robomorphine.strictmode.fragment.ViolationListFragment;
import com.robomorphine.strictmode.fragment.ViolationListFragment.OnViolationClickListener;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.violation.group.ViolationGroup;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity 
                          implements OnViolationFilterChangedListener, 
                                     OnViolationClickListener {
   
    private ViolationListFilterFragment mViolationListFilterFragment;
    private ViolationListFragment mViolationListFragment;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(R.string.app_name_subtitle);
                
        FragmentManager fm = getSupportFragmentManager();
        
        mViolationListFilterFragment = (ViolationListFilterFragment)fm.findFragmentById(R.id.violation_list_filter);
        mViolationListFragment = (ViolationListFragment)fm.findFragmentById(R.id.violation_list);
    }
    
    @Override
    public void onViolationFilterChanged(ViolationFilter filter) {
        mViolationListFragment.setFilter(filter);
    }
    
    @Override
    public void onViolationClicked(ViolationGroup group) {
        Intent intent = new Intent(this, ViolationActivity.class);
        intent.putExtra(ViolationActivity.EXTRA_VIOLATION_GROUP, group);
        startActivity(intent);
    }
    
    @Override
    public void onViolationLongClicked(ViolationGroup group) {
        mViolationListFilterFragment.setSelectedPackage(group.getViolation().getPackage());
    }
}