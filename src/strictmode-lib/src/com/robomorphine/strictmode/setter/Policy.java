package com.robomorphine.strictmode.setter;

import com.robomorphine.strictmode.setter.predefined.DetectAll;
import com.robomorphine.strictmode.setter.predefined.LaxAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyDeathAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyDropBoxAll;
import com.robomorphine.strictmode.setter.predefined.PenaltyLogAll;
import com.robomorphine.strictmode.setter.predefined.ResetAll;
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

public class Policy {
    
    public static enum All implements StrictModeSetter {
        Lax(new LaxAll()),
        Reset(new ResetAll()),
        DetectAll(new DetectAll()),
        PenaltyDeath(new PenaltyDeathAll()),
        PenaltyDropBox(new PenaltyDropBoxAll()),
        PenaltyLog(new PenaltyLogAll());
        
        private final StrictModeSetter mSetter;
        private All(StrictModeSetter setter) {
            mSetter = setter;
        }
            
        @Override
        public void set() {
            mSetter.set();        
        }
    }
    
    public static enum Thread implements StrictModeSetter {
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
        PenaltyFlashScreen(new ThreadPenaltyFlashScreen());
        
        private final StrictModeSetter mSetter;
        private Thread(StrictModeSetter setter) {
            mSetter = setter;
        }
            
        @Override
        public void set() {
            mSetter.set();        
        }
    }
    
    public enum Vm implements StrictModeSetter {
        Lax(new VmLax()),
        Reset(new VmReset()),
        DetectAll(new VmDetectAll()),
        DetectActivityLeaks(new VmDetectActivityLeaks()),
        DetectLeakedClosableObjects(new VmDetectLeakedClosableObjects()),
        DetectLeakedRegistrationObjects(new VmDetectLeakedRegistrationObjects()),
        DetectLeakedSqlLiteObjects(new VmDetectLeakedSqlLiteObjects()),
        PenaltyDeath(new VmPenaltyDeath()),
        PenaltyDropBox(new VmPenaltyDropBox()),
        PenaltyLog(new VmPenaltyLog());
        
        private final StrictModeSetter mSetter;
        private Vm(StrictModeSetter setter) {
            mSetter = setter;
        }    
        
        @Override
        public void set() {
            mSetter.set();        
        }
    }

}
