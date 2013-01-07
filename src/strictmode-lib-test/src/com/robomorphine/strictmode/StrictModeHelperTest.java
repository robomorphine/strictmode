package com.robomorphine.strictmode;

import junit.framework.TestCase;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class StrictModeHelperTest extends TestCase {
    
	private DataProxy<Intent> intentProxy = new DataProxy<Intent>() {
		@Override
		public Intent handle(Intent in) {
			return in;
		}
	};
	
	private DataProxy<IBinder> binderProxy = new DataProxy<IBinder>() {
		@Override
		public IBinder handle(IBinder in) {
			return in;
		}
	};
	
    public void testEnableDisableUniqueViolations() {
        
        try {
            ActivityManagerProxifier.setProxy(intentProxy, binderProxy, true);
            assertTrue(Build.VERSION.SDK_INT > 8);//should not reach this line on 8th or earlier
        } catch (PlatformNotSupportedException ex) {
            assertTrue(Build.VERSION.SDK_INT <= 8);
        }
    }
    
}
