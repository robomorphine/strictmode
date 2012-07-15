package com.robomorphine.strictmode.violator.violation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public abstract class Violation {
    
    private final Context mContext;
    private final Drawable mIcon;
    private final String mName;
    private final String mDescr;
    
    public Violation(Context context, Drawable icon, String name, String descr) {
        mContext = context.getApplicationContext();
        mIcon = icon;
        mName = name;
        mDescr = descr;
    }
    
    public Violation(Context context, int icon, int name, int descr) {
        this(context,
             context.getResources().getDrawable(icon),
             context.getString(name), 
             context.getString(descr));
    }
    
    public Drawable getIcon() {
        return mIcon;
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
    
    public int getMinimunPlatformVersion() {
        return Build.VERSION_CODES.GINGERBREAD;
    }
    
    public abstract void violate();

}
