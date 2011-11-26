
package com.robomorphine.test.testrunner;

import com.robomorphine.test.annotation.DisabledTest;
import com.robomorphine.test.annotation.EnabledTest;
import com.robomorphine.test.annotation.LongTest;
import com.robomorphine.test.annotation.ManualTest;
import com.robomorphine.test.annotation.NonUiTest;
import com.robomorphine.test.annotation.PerformanceTest;
import com.robomorphine.test.annotation.ShortTest;
import com.robomorphine.test.annotation.StabilityTest;
import com.robomorphine.test.annotation.UiTest;

import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class TestFilterTestRunner extends TestCase {
    
    @android.test.suitebuilder.annotation.SmallTest
    public void testAsAndroidSmall() {

    }

    @SmallTest
    public void testAsSmall() {

    }

    @ShortTest
    public void testAsShort() {

    }

    @MediumTest
    public void testAsMedium() {

    }
    
    @android.test.suitebuilder.annotation.MediumTest
    public void testAsAndroidMedium() {

    }

    @LargeTest
    public void testAsLarge() {

    }
    
    @android.test.suitebuilder.annotation.LargeTest
    public void testAsAndroidLarge() {

    }
    
    @LongTest
    public void testAsLong() {
        
    }

    @ManualTest
    public void testAsManual() {

    }

    @PerformanceTest
    public void testAsPerfomance() {

    }
    
    @StabilityTest
    public void testAsStability() {

    }
    
    @UiTest
    public void testAsUi() {

    }
    
    @NonUiTest
    public void testAsNonUi() {

    }
    
    @UiTest
    @PerformanceTest
    public void testAsUiPerformance() {

    }
    
    @NonUiTest
    @PerformanceTest
    public void testAsNonUiPerformance() {

    }
    
    @EnabledTest
    public void testEnabled() {

    }
    
    @DisabledTest("test")
    public void testDisabled() {

    }
}
