package com.robomorphine.strictmode.violator.violation;

import com.robomorphine.strictmode.violator.R;

import android.content.Context;
import android.os.StrictMode;

public class FakeSlowCallViolation extends ThreadViolation {
    
    public FakeSlowCallViolation(Context context) {
        super(context, 
              R.drawable.clock,
              R.string.fake_slow_call_name, 
              R.string.fake_slow_call_descr);
    }
    
    @Override
    public void violate() {
        StrictMode.noteSlowCall(FakeSlowCallViolation.class.getName());
    }
}
