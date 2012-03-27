package com.robomorphine.prefs;

import com.google.common.collect.Sets;
import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;
import com.robomorphine.prefs.SharedPrefsMap.SharedPrefsMapListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.FileObserver;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InternalPrefsManager implements SharedPrefsMapListener {    
    
    private final static String TAG = Tags.getTag(InternalPrefsManager.class);
    
    private static final String CONTEXT_IMPL_CLASS_NAME = "android.app.ContextImpl";
    private static final String CONTEXT_IMPL_SHARED_PREFS_FIELD = "sSharedPrefs";
    
    protected static final String PREFERENCES_SUBDIR = "shared_prefs";
    protected static final String PREFERENCES_EXT = ".xml".toLowerCase();
    
    private static final SharedPrefsMap sSharedPrefs = new SharedPrefsMap();
    
    private static class PreferenceFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String filename = pathname.getName().toLowerCase();
            return pathname.isFile() && filename.endsWith(PREFERENCES_EXT);
        }
    }
    
    private class PreferenceFileObserver extends FileObserver {
        public PreferenceFileObserver(String path) {
            super(path, FileObserver.CREATE);
        }
        
        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, "File " + path + " event: 0x" + Integer.toHexString(event));
            refreshPreferences();
        }
    }
        
    private final Map<String, ?> mContextImplSharedPrefs;
    private final boolean mIsSharedPrefsObservable;
    private final Set<String> mMemoryPreferences = new HashSet<String>();
    private final Set<String> mDiskPreferences = new HashSet<String>();
     
    private boolean mTracking = false;
    
    private final FileFilter mSharedPrefFilter = new PreferenceFileFilter(); 
    private final File mSharedPrefDir;
    private final FileObserver mFileObserver; 
    
    @SuppressWarnings("unchecked")
    protected static boolean replaceContextImplMap() {
        try {
            Class<?> clazz = Class.forName(CONTEXT_IMPL_CLASS_NAME);
            Field field = clazz.getDeclaredField(CONTEXT_IMPL_SHARED_PREFS_FIELD);
            field.setAccessible(true);
            
            synchronized (clazz) {
                HashMap<String, SharedPreferences> currentMap;
                currentMap = (HashMap<String, SharedPreferences>)field.get(null);
                synchronized (currentMap) {
                    if(currentMap != sSharedPrefs) {
                        sSharedPrefs.putAll(currentMap);
                        field.set(null, sSharedPrefs);
                    }
                }
                    
                if(sSharedPrefs == field.get(null)) {
                    return true;
                } else {
                    Log.e(TAG, "Successfuly set ContextImpl.sSharedPrefs, but it didn't have effect.");
                    return false;
                }    
            }
            
        } catch(Exception ex) {
            Log.e(TAG, "Failed to substitute ContextImpl.sSharedPrefs with observable map.", ex);
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected static Map<String, ?> getPrefsMap() {
        try {
            Class<?> clazz = Class.forName(CONTEXT_IMPL_CLASS_NAME);
            Field field = clazz.getDeclaredField(CONTEXT_IMPL_SHARED_PREFS_FIELD);
            field.setAccessible(true);
            
            return (Map<String, ?>)field.get(null);
        } catch(ClassNotFoundException ex) {
            Log.e(TAG, "Failed to find class " + CONTEXT_IMPL_CLASS_NAME, ex);            
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "Failed to find field " + CONTEXT_IMPL_SHARED_PREFS_FIELD, ex);
        } catch (SecurityException ex) {
            Log.e(TAG, ex);
        } catch (IllegalAccessException ex) {
            Log.e(TAG, ex);
        }        
        return new HashMap<String, SharedPreferences>();
    }
    
    public InternalPrefsManager(Context context) {
        mIsSharedPrefsObservable = replaceContextImplMap();
                
        mContextImplSharedPrefs = getPrefsMap();
        mSharedPrefDir = new File(context.getFilesDir().getParentFile(), PREFERENCES_SUBDIR);
        mFileObserver = new PreferenceFileObserver(mSharedPrefDir.getAbsolutePath());        
    }      
    
    @Override
    public void onNewSharedPrefs(String name) {
        Log.d(TAG, "New SharedPreferneces notification: " + name);
        mMemoryPreferences.add(name);
        mDiskPreferences.add(name);
    }
    
    /****************************************/
    /**   Get/Refresh memory preferences   **/
    /****************************************/
    
    public void refreshMemoryPreferences() {
        Log.d(TAG, "Refreshing memory preferences.");
        synchronized (this) {
            synchronized (mContextImplSharedPrefs) {
                Set<String> newPrefs = mContextImplSharedPrefs.keySet();
                
                Set<String> diff = Sets.difference(newPrefs, mMemoryPreferences);                
                if(diff.size() > 0) {
                    Log.d(TAG, "New in-mem preferences detected: " + diff);
                }
            
                mMemoryPreferences.clear();
                mMemoryPreferences.addAll(mContextImplSharedPrefs.keySet());    
            }
        }
    }
    
    public Set<String> getMemoryPreferences() {
        synchronized (this) {
            refreshMemoryPreferences();
            return mMemoryPreferences;
        }
    }
    
    /****************************************/
    /**    Get/Refresh disk preferences    **/
    /****************************************/
    
    public void refreshDiskPreferences() { 
        Log.d(TAG, "Refreshing disk preferences.");
        File [] prefFiles = mSharedPrefDir.listFiles(mSharedPrefFilter);
        synchronized (this) {
            Set<String> newPrefs = new HashSet<String>();
            if(prefFiles != null) {
                for(File file : prefFiles) {
                    String prefName = file.getName().replace(PREFERENCES_EXT, "");
                    newPrefs.add(prefName);
                }
            }
            
            Set<String> diff = Sets.difference(newPrefs, mMemoryPreferences);                
            if(diff.size() > 0) {
                Log.d(TAG, "New on-disk preferences detected: " + diff);
            }
            mDiskPreferences.clear();
            mDiskPreferences.addAll(newPrefs);
        }
    }
    
    public Set<String> getDiskPreferences() {
        synchronized (this) {
            refreshDiskPreferences();
            return mDiskPreferences;
        }
    }
    
    /****************************************/
    /**       Get/Refresh preferences      **/
    /****************************************/
        
    public void refreshPreferences() {
        refreshMemoryPreferences();
        refreshDiskPreferences();
    }
    
    public Set<String> getPreferences() {
        synchronized (this) {
            HashSet<String> set = new HashSet<String>();
            set.addAll(getMemoryPreferences());
            set.addAll(getDiskPreferences());
            return set;
        }
    }
    
    /*************************************/
    /**       Automatic tracking        **/
    /*************************************/    
    
    public void startTracking() {
        Log.d(TAG, "Starting tracking preferneces.");
        synchronized (this) {
            if(!mTracking) {
                if(mIsSharedPrefsObservable) {
                    sSharedPrefs.addListener(this);
                } else {
                    mFileObserver.startWatching();
                }
                
                refreshPreferences();
                mTracking = true;
            }
        }
    }
    
    public void stopTracking() {
        Log.d(TAG, "Stopped tracking preferneces.");
        synchronized (this) {
            if(mTracking) {
                if(mIsSharedPrefsObservable) {
                    sSharedPrefs.removeListener(this);
                } else {
                    mFileObserver.stopWatching();
                }
                mTracking = false;
            }
        }
    }
}
