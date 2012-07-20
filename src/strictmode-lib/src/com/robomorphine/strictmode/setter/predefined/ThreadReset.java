package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.ThreadPolicy;

/**
 * Resets current thread policy. 
 * This is basically the policy that has nothing set or unset on it. The one that is provided 
 * by new ThreadPolicy.Builder().build();
 * 
 * @author inazaruk
 */
public class ThreadReset extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected ThreadPolicy onUpdateThreadPolicy(ThreadPolicy.Builder builder) {
        return new ThreadPolicy.Builder().build();
    }

}
