package com.robomorphine.strictmode;

import com.robomorphine.strictmode.setter.StrictModeSetter;
import com.robomorphine.strictmode.setter.ThreadPolicy;
import com.robomorphine.strictmode.setter.VmPolicy;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StrictModeHelper {
    
    private static Runnable sRestoreStrictMode = null;
    private static Handler sHandler = null;
    
    public static boolean enableUniqueViolations(boolean enable) {
        return ActivityManagerProxifier.enableUniqueViolations(enable);        
    }
    
    /**
     * Enables strict mode with default strict mode configuration.
     */
    public static void enableStrictMode() {
        setStrictMode(ThreadPolicy.DetectAll, 
                      ThreadPolicy.PenaltyDeathOnNetwork,
                      ThreadPolicy.PenaltyFalshScreen,
                      VmPolicy.DetectAll,
                      VmPolicy.PenaltyLog);
    }
    
    /**
     * Completely disables StrictMode checks.
     */
    public static void disableStrictMode() {
        setStrictMode(ThreadPolicy.Lax, VmPolicy.Lax);
    }
    
    public static void setStrictMode(StrictModeSetter...setters) {
         ArrayList<StrictModeSetter> list = new ArrayList<StrictModeSetter>(setters.length);
         for (StrictModeSetter setter : setters) {
             list.add(setter);
         }
         setStrictMode(list);
    }
    
    public static void setStrictMode(final List<StrictModeSetter> setters) {
        doSetStrictMode(setters);
        if (Build.VERSION.SDK_INT >= 16) { //Build.VERSION_CODES.JELLY_BEAN
            //workaround bug in JellyBean when called from Application's onCreate().
            synchronized (StrictModeHelper.class) {
                if (sHandler == null) {
                    sHandler = new Handler(Looper.getMainLooper());
                }
                
                if(sRestoreStrictMode != null) {
                    sHandler.removeCallbacks(sRestoreStrictMode);
                }
                sRestoreStrictMode = new Runnable() {
                    @Override
                    public void run() {
                        doSetStrictMode(setters);
                    }
                };
                sHandler.postAtFrontOfQueue(sRestoreStrictMode);
            }
        }
    }
    
    private static void doSetStrictMode(Collection<StrictModeSetter> setters) {
        for (StrictModeSetter setter : setters) {
            setter.set();
        }
    }
}
