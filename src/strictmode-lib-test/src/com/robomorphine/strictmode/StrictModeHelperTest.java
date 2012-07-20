package com.robomorphine.strictmode;

import junit.framework.TestCase;

public class StrictModeHelperTest extends TestCase {
    
    public void testEnableDisableUniqueViolations() {
        assertTrue(StrictModeHelper.enableUniqueViolations(true));        
        assertTrue(StrictModeHelper.enableUniqueViolations(false));
    }
    
}
