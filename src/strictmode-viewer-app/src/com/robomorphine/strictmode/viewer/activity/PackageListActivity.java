package com.robomorphine.strictmode.viewer.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.robomorphine.strictmode.viewer.R;
import com.robomorphine.strictmode.viewer.fragment.PackageListFragment.PackageSelectionListener;

import android.content.Intent;
import android.os.Bundle;

public class PackageListActivity extends SherlockFragmentActivity 
                                 implements PackageSelectionListener {
    
    public static final String EXTRA_SELECTED_PACKAGE = "selectedPackage";
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public void onPackageSelected(String packageName) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_PACKAGE, packageName);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}