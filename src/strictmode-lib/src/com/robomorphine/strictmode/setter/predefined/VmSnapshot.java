package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.util.Log;

/**
 * Makes snapshot of current vm policy and can restore this policy later on.
 *  
 * @author inazaruk
 */
public class VmSnapshot extends AbstractStrictModeSetter {
    
    private static final String TAG = VmSnapshot.class.getSimpleName();
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    private Object  mPolicy;
    
    @TargetApi(9)
    public VmSnapshot() {
        try {
            mPolicy = StrictMode.getVmPolicy();
        } catch (Throwable ex) {//NOPMD
            Log.e(TAG, "Unable to retrieve current vm policy.", ex);
        }        
    }
        
    @Override
    protected int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @TargetApi(9)
    @Override
    protected VmPolicy onUpdateVmPolicy(VmPolicy.Builder builder) {
        VmPolicy policy = (VmPolicy)mPolicy;
        return createVmBuilder(policy).build();
    }
    
    @Override
    protected boolean shouldRestoreVmPenaltyLog() {
        return !isPenaltyLogSet((VmPolicy)mPolicy);
    }
}
