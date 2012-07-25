package com.robomorphine.strictmode.setter.predefined;

import com.robomorphine.strictmode.setter.AbstractStrictModeSetter;

import android.annotation.TargetApi;
import android.os.StrictMode.VmPolicy;
import android.os.StrictMode.VmPolicy.Builder;

/**
 * Detect when a BroadcastReceiver or ServiceConnection is leaked during Context teardown.
 * 
 *  See: StrictMode.VmPolicy.Builder.detectLeakedRegistrationObjects()
 * 
 * @author inazaruk
 */
public class VmDetectLeakedRegistrationObjects extends AbstractStrictModeSetter {
    private static final int TARGET_VERSION = 16; //Build.VERSION_CODES.JELLY_BEAN
    
    @Override
    public int getMinimumApiLevel() {
        return TARGET_VERSION;
    }
    
    @Override
    @TargetApi(TARGET_VERSION)
    protected VmPolicy onUpdateVmPolicy(Builder builder) {
        builder.detectLeakedRegistrationObjects();
        return builder.build();
    }
}
