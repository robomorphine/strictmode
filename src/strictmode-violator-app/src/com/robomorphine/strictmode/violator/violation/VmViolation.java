package com.robomorphine.strictmode.violator.violation;

import android.content.Context;

public abstract class VmViolation extends Violation {
    
    public VmViolation(Context context, String name, String descr) {
        super(context, name, descr);
    }
    
    public VmViolation(Context context, int name, int descr) {
        super(context, name, descr);
    }
}
