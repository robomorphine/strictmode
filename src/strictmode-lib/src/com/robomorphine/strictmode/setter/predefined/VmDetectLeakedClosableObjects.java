package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Detect when an Closeable or other object with a explict termination method is finalized 
 * without having been closed.
 * 
 *  See: StrictMode.VmPolicy.Builder.detectLeakedClosableObjects()
 * 
 * @author inazaruk
 */
public class VmDetectLeakedClosableObjects extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 11; //Build.VERSION_CODES.HONEYCOMB
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(Builder builder) {
        builder.detectLeakedClosableObjects();
        return builder.build();
    }
}
