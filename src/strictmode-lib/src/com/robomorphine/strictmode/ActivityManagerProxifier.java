package com.robomorphine.strictmode;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IActivityManagerProxyR09;
import android.app.IActivityManagerProxyR11;
import android.app.IActivityManagerProxyR12;
import android.app.IActivityManagerProxyR13;
import android.app.IActivityManagerProxyR14;
import android.app.IActivityManagerProxyR15;
import android.app.IActivityManagerProxyR16;
import android.os.Build;
import android.os.StrictMode.ViolationInfo;
import android.util.Log;

import java.lang.reflect.Field;

class ActivityManagerProxifier { //NOPMD
    
    private static final String TAG = ActivityManagerProxifier.class.getSimpleName();
    
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
    
    private static DataProxy<ViolationInfo> createDataProxy() throws PlatformNotSupportedException {
        DataProxy<ViolationInfo> dataProxy = null;        
        switch(Build.VERSION.SDK_INT) {
            case 9:
            case 10:
                dataProxy = new ViolationInfoProxyR09();
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                dataProxy = new ViolationInfoProxyR11();
                break;
            default:
                throw new PlatformNotSupportedException("ViolationInfoProxy");
        }
        return dataProxy;
    }
    
    private static IActivityManager createActivityManagerProxy(IActivityManager manager)
            throws PlatformNotSupportedException {
        
        DataProxy<ViolationInfo> dataProxy = createDataProxy();        
        switch(Build.VERSION.SDK_INT) {//NOPMD
            case 9:
            case 10:
                return new IActivityManagerProxyR09(manager, dataProxy);
            case 11:
                return new IActivityManagerProxyR11(manager, dataProxy);
            case 12:
                return new IActivityManagerProxyR12(manager, dataProxy);
            case 13:
                return new IActivityManagerProxyR13(manager, dataProxy);
            case 14: 
                return new IActivityManagerProxyR14(manager, dataProxy);
            case 15: 
                return new IActivityManagerProxyR15(manager, dataProxy);
            case 16: 
                return new IActivityManagerProxyR16(manager, dataProxy);
            default:
                throw new PlatformNotSupportedException("IActivityManagerProxy");
        }
    }
        
    public static void enableUniqueViolations(boolean enable)
            throws PlatformNotSupportedException {
    
        synchronized (ActivityManagerProxifier.class) {
            if (enable && sOldActivityManager == null) {
                IActivityManager originalActivityManager = ActivityManagerNative.getDefault();
                IActivityManager proxy = createActivityManagerProxy(originalActivityManager);
                sOldActivityManager = replaceActivityManager(proxy);
                if (sOldActivityManager == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
            } else if (!enable && sOldActivityManager != null) {
                IActivityManager activityManager = replaceActivityManager(sOldActivityManager);
                sOldActivityManager = null;
                if (activityManager == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
            }
        }        
    }    
}
