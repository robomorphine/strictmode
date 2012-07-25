package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 *  Crashes the whole process on violation. This penalty runs at the end of all enabled 
 *  penalties so yo you'll still get your logging or other violations before the process dies.
 * 
 * See: StrictMode.ThreadPolicy.Builder.penaltyDeath()
 * 
 * @author inazaruk
 */
public class ThreadPenaltyDeath extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.penaltyDeath();
        return builder.build();
    }

}
