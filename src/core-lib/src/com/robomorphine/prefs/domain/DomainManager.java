package com.robomorphine.prefs.domain;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;
import com.robomorphine.prefs.domain.DomainMap.DomainMapObserver;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implements tracking for new or changed SharedPreferences located in memory 
 * or on disk. It tries to detect when new shared preferences were 
 * added or changed as soon as possible. 
 * 
 * Does not detect when shared preferences are deleted as this 
 * is currently not supported operation for Context interface.
 * 
 */
public class DomainManager {    
    
    private final static String TAG = Tags.getTag(DomainManager.class);
    
    private static final String CONTEXT_IMPL_CLASS_NAME = "android.app.ContextImpl";
    private static final String CONTEXT_IMPL_SHARED_PREFS_FIELD = "sSharedPrefs";
    
    protected static final String PREFERENCES_SUBDIR = "shared_prefs";
    protected static final String PREFERENCES_EXT = ".xml".toLowerCase();
    
    private static final String MEMORY_SOURCE_NAME = "in-mem";
    private static final String DISK_SOURCE_NAME = "on-disk";
    
    private static final DomainMap sSharedPrefs = new DomainMap();
    private static final Map<String, Object> sDefaultMap = new HashMap<String, Object>();
    private static final Object sEmptyValue = new Object();  
    
    private static class PreferenceFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String filename = pathname.getName().toLowerCase();
            return pathname.isFile() && filename.endsWith(PREFERENCES_EXT);
        }
    }
    
    private class PreferenceDirectoryObserver extends FileObserver {
        public PreferenceDirectoryObserver(String path) {
            super(path, FileObserver.CREATE);
        }
        
        @Override
        public void onEvent(int event, String filename) {
            if(event == FileObserver.CREATE && filename != null) {
                Log.d(TAG, "Preference file " + filename + " was created.");
                                
                if(filename.toLowerCase().endsWith(PREFERENCES_EXT.toLowerCase())) { //NOPMD
                    int filenameLen = filename.length();
                    int extLen = PREFERENCES_EXT.length();
                    String name = filename.substring(0, filenameLen - extLen);
                    onOnDiskDomainAdded(name);
                }
            }
        }
            
    }
        
    private final Map<String, ?> mContextImplSharedPrefs;
    private final boolean mIsSharedPrefsObservable;
    private final MapObserver mSharedPrefsObserver = new MapObserver();
        
    private final FileFilter mSharedPrefFilter = new PreferenceFileFilter(); 
    private final File mSharedPrefDir;    
    private final FileObserver mFileObserver;
    private final ConcurrentMap<String, Object> mDomains = new ConcurrentHashMap<String, Object>();
            
    private DomainObserver mObserver;
    private boolean mTracking = false;
    
    /**
     * Replaces static instance of Map<String, SharedPreferencesImpl> map in ContextImpl class.
     * New instance is DomainMap which allows observing the changes in the map, thus providing
     * immediate notifications when new instances of shared preferences are added.
     * 
     * @return true if succeeded with replaces, false if failed.
     */
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
                    if(currentMap != sSharedPrefs) { //NOPMD
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
    
    private class MapObserver implements DomainMapObserver  {
        @Override
        public void onDomainAdded(String name) {
            onInMemoryDomainAdded(name);            
        }
    }
    
    /**
     * @return true if current shared preferences map in ContextImpl is replaced by observable 
     *         DomainMap instance. Returns false if no changes were made to runtime.
     */
    public static boolean isContextImplPatched() {
        return sSharedPrefs == getPrefsMap();
    }
    
    
    /**
     * Returns current instance of shared preference map in ContextImpl. This may be the
     * original Map or the observable DomainMap instance.
     * 
     * If there was a error in retrieving the instance this method will return an empty default map.
     */
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
        return sDefaultMap;
    }
    
    public DomainManager(Context context, boolean replaceSharedPrefsMap) {
        if(replaceSharedPrefsMap) {
            mIsSharedPrefsObservable = replaceContextImplMap();
        } else {
            mIsSharedPrefsObservable = false;
        }
                
        mContextImplSharedPrefs = getPrefsMap();
        mSharedPrefDir = new File(context.getFilesDir().getParentFile(), PREFERENCES_SUBDIR);
        mFileObserver = new PreferenceDirectoryObserver(mSharedPrefDir.getAbsolutePath());        
    }    
        
    private void onInMemoryDomainAdded(String name) {
        onDomainAdded(MEMORY_SOURCE_NAME, name);       
    }
    
    private void onOnDiskDomainAdded(String name) {
        if(!onDomainAdded(DISK_SOURCE_NAME, name)) {
            notifyDomainChanged(name);
        }
    }
    
    /****************************************/
    /**             Events                 **/
    /****************************************/
    
    /**
     * Single point for registering potentially new domain. If domain already existed
     * function return false, if domain is new function returns true.
     */
    private boolean onDomainAdded(String sourceName, String name) {
        synchronized (this) {
            if(!mDomains.containsKey(name)) {
                Log.d(TAG, "New %s domain added: \"%s\"", sourceName, name);
                mDomains.put(name, sEmptyValue);
                notifyDomainAdded(name);
                return true;
            }
            return false;
        }
    }
    
    /**
     * This function is called when list of available domains is refreshed either via
     * in-memory change notification or on-disk change notification.
     * @param names
     */
    private void onDomainSetUpdated(String sourceName, Set<String> names) {
        synchronized (this) {
            for(String name : names) {
                onDomainAdded(sourceName, name);
            }
        }
    }
    
   
    /****************************************/
    /**             Observer               **/
    /****************************************/
    
    public void setObserver(DomainObserver observer) {
        synchronized (this) {
            mObserver = observer;
        }
    }
    
    private void notifyDomainChanged(String name) {
        Log.d(TAG, "Domain \"%s\" is changed.", name);
        synchronized (this) {
            if(mObserver != null) {
                mObserver.onDomainChanged(name);
            }
        }   
    }
    
    private void notifyDomainAdded(String name) {
        Log.d(TAG, "Domain \"%s\" is added.", name);
        synchronized (this) {
            if(mObserver != null) {
                mObserver.onDomainAdded(name);
            }
        }
    }    
    
    /****************************************/
    /**     Get/Refresh preferences        **/
    /****************************************/
    
    public void refreshMemoryPreferences() {
        Log.d(TAG, "Refreshing memory preferences.");
        synchronized (mContextImplSharedPrefs) {
            onDomainSetUpdated(MEMORY_SOURCE_NAME, mContextImplSharedPrefs.keySet());                 
        }
    }
    
    public void refreshDiskPreferences() { 
        Log.d(TAG, "Refreshing disk preferences.");
        File [] prefFiles = mSharedPrefDir.listFiles(mSharedPrefFilter);
        
        Set<String> domainFiles = new HashSet<String>();
        if(prefFiles != null) {
            for(File file : prefFiles) {
                String prefName = file.getName().replace(PREFERENCES_EXT, "");
                domainFiles.add(prefName);
            }
        }
        onDomainSetUpdated(DISK_SOURCE_NAME, domainFiles);
    }
     
    public void refreshPreferences() {
        refreshMemoryPreferences();
        refreshDiskPreferences();
    }
    
    public Set<String> getPreferences() {
        return mDomains.keySet();
    }
    
    /*************************************/
    /**       Automatic tracking        **/
    /*************************************/    
    
    public void startTracking() {
        Log.d(TAG, "Starting tracking preferneces.");
        synchronized (this) {
            if(!mTracking) {
                if(mIsSharedPrefsObservable) {
                    sSharedPrefs.addObserver(mSharedPrefsObserver);
                }
                mFileObserver.startWatching();
                mTracking = true;
                refreshPreferences();
            }
        }
    }
    
    public void stopTracking() {
        Log.d(TAG, "Stopped tracking preferneces.");
        synchronized (this) {
            if(mTracking) {
                if(mIsSharedPrefsObservable) {
                    sSharedPrefs.removeObserver(mSharedPrefsObserver);
                }
                mFileObserver.stopWatching();
                mTracking = false;
            }
        }
    }
}
