package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.ViolationListFilterFragment.OnViolationFilterChangedListener;
import com.robomorphine.strictmode.fragment.ViolationListFragment;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity 
                          implements OnViolationFilterChangedListener, OnNavigationListener {
   
    private ViolationListFragment mViolationListFragment;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(R.string.app_name_subtitle);
                
        FragmentManager fm = getSupportFragmentManager();
        mViolationListFragment = (ViolationListFragment)fm.findFragmentById(R.id.violation_list);
    }
    
    @Override
    public void onViolationFilterChanged(ViolationFilter filter) {
        mViolationListFragment.setFilter(filter);
    }
    
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }
}