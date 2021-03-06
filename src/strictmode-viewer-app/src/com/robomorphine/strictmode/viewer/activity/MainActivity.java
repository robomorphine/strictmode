package com.robomorphine.strictmode.viewer.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.robomorphine.fragment.AboutDialogFragment;
import com.robomorphine.fragment.HelpDialogFragment;
import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.fragment.ViolationListFilterFragment;
import com.robomorphine.strictmode.viewer.fragment.ViolationListFilterFragment.OnViolationFilterChangedListener;
import com.robomorphine.strictmode.viewer.fragment.ViolationListFragment;
import com.robomorphine.strictmode.viewer.fragment.ViolationListFragment.OnViolationClickListener;
import com.robomorphine.strictmode.viewer.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.viewer.violation.group.ViolationGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class MainActivity extends SherlockFragmentActivity
                          implements OnViolationFilterChangedListener, 
                                     OnViolationClickListener {
   
    private ViolationListFilterFragment mViolationListFilterFragment;
    private ViolationListFragment mViolationListFragment;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name_title);
        actionBar.setSubtitle(R.string.app_name_subtitle);
                
        FragmentManager fm = getSupportFragmentManager();
        
        mViolationListFilterFragment = 
                (ViolationListFilterFragment)fm.findFragmentById(R.id.violation_list_filter);
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                onAbout();
                return true;
            case R.id.help:
                onHelp();
                return true;
        }        
        return super.onOptionsItemSelected(item);
    }
    
    private void onAbout() {
        AboutDialogFragment fragment = new AboutDialogFragment();
        fragment.show(getSupportFragmentManager(), "about");
    }
    
    private void onHelp() {
        HelpDialogFragment fragment = HelpDialogFragment.createFragment(R.raw.help);
        fragment.show(getSupportFragmentManager(), "help");
    }
}