package com.robomorphine.strictmode.violator.violation;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class ThreadViolation extends Violation {
    
    public ThreadViolation(Context context, Drawable icon, String name, String descr) {
        super(context, icon, name, descr);
    }
   
    public ThreadViolation(Context context, int icon, int name, int descr) {
        super(context, icon, name, descr);
    }

}
