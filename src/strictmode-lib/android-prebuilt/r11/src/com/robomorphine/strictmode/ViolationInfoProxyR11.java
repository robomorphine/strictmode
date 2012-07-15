package com.robomorphine.strictmode;

import android.os.StrictMode.ViolationInfo;

public class ViolationInfoProxyR11 implements DataProxy<ViolationInfo> {
    
    @Override
    public ViolationInfo handle(ViolationInfo violation) {
        String [] oldTags = violation.tags;
        int size = 0;
        if(oldTags != null) {
            size = oldTags.length;
        }
        
        violation.tags = new String[size + 1];
        violation.tags[0] = Long.toString(System.currentTimeMillis());
        
        if(oldTags != null) {
            for(int i = 0; i < oldTags.length; i++) {
                violation.tags[i+1] = oldTags[i];
            }
        }
        
        return violation;
    }
}
