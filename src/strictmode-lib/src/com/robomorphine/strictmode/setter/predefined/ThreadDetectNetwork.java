package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Enables detection of networking violation on current thread.
 * 
 * See: StrictMode.ThreadPolicy.Builder.detectNetwork()
 * 
 * @author inazaruk
 */
public class ThreadDetectNetwork extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD 
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.detectNetwork();
        return builder.build();
    }

}
