package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Flash the screen during a violation.
 * 
 * See: StrictMode.ThreadPolicy.Builder.penaltyFlashScreen ()
 * 
 * @author inazaruk
 */
public class ThreadPenaltyFlashScreen extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 11; //Build.VERSION_CODES.HONEYCOMB
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.penaltyFlashScreen();
        return builder.build();
    }

}
