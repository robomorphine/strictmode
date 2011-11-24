package com.robomorphine.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.robomorphine.demo.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onDemoLogsClicked(View v) {
        startActivity(new Intent(this, LogsActivity.class));
    }
}
