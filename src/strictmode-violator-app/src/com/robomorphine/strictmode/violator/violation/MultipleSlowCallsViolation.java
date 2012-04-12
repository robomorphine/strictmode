package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.os.StrictMode;

public class MultipleSlowCallsViolation extends ThreadViolation {
    
    public MultipleSlowCallsViolation(Context context) {
        super(context, 
              R.drawable.clock,
              R.string.multiple_slow_calls_name, 
              R.string.multiple_slow_calls_descr);
    }
    
    @Override
    public void violate() {
        for(int i = 0; i < 10; i++) {
            StrictMode.noteSlowCall(MultipleSlowCallsViolation.class.getName());          
        }
    }
}
