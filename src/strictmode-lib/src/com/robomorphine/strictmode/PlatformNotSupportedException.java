package com.robomorphine.strictmode;

import android.os.Build;


public class PlatformNotSupportedException extends Exception {
    
    private static final long serialVersionUID = 1L;

    private static final String moreInfo() {
        return String.format("[%d, %s]", Build.VERSION.SDK_INT, Build.MODEL);
    }
    
    public PlatformNotSupportedException(String reason) {
        super("Current Android platform is not supported: " + reason + " " + moreInfo());
    }
}