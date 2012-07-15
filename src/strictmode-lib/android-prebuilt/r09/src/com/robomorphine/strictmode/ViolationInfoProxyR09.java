package com.robomorphine.strictmode;

import android.os.StrictMode.ViolationInfo;

public class ViolationInfoProxyR09 implements DataProxy<ViolationInfo> {
    
    @Override
    public ViolationInfo handle(ViolationInfo violation) {
        
        String postfix = String.format("strictmode$ignore@%d", 
                                        System.currentTimeMillis());
        violation.crashInfo.stackTrace += postfix;
        return violation;
    }
}
