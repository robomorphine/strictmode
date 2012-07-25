package com.robomorphine.strictmode.setter;

import com.robomorphine.strictmode.setter.predefined.DetectAll;
import com.robomorphine.strictmode.setter.predefined.LaxAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyDeathAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyDropBoxAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyLogAll;
import com.robomorphine.strictmode.setter.predefined.ResetAll;
import com.robomorphine.strictmode.setter.predefined.SnapshotAll;
import com.robomorphine.strictmode.setter.predefined.ThreadDetectAll;
import com.robomorphine.strictmode.setter.predefined.ThreadDetectCustomSlowCalls;
import com.robomorphine.strictmode.setter.predefined.ThreadDetectDisk;
import com.robomorphine.strictmode.setter.predefined.ThreadDetectNetwork;
import com.robomorphine.strictmode.setter.predefined.ThreadLax;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyDeath;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyDeathOnNetwork;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyDialog;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyDropBox;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyFlashScreen;
import com.robomorphine.strictmode.setter.predefined.ThreadPenaltyLog;
import com.robomorphine.strictmode.setter.predefined.ThreadPermitAll;
import com.robomorphine.strictmode.setter.predefined.ThreadPermitCustomSlowCalls;
import com.robomorphine.strictmode.setter.predefined.ThreadPermitDisk;
import com.robomorphine.strictmode.setter.predefined.ThreadPermitNetwork;
import com.robomorphine.strictmode.setter.predefined.ThreadReset;
import com.robomorphine.strictmode.setter.predefined.ThreadSnapshot;
import com.robomorphine.strictmode.setter.predefined.VmDetectActivityLeaks;
import com.robomorphine.strictmode.setter.predefined.VmDetectAll;
import com.robomorphine.strictmode.setter.predefined.VmDetectClassInstanceLimit;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedClosableObjects;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedRegistrationObjects;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedSqlLiteObjects;
import com.robomorphine.strictmode.setter.predefined.VmLax;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyDeath;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyDropBox;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyLog;
import com.robomorphine.strictmode.setter.predefined.VmReset;
import com.robomorphine.strictmode.setter.predefined.VmSnapshot;

import junit.framework.TestCase;

public class SettersTest extends TestCase {
    
    /**
     * Basically make sure that nothing crashes.
     */
    public void testSetters() {
        StrictModeSetter setters [] = new StrictModeSetter[] {
            new DetectAll(),
            new LaxAll(),
            new PenaltyDeathAll(),
            new PenaltyDropBoxAll(),
            new PenaltyLogAll(),
            new ResetAll(),
            new SnapshotAll(),
            
            new ThreadReset(),
            new ThreadSnapshot(),
            new ThreadLax(),
            new ThreadDetectAll(),
            new ThreadPermitAll(),
            new ThreadDetectCustomSlowCalls(),
            new ThreadPermitCustomSlowCalls(),
            new ThreadDetectDisk(),
            new ThreadPermitDisk(),
            new ThreadDetectNetwork(),
            new ThreadPermitNetwork(),
            new ThreadPenaltyDeath(),
            new ThreadPenaltyDeathOnNetwork(),
            new ThreadPenaltyDialog(),
            new ThreadPenaltyDropBox(),
            new ThreadPenaltyFlashScreen(),
            new ThreadPenaltyLog(),
            
            new VmLax(),
            new VmReset(),
            new VmSnapshot(),
            new VmDetectActivityLeaks(),
            new VmDetectAll(),            
            new VmDetectClassInstanceLimit(SettersTest.class, 100),
            new VmDetectLeakedClosableObjects(),
            new VmDetectLeakedRegistrationObjects(),
            new VmDetectLeakedSqlLiteObjects(),
            new VmPenaltyDeath(),
            new VmPenaltyDropBox(),
            new VmPenaltyLog()
        };
        
        ResetAll resetAll = new ResetAll();
        for (StrictModeSetter setter : setters) {
            setter.set();
            resetAll.set();
        }
    }
}
