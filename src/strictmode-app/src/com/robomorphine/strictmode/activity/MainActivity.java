package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.ViolationListFilterFragment.FilterListener;
import com.robomorphine.strictmode.fragment.ViolationListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity implements FilterListener {
   
    private ViolationListFragment mViolationListFragment;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        FragmentManager fm = getSupportFragmentManager();
        mViolationListFragment = (ViolationListFragment)fm.findFragmentById(R.id.violation_list);
    }
    
    @Override
    public void onFilterChanged(String selectedPackage) {
        mViolationListFragment.setFilter(selectedPackage);
    }
}