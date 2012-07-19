package com.robomorphine.strictmode.setter;

import com.robomorphine.strictmode.setter.predefined.VmDetectActivityLeaks;
import com.robomorphine.strictmode.setter.predefined.VmDetectAll;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedClosableObjects;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedRegistrationObjects;
import com.robomorphine.strictmode.setter.predefined.VmDetectLeakedSqlLiteObjects;
import com.robomorphine.strictmode.setter.predefined.VmLax;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyDeath;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyDropBox;
import com.robomorphine.strictmode.setter.predefined.VmPenaltyLog;
import com.robomorphine.strictmode.setter.predefined.VmReset;


public enum VmPolicy implements StrictModeSetter {
    Lax(new VmLax()),
    Rest(new VmReset()),
    DetectAll(new VmDetectAll()),
    DetectActivityLeaks(new VmDetectActivityLeaks()),
    DetectLeakedClosableObjects(new VmDetectLeakedClosableObjects()),
    DetectLeakedRegistrationObjects(new VmDetectLeakedRegistrationObjects()),
    DetectLeakedSqlLiteObjects(new VmDetectLeakedSqlLiteObjects()),
    PenaltyDeath(new VmPenaltyDeath()),
    PenaltyDropBox(new VmPenaltyDropBox()),
    PenaltyLog(new VmPenaltyLog());
    
    private final StrictModeSetter mSetter;
    private VmPolicy(StrictModeSetter setter) {
        mSetter = setter;
    }    
    
    @Override
    public void set() {
        mSetter.set();        
    }
}
