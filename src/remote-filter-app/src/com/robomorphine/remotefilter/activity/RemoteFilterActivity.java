package com.robomorphine.remotefilter.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.robomorphine.remotefilter.R;

public class RemoteFilterActivity extends FragmentActivity {

    public static final String EXTRA_PACKAGE = "package";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_filter_activity);
    }
}
