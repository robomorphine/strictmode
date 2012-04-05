package com.robomorphine.remoteprefs.activity;

import com.robomorphine.remoteprefs.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PackageListActivity extends FragmentActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_list_activity);        
    }
}