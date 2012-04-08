package com.robomorphine.strictmode.violator.violation;

import android.content.Context;

public abstract class ThreadViolation extends Violation {
    
    public ThreadViolation(Context context, String name, String descr) {
        super(context, name, descr);
    }
    
    public ThreadViolation(Context context, int name, int descr) {
        super(context, name, descr);
    }

}
