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
        StackTraceRandomizer stackTraceRandomizer = new StackTraceRandomizer();
        for(int i = 0; i < 10; i++) {
            stackTraceRandomizer.call(4, new Runnable() {
                @Override
                public void run() {
                    StrictMode.noteSlowCall(MultipleSlowCallsViolation.class.getName());
                }
            });            
        }
    }
}
