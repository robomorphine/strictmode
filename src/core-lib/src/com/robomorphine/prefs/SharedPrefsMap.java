package com.robomorphine.prefs;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * This implementation is designed to trigger notifications when new
 * keys are put into the map. It won't catch all possible HashMap usage scenarios,
 * only those that are used by ContextImpl to store SharedPreferneces. 
 * 
 * In short, this class implementation may be considered as tightly coupled with 
 * ContextImpl implementation. 
 */
class SharedPrefsMap extends HashMap<String, SharedPreferences> {
    
    private static final long serialVersionUID = 1L;
        
    public interface SharedPrefsMapListener {
        void onNewSharedPrefs(String name);
    }
    
    WeakHashMap<SharedPrefsMapListener, Void> mListeners;
    
    public SharedPrefsMap() {
        mListeners = new WeakHashMap<SharedPrefsMapListener, Void>();
    }
    
    @Override
    public SharedPreferences put(String key, SharedPreferences value) {
        SharedPreferences prefs = super.put(key, value);
        notifyListeners(key);
        return prefs;
    }
    
    public void addListener(SharedPrefsMapListener listener) {
        synchronized (this) {
            mListeners.put(listener, null);
        }
    }
    
    public void removeListener(SharedPrefsMapListener listener) {
        synchronized (this) {
            mListeners.remove(listener);
        }
    }

    private void notifyListeners(String name) {
        synchronized (this) {
            for(SharedPrefsMapListener listener : mListeners.keySet()) {
                listener.onNewSharedPrefs(name);
            }    
        }       
    }
}
