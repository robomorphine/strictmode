package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;

/**
 * Makes snapshot of current thread policy and can restore this policy later on.
 *  
 * @author inazaruk
 */
public class ThreadSnapshot extends AbstractStrictModeSetter {
    
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    private ThreadPolicy mPolicy;
    public ThreadSnapshot(ThreadPolicy policy) {
        mPolicy = policy;        
    }
    
    public ThreadSnapshot() {
        this(StrictMode.getThreadPolicy());
    }
        
    @Override
    protected int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    protected ThreadPolicy onUpdateThreadPolicy(Builder builder) {
        return new StrictMode.ThreadPolicy.Builder(mPolicy).build();
    }
}
