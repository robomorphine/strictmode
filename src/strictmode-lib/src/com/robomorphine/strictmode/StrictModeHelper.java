package com.robomorphine.strictmode;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.StrictModeActivityManagerProxy;
import android.util.Log;

import java.lang.reflect.Field;

public class StrictModeHelper {
    
    private static final String TAG = StrictModeHelper.class.getSimpleName();
    
    private static final String GLOBAL_DEFAULT_INSTANCE = "gDefault";
    
    private static final String SINGLETON_CLASSNAME = "android.util.Singleton";
    private static final String SINGLETON_INSTANCE = "mInstance";
    
     
    
    private static IActivityManager sOldActivityManager;
    
    private static IActivityManager replaceActivityManager(IActivityManager activityManager) {
        /* make sure original IActivityManager instance is initialized */
        ActivityManagerNative.getDefault();
        
        /* now replace it with proxy */
        try {
            Class<?> clazz = ActivityManagerNative.class;
            Field field = clazz.getDeclaredField(GLOBAL_DEFAULT_INSTANCE);
            field.setAccessible(true);
            
            Object singletonOrManager = field.get(null);
            clazz = singletonOrManager.getClass();
            
            IActivityManager oldManager = null;
            if(IActivityManager.class.isAssignableFrom(clazz)) {
                /* API < 3.0 */
                oldManager = (IActivityManager)field.get(null);
                field.set(null, activityManager);
            } else {
                /* API >= 3.0 */
                clazz = clazz.getSuperclass();
                if(!clazz.getName().equals(SINGLETON_CLASSNAME)) {
                    /* unexpected class! */
                    return null;
                }
                field = clazz.getDeclaredField(SINGLETON_INSTANCE);
                field.setAccessible(true);
                
                oldManager = (IActivityManager)field.get(singletonOrManager);
                field.set(singletonOrManager, activityManager);
            }            
            return oldManager;
        } catch(Exception ex) {
            Log.e(TAG, "Failed to replace activity manager.", ex);
            return null;
        }
    }
        
    public static boolean enableUniqueViolations(boolean enable) {
        synchronized (StrictModeHelper.class) {
            if(enable && sOldActivityManager == null) {
                StrictModeActivityManagerProxy proxy;
                proxy = new StrictModeActivityManagerProxy(ActivityManagerNative.getDefault());
                sOldActivityManager = replaceActivityManager(proxy);
                return sOldActivityManager != null;
            } else if(!enable && sOldActivityManager != null) {
                IActivityManager activityManager = replaceActivityManager(sOldActivityManager);
                sOldActivityManager = null;
                return activityManager != null;
            }
            return true;
        }        
    } 
}
