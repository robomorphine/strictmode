package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Allows network violations on current thread.
 * 
 * See: StrictMode.ThreadPolicy.Builder.permitNetwork()
 * 
 * @author inazaruk
 */
public class ThreadPermitNetwork extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD 
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        builder.permitNetwork();
        return builder.build();
    }

}
