package com.robomorphine.prefs.domain;

import com.robomorphine.prefs.domain.DomainManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class PreferencesRemover {
    
    private Context mContext;
    public PreferencesRemover(Context context) {
        mContext = context.getApplicationContext();
    }
    
    public void clear() {
        DomainManager manager = new DomainManager(mContext, false);
        for(String name : manager.getPreferences()) {
            SharedPreferences prefs = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            prefs.edit().clear().commit();
        }
        
        File dir = new File(mContext.getFilesDir().getParentFile(), 
                            DomainManager.PREFERENCES_SUBDIR);
        File [] files = dir.listFiles();
        if (files != null) {
            for(File file : files) {
                file.delete();
            }
        }
        
        DomainManager.getPrefsMap().clear();
    }
}
