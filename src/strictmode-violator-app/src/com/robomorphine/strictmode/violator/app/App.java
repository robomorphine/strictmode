package com.robomorphine.strictmode.violator.app;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;
import com.robomorphine.strictmode.StrictModeHelper;
import com.robomorphine.strictmode.violator.BuildConfig;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

@SuppressFBWarnings(justification="Android pattern", value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
public class App extends Application {
    
    private final static String TAG = Tags.getTag(App.class);
    
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
            enableStrictMode();
        }
        super.onCreate();
        
        LoaderManager.enableDebugLogging(BuildConfig.DEBUG);
        FragmentManager.enableDebugLogging(BuildConfig.DEBUG);
    }
    
    private static void enableStrictMode() {
        try {
            StrictModeHelper.enableUniqueViolations(true);
            doEnableStrictMode();
        } catch (Throwable ex) {
            Log.d(TAG, "Failed to enable StrictMode.", ex);
        }
    }

    @TargetApi(9)
    private static void doEnableStrictMode() {
        ThreadPolicy.Builder threadBuilder;
        VmPolicy.Builder vmBuilder;

        threadBuilder = new ThreadPolicy.Builder();
        vmBuilder = new VmPolicy.Builder();

        try {
            updateStrictModePolicies_v9(threadBuilder, vmBuilder);
        } catch (Exception ex) {
            Log.d(TAG, "Failed to update StrictMode policies for v9.");
        }

        try {
            updateStrictModePolicies_v11(threadBuilder, vmBuilder);
        } catch (Throwable ex) {
            Log.d(TAG, "Failed to update StrictMode policies for v11.");
        }

        StrictMode.setThreadPolicy(threadBuilder.build());
        StrictMode.setVmPolicy(vmBuilder.build());
    }

    @TargetApi(9)
    private static void updateStrictModePolicies_v9(ThreadPolicy.Builder threadBuilder,
            VmPolicy.Builder vmBuilder) {
        threadBuilder.detectAll().penaltyLog().penaltyDropBox();
        vmBuilder.detectAll().penaltyLog().penaltyDropBox();
    }

    @TargetApi(11)
    private static void updateStrictModePolicies_v11(ThreadPolicy.Builder threadBuilder,
            VmPolicy.Builder vmBuilder) {
        threadBuilder.penaltyFlashScreen();
        vmBuilder.detectLeakedClosableObjects().detectActivityLeaks();
    }

}
