package com.robomorphine.prefs.remote;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class RemoteSharedPreferences implements SharedPreferences {
    
    @Override
    public boolean contains(String key) {
        return false;
    }
    
    @Override
    public Map<String, ?> getAll() {
        return null;
    }
    
    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }
    
    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }
    
    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }
    
    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }
    
    @Override
    public String getString(String key, String defValue) {
        return null;
    }
    
    @Override
    public Set<String> getStringSet(String arg0, Set<String> arg1) {
        return null;
    }

    
    @Override
    public Editor edit() {
        return null;
    }
    
    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        
    }
    
    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        
    }   
    
}
