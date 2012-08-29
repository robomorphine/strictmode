package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Log detected violations to the system log.
 * 
 * See: StrictMode.ThreadPolicy.Builder.penaltyLog()
 * 
 * @author inazaruk
 */
public class ThreadPenaltyLog extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.penaltyLog();
        return builder.build();
    }
    
    @Override
    protected boolean shouldRestoreThreadPenaltyLog() {
        return false;
    }

}
