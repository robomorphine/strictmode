package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;
import com.robomorphine.strictmode.fragment.PackageListFragment.PackageSelectionListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PackageListActivity extends FragmentActivity implements PackageSelectionListener {
    
    public static final String EXTRA_SELECTED_PACKAGE = "selectedPackage";
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_list_activity);
    }
    
    @Override
    public void onPackageSelected(String packageName) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_PACKAGE, packageName);
        setResult(RESULT_OK, intent);
        finish();
    }
}