package com.robomorphine.prefs;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class InternalPreferenceManager {    
    
    private final static String TAG = Tags.getTag(InternalPreferenceManager.class);
    
    private static final String CONTEXT_IMPL_CLASS_NAME = "android.app.ContextImpl";
    private static final String CONTEXT_IMPL_SHARED_PREFS_FIELD = "sSharedPrefs";
    private static final long DEFAULT_MEMORY_UPDATE_TIMEOUT_MS = 1000;
    
    protected static final String PREFERENCES_SUBDIR = "shared_prefs";
    protected static final String PREFERENCES_EXT = ".xml".toLowerCase();
    public static final long DEFAULT_DISK_UPDATE_TIMEOUT_MS = 10 * 1000;
    
    private static class DirFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String filename = pathname.getName().toLowerCase();
            return pathname.isFile() && filename.endsWith(PREFERENCES_EXT);
        }
    }    
    
    private final Runnable mMemoryUpdater = new Runnable() {
        @Override
        public void run() {
            refreshMemoryPrefernces();
            mHandler.postDelayed(this, mMemoryUpdateTimeout);
        }
    };
    
    private final Runnable mDiskUpdater = new Runnable() {
        @Override
        public void run() {
            refreshDiskPreferences();
            mHandler.postDelayed(this, mDiskUpdateTimeout);
        }
    };
        
    private final Map<String, ?> mContextImplSharedPrefs;
    private final Set<String> mMemoryPreferences = new HashSet<String>();
    private final Set<String> mDiskPreferences = new HashSet<String>();
    
    private final Handler mHandler;
    private boolean mTracking = false;
    private long mMemoryUpdateTimestamp = 0;    
    private long mMemoryUpdateTimeout = DEFAULT_MEMORY_UPDATE_TIMEOUT_MS;
    private long mDiskUpdateTimestamp = 0;    
    private long mDiskUpdateTimeout = DEFAULT_DISK_UPDATE_TIMEOUT_MS;
    
    private final FileFilter mSharedPrefFilter = new DirFilter(); 
    private final File mSharedPrefDir;
    
    public InternalPreferenceManager(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        mContextImplSharedPrefs = getPrefsMap();
        mSharedPrefDir = new File(context.getFilesDir().getParentFile(), PREFERENCES_SUBDIR);
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
    
    /*************************/
    /**     Timeouts        **/
    /*************************/
    
    public void setMemoryUpdateTimeout(long timeout, TimeUnit unit) {
        synchronized (this) {
            mMemoryUpdateTimeout = unit.toMillis(timeout);
            restartTracking();
        }
    }
    
    public long getMemoryUpdateTimeout() {
        synchronized (this) {
            return mMemoryUpdateTimeout;    
        }
    }
    
    public boolean isMemorySnapshotExpired() {
        synchronized (this) {
            return (mMemoryUpdateTimestamp + mMemoryUpdateTimeout) < System.currentTimeMillis();     
        }
    }
    
    public void setDiskUpdateTimeout(long timeout, TimeUnit unit) {
        synchronized (this) {
            mDiskUpdateTimeout = unit.toMillis(timeout);
            restartTracking();
        }
    }
    
    public long getDiskUpdateTimeout() {
        synchronized (this) {
            return mDiskUpdateTimeout;    
        }
    }
    
    public boolean isDiskSnapshotExpired() {
        synchronized (this) {
            return (mDiskUpdateTimestamp + mDiskUpdateTimeout) < System.currentTimeMillis();     
        }
    }
    
    /****************************************/
    /**   Get/Refresh memory preferences   **/
    /****************************************/
    
    public void refreshMemoryPrefernces() {
        synchronized (this) {
            synchronized (mContextImplSharedPrefs) {
                mMemoryPreferences.clear();
                mMemoryPreferences.addAll(mContextImplSharedPrefs.keySet());    
            }
            mMemoryUpdateTimestamp = System.currentTimeMillis();
        }
    }
    
    public Set<String> getMemoryPreferences() {
        synchronized (this) {
            refreshMemoryPrefernces();
            return mMemoryPreferences;
        }
    }
    
    /****************************************/
    /**    Get/Refresh disk preferences    **/
    /****************************************/
    
    public void refreshDiskPreferences() {        
        File [] prefFiles = mSharedPrefDir.listFiles(mSharedPrefFilter);
        synchronized (this) {            
            mDiskPreferences.clear();
            if(prefFiles != null) {
                for(File file : prefFiles) {
                    mDiskPreferences.add(file.getName());    
                }
            }
            mDiskUpdateTimestamp = System.currentTimeMillis();
        }
    }
    
    public Set<String> getDiskPreferences() {
        synchronized (this) {
            refreshDiskPreferences();
            return mDiskPreferences;
        }
    }
    
    public void refreshPreferences() {
        refreshMemoryPrefernces();
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

    private void restartTracking() {
        if(mTracking) {
            stopTracking();
            startTracking();
        }
    }
    
    public void startTracking() {
        synchronized (this) {
            if(!mTracking) {
                mMemoryUpdater.run();
                mDiskUpdater.run();
                mTracking = true;
            }
        }
    }
    
    public void stopTracking() {
        synchronized (this) {
            if(mTracking) {
                mHandler.removeCallbacks(mMemoryUpdater);
                mHandler.removeCallbacks(mDiskUpdater);
                mTracking = false;
            }
        }
    }
}
