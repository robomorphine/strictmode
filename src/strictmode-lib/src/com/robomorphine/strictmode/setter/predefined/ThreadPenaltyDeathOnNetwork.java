package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Crash application if networking detection was on and it networking was detected.
 * 
 * See: StrictMode.ThreadPolicy.Builder.penaltyDeathOnNetwork()
 * 
 * @author inazaruk
 */
public class ThreadPenaltyDeathOnNetwork extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 11; //Build.VERSION_CODES.HONEYCOMB
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.penaltyDeathOnNetwork();
        return builder.build();
    }

}
