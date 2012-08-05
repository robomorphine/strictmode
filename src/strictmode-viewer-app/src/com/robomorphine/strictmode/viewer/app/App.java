package com.robomorphine.strictmode.viewer.app;

import com.robomorphine.strictmode.StrictModeHelper;
import com.robomorphine.strictmode.viewer.BuildConfig;

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
    public void onCreate() {
        sContext = this;
        
        if(BuildConfig.DEBUG) {
            StrictModeHelper.enableStrictMode();
        }
        super.onCreate();
        
        LoaderManager.enableDebugLogging(BuildConfig.DEBUG);
        FragmentManager.enableDebugLogging(BuildConfig.DEBUG);
    }    
}
