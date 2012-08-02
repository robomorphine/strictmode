package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class FakeSlowCallViolation extends ThreadViolation {
    
    public FakeSlowCallViolation(Context context) {
        super(context, 
              R.drawable.violation_type_clock,
              R.string.fake_slow_call_name, 
              R.string.fake_slow_call_descr);
    }
    
    @TargetApi(11)
    @Override    
    public void violate() {
        StrictMode.noteSlowCall(FakeSlowCallViolation.class.getName());
    }
    
    @Override
    public int getMinimunPlatformVersion() {
        return Build.VERSION_CODES.HONEYCOMB;
    }
    
}
