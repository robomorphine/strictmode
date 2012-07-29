package com.robomorphine.strictmode;

import android.os.Build;

import junit.framework.TestCase;

public class StrictModeHelperTest extends TestCase {
    
    public void testEnableDisableUniqueViolations() {
        
        try {
            StrictModeHelper.enableUniqueViolations(true);        
            StrictModeHelper.enableUniqueViolations(false);
            
            assertTrue(Build.VERSION.SDK_INT > 8);//should not reach this line on 8th or earlier
        } catch (PlatformNotSupportedException ex) {
            assertTrue(Build.VERSION.SDK_INT <= 8);
        }
    }
    
}
