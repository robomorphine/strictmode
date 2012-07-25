package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Enables detection of custom slow calls.
 * See: StrictMode.ThreadPolicy.Builder.detectCustomSlowCalls(),
 *      StrictMode.noteSlowCall()
 * 
 * @author inazaruk
 */
public class ThreadDetectCustomSlowCalls extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 11; //Build.VERSION_CODES.HONEYCOMB
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.detectCustomSlowCalls();
        return builder.build();
    }

}
