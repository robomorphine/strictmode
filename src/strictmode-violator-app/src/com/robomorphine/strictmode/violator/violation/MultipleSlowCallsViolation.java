package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class MultipleSlowCallsViolation extends ThreadViolation {
    
    public MultipleSlowCallsViolation(Context context) {
        super(context, 
              R.drawable.violation_type_clock,
              R.string.multiple_slow_calls_name, 
              R.string.multiple_slow_calls_descr);
    }
    
    @TargetApi(11)
    @Override    
    public void violate() {
        for(int i = 0; i < 10; i++) {
            StrictMode.noteSlowCall(MultipleSlowCallsViolation.class.getName());          
        }
    }    

    @Override
    public int getMinimunPlatformVersion() {
        return Build.VERSION_CODES.HONEYCOMB;
    }
}
