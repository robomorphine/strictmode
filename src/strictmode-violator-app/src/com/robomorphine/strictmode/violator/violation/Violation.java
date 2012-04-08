package com.robomorphine.strictmode.violator.violation;

import android.content.Context;

public abstract class Violation {
    
    private final Context mContext;
    private final String mName;
    private final String mDescr;
    
    public Violation(Context context, String name, String descr) {
        mContext = context.getApplicationContext();
        mName = name;
        mDescr = descr;
    }
    
    public Violation(Context context, int name, int descr) {
        this(context, context.getString(name), context.getString(descr));
    }
    
    public String getName() {
        return mName;
    }
    
    public String getDescription() {
        return mDescr;
    }
    
    public Context getContext() {
        return mContext;
    }
    
    public abstract void violate();

}
