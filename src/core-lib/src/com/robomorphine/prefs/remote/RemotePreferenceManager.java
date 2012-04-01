package com.robomorphine.prefs.remote;

import android.content.Context;

import java.util.Observable;
import java.util.Set;

public class RemotePreferenceManager extends Observable {
    
    private final Context mContext;
    private final String mPackage;
    
    public RemotePreferenceManager(Context context, String packageName) {
        mContext = context;
        mPackage = packageName;
    }
    
    public void startTracking() {
        
    }
    
    public void stopTracking() {
        
    }
    
    public RemoteSharedPreferences getSharedPreferneces(String name) {
        return null;
    }
    
    public Set<String> getSharedPrefernceNames() {
        return null;
    }
}
