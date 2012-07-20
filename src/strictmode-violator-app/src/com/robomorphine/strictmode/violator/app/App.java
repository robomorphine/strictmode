package com.robomorphine.strictmode.violator.app;

import com.robomorphine.strictmode.StrictModeHelper;
import com.robomorphine.strictmode.setter.Policy;
import com.robomorphine.strictmode.violator.BuildConfig;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

@SuppressFBWarnings(justification="Android pattern", value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
public class App extends Application {
        
    private static Context sContext;
    
    public static Context getContext() {
        return sContext;
    }
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = this;
    }
    
    @Override
    public void onCreate() {        
        if(BuildConfig.DEBUG) {
            StrictModeHelper.setStrictMode(Policy.All.Reset, 
                                           Policy.All.DetectAll,
                                           Policy.All.PenaltyDropBox,
                                           Policy.All.PenaltyLog);
        }
        super.onCreate();
        
        LoaderManager.enableDebugLogging(BuildConfig.DEBUG);
        FragmentManager.enableDebugLogging(BuildConfig.DEBUG);
    }

}
