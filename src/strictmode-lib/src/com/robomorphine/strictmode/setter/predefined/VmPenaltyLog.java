package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Log detected violations to the system log.
 * 
 *  See: StrictMode.VmPolicy.Builder.penaltyLog()
 * 
 * @author inazaruk
 */
public class VmPenaltyLog extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(Builder builder) {
        builder.penaltyLog();
        return builder.build();
    }
}
