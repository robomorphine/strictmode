package com.robomorphine.remoteprefs.app;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;
import com.robomorphine.remoteprefs.BuildConfig;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

public class App extends Application {
    private static final String TAG = Tags.getTag(App.class);
    
    private static Context sContext;
    
    public static Context getContext() {
        return sContext;
    }
    
    @Override
    public void onCreate() {
        sContext = this;
        
        enableStrictMode();
        super.onCreate();
        
        LoaderManager.enableDebugLogging(BuildConfig.DEBUG);
        FragmentManager.enableDebugLogging(BuildConfig.DEBUG);
    }
    
    @TargetApi(9)
    private void enableStrictMode() {
        try {
            if (BuildConfig.DEBUG) {                
                ThreadPolicy.Builder threadBuilder = new StrictMode.ThreadPolicy.Builder();
                threadBuilder.detectAll()
                             .penaltyLog()
                             .penaltyDropBox()
                             .penaltyDeathOnNetwork();
                
                penaltyFlashScreen(threadBuilder);
                StrictMode.setThreadPolicy(threadBuilder.build());
                
                VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder();
                vmBuilder.detectLeakedClosableObjects()
                         .detectLeakedSqlLiteObjects()
                         .penaltyDropBox()
                         .penaltyDeath();
                
                detectLeakingActivities(vmBuilder);
                StrictMode.setVmPolicy(vmBuilder.build());
            }
        } catch(VerifyError ex) {
            //ignore
        }
    }
    
    @TargetApi(11)
    private ThreadPolicy.Builder penaltyFlashScreen(ThreadPolicy.Builder builder) {
        try {
            return builder.penaltyFlashScreen();
        } catch(VerifyError ex) {
            return builder;
        }
    }
    
    @TargetApi(9)
    private VmPolicy.Builder detectLeakingActivities(VmPolicy.Builder builder) {
        ContentResolver cr = getContext().getContentResolver();
        int forceFinish = 0;
        
        try {
            forceFinish = Settings.System.getInt(cr, Settings.System.ALWAYS_FINISH_ACTIVITIES);
        } catch(SettingNotFoundException ex) {
            Log.e(TAG, ex);
        }
        
        if(forceFinish != 0) {
            builder.detectActivityLeaks();
        }
        
        return builder;
    }
}
