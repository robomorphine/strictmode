package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

public class FakeSlowCallViolation extends ThreadViolation {
    
    public FakeSlowCallViolation(Context context) {
        super(context, 
              R.drawable.clock,
              R.string.fake_slow_call_name, 
              R.string.fake_slow_call_descr);
    }
    
    @TargetApi(11)
    @Override    
    public void violate() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.noteSlowCall(FakeSlowCallViolation.class.getName());
        } else {
            Toast.makeText(getContext(), "Not supported.", Toast.LENGTH_LONG).show();
        }
    }
}
