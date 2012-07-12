package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

public class MultipleSlowCallsViolation extends ThreadViolation {
    
    public MultipleSlowCallsViolation(Context context) {
        super(context, 
              R.drawable.clock,
              R.string.multiple_slow_calls_name, 
              R.string.multiple_slow_calls_descr);
    }
    
    @TargetApi(11)
    @Override    
    public void violate() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for(int i = 0; i < 10; i++) {
                StrictMode.noteSlowCall(MultipleSlowCallsViolation.class.getName());          
            }
        } else {
            Toast.makeText(getContext(), "Not supported.", Toast.LENGTH_LONG).show();
        }
        
    }
}
