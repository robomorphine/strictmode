package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Enable detected violations log a stacktrace and timing data to the DropBox on policy violation. 
 * Intended mostly for platform integrators doing beta user field data collection.
 * 
 * See: StrictMode.ThreadPolicy.Builder.penaltyDropBox()
 * 
 * @author inazaruk
 */
public class ThreadPenaltyDropBox extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.penaltyDropBox();
        return builder.build();
    }

}
