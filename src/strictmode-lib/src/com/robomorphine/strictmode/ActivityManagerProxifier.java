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

public class ActivityManagerProxifier { //NOPMD
    
    private static final String TAG = ActivityManagerProxifier.class.getSimpleName();
    
    private static final String GLOBAL_DEFAULT_INSTANCE = "gDefault";
    
    private static final String SINGLETON_CLASSNAME = "android.util.Singleton";
    private static final String SINGLETON_INSTANCE = "mInstance"; 
    
    private static IActivityManager sOriginalActivityManager;
    
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
    
    private static IActivityManager createActivityManagerProxy(IActivityManager manager, 
    		IntentProxy intentProxy) throws PlatformNotSupportedException {
        
        switch(Build.VERSION.SDK_INT) {//NOPMD
            case 9:
            case 10:
                return new IActivityManagerProxyR09(manager, intentProxy);
            case 11:
                return new IActivityManagerProxyR11(manager, intentProxy);
            case 12:
                return new IActivityManagerProxyR12(manager, intentProxy);
            case 13:
                return new IActivityManagerProxyR13(manager, intentProxy);
            case 14: 
                return new IActivityManagerProxyR14(manager, intentProxy);
            case 15: 
                return new IActivityManagerProxyR15(manager, intentProxy);
            case 16: 
                return new IActivityManagerProxyR16(manager, intentProxy);
            default:
                throw new PlatformNotSupportedException("IActivityManagerProxy");
        }
    }
        
    public static void setIntentProxy(IntentProxy intentProxy)
            throws PlatformNotSupportedException {
    
        synchronized (ActivityManagerProxifier.class) {
            if (intentProxy != null) {
            	if (sOriginalActivityManager == null) {
            		sOriginalActivityManager = ActivityManagerNative.getDefault();
            	}
            	IActivityManager originalActivityManager = sOriginalActivityManager;
            	if (sOriginalActivityManager == null) {
            		throw new IllegalStateException("Original activity manager is null.");
            	}
            	
                IActivityManager proxy = 
                		createActivityManagerProxy(originalActivityManager, intentProxy);
                
                if (replaceActivityManager(proxy) == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
            } else if (sOriginalActivityManager != null) {
                IActivityManager activityManager = replaceActivityManager(sOriginalActivityManager);
                sOriginalActivityManager = null;
                if (activityManager == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
            }
        }        
    }    
}
