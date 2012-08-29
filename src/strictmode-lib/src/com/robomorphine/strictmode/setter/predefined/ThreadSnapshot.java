package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;

/**
 * Makes snapshot of current thread policy and can restore this policy later on.
 *  
 * @author inazaruk
 */
public class ThreadSnapshot extends AbstractStrictModeSetter {
    
    private static final String TAG = ThreadSnapshot.class.getSimpleName();
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
        
    private Object mPolicy;
    
    @TargetApi(9)
    public ThreadSnapshot() {
        try {
            mPolicy = StrictMode.getThreadPolicy();
        } catch(Throwable ex) {//NOPMD
            Log.e(TAG, "Unable to retrieve current thread policy.", ex);
        }
    }
        
    @Override
    protected int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @TargetApi(TARGET_VERSION)
    @Override    
    protected ThreadPolicy onUpdateThreadPolicy(Builder unused) {
        ThreadPolicy policy = (ThreadPolicy)mPolicy;
        ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder(policy);
        return builder.build();
    }
    
    @Override
    protected boolean shouldRestoreThreadPenaltyLog() {
        return !isPenaltyLogSet((ThreadPolicy)mPolicy);
    }
}
