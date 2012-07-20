package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;

/**
 * Resets current vm policy. 
 * This is basically the policy that has nothing set or unset on it. The one that is provided 
 * by new VmPolicy.Builder().build();
 * 
 * @author inazaruk
 */
public class VmReset extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(VmPolicy.Builder builder) {
        return new VmPolicy.Builder().build();
    }

}
