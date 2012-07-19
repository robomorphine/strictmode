package com.robomorphine.strictmode.setter;

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

public enum ThreadPolicy implements StrictModeSetter {
    Lax(new ThreadLax()),
    Reset(new ThreadReset()),
    PermitAll(new ThreadPermitAll()),
    DetectAll(new ThreadDetectAll()),
    PermitDisk(new ThreadPermitDisk()),
    DetectDisk(new ThreadDetectDisk()),
    PermitNetwork(new ThreadPermitNetwork()),
    DetectNetwork(new ThreadDetectNetwork()),
    PermitCustomSlowCalls(new ThreadPermitCustomSlowCalls()),
    DetectCustomSlowCalls(new ThreadDetectCustomSlowCalls()),
    PenaltyDeath(new ThreadPenaltyDeath()),
    PenaltyDeathOnNetwork(new ThreadPenaltyDeathOnNetwork()),
    PenaltyDialog(new ThreadPenaltyDialog()),
    PenaltyDropBox(new ThreadPenaltyDropBox()),
    PenaltyLog(new ThreadPenaltyLog()),
    PenaltyFalshScreen(new ThreadPenaltyFlashScreen());
    
    private final StrictModeSetter mSetter;
    private ThreadPolicy(StrictModeSetter setter) {
        mSetter = setter;
    }
        
    @Override
    public void set() {
        mSetter.set();        
    }
}
