package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;

/**
 * Makes snapshot of current vm policy and can restore this policy later on.
 *  
 * @author inazaruk
 */
public class VmSnapshot extends AbstractStrictModeSetter {
    
    private static final int TARGET_VERSION = 9; //Build.VERSION_CODES.GINGERBREAD
    
    private VmPolicy mPolicy;
    public VmSnapshot(VmPolicy policy) {
        mPolicy = policy;        
    }
    
    public VmSnapshot() {
        this(StrictMode.getVmPolicy());
    }
        
    @Override
    protected int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    protected VmPolicy onUpdateVmPolicy(VmPolicy.Builder builder) {
        return createVmBuilder(mPolicy).build();
    }
}
