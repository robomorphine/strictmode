package com.robomorphine.strictmode.activity;

import com.robomorphine.strictmode.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ViolationActivity extends FragmentActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.violation_activity);
    }
}
