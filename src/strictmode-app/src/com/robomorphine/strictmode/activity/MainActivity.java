package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.adapter.ViolationFilterListAdapter;
import com.robomorphine.strictmode.adapter.ViolationFilterListAdapter.ViolationFilterInfo;
import com.robomorphine.strictmode.fragment.ViolationListFilterFragment.OnViolationFilterChangedListener;
import com.robomorphine.strictmode.fragment.ViolationListFragment;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity 
                          implements OnViolationFilterChangedListener, OnNavigationListener {
   
    private ViolationListFragment mViolationListFragment;
    private ViolationFilterListAdapter mViolationFilterListAdapter;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        List<ViolationFilterInfo> generalFilters = new LinkedList<ViolationFilterInfo>();
        generalFilters.add(new ViolationFilterInfo("Show All", "All violations are visible."));
        generalFilters.add(new ViolationFilterInfo("Show Last 24h", "Older vioaltions are not visible."));
        generalFilters.add(new ViolationFilterInfo("Show After Install", "Only violations that were reported after last application installation are visible."));
        
        mViolationFilterListAdapter = new ViolationFilterListAdapter(this, generalFilters);
                
        ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(R.string.app_name_subtitle);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(mViolationFilterListAdapter, this);
        
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