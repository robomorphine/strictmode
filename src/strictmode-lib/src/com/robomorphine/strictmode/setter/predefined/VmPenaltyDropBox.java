package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Enable detected violations log a stacktrace and timing data to the DropBox on policy violation. 
 * Intended mostly for platform integrators doing beta user field data collection.
 *  
 *  See: StrictMode.VmPolicy.Builder.penaltyDropBox()
 * 
 * @author inazaruk
 */
public class VmPenaltyDropBox extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(Builder builder) {
        builder.penaltyDropBox();
        return builder.build();
    }
}
