package com.robomorphine.strictmode;

import java.lang.reflect.Field;

import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.app.IActivityManagerProxyR09;
import android.app.IActivityManagerProxyR11;
import android.app.IActivityManagerProxyR12;
import android.app.IActivityManagerProxyR13;
import android.app.IActivityManagerProxyR14;
import android.app.IActivityManagerProxyR15;
import android.app.IActivityManagerProxyR16;
import android.app.IActivityManagerProxyR17;
import android.app.IApplicationThread;
import android.app.IApplicationThreadProxyR17;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ActivityManagerProxifier { //NOPMD
    
    private static final String TAG = ActivityManagerProxifier.class.getSimpleName();
    
    private static final String GLOBAL_DEFAULT_INSTANCE = "gDefault";
    
    private static final String SINGLETON_CLASSNAME = "android.util.Singleton";
    private static final String SINGLETON_INSTANCE = "mInstance"; 
    
    private static IActivityManager sOriginalActivityManager;
    private static IApplicationThread sOriginalApplicationThread;
    
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
        } catch(Throwable ex) {
            Log.e(TAG, "Failed to replace activity manager.", ex);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static ActivityThread getActivityThread() {
    	try {
    		Class<ActivityThread> clazz = ActivityThread.class;
    		Field threadLocalField = clazz.getDeclaredField("sThreadLocal");
    		threadLocalField.setAccessible(true);
    		ThreadLocal<ActivityThread> threadLocal =
    				(ThreadLocal<ActivityThread>)threadLocalField.get(null);
    		return threadLocal.get();
    	} catch (Throwable ex) {
    		Log.e(TAG, "Failed to get activity thread.", ex);
    		return null;
    	}
    }
    
    private static IApplicationThread getApplicationThread() {
    	try { 
    		Class<ActivityThread> clazz = ActivityThread.class;
    		Field appThreadField = clazz.getDeclaredField("mAppThread");
    		appThreadField.setAccessible(true);
    		
    		ActivityThread activityThread = getActivityThread();
    		return (IApplicationThread)appThreadField.get(activityThread);
    	} catch (Throwable ex) {
    		Log.e(TAG, "Failed to get application thread.", ex);
    		return null;
    	}
    }
    
    private static IApplicationThread replaceApplicationThread(IApplicationThread newAppThread) {
    	try {
    		Class<ActivityThread> clazz = ActivityThread.class;
    		Field appThreadField = clazz.getDeclaredField("mAppThread");
    		ActivityThread activityThread = getActivityThread();
    		
    		appThreadField.setAccessible(true);
    		IApplicationThread oldAppThread = 
    				(IApplicationThread)appThreadField.get(activityThread);
    		appThreadField.set(activityThread, newAppThread);
    		return oldAppThread;
    	} catch (Throwable ex) {
    		Log.e(TAG, "Failed to replace application thread.", ex);
    		return null;
    	}
    }
    
    private static IActivityManager createActivityManagerProxy(IActivityManager manager, 
    		DataProxy<Intent> intentProxy,
    		DataProxy<IBinder> binderProxy,
    		boolean logMethodCalls) throws PlatformNotSupportedException {
            
        switch(Build.VERSION.SDK_INT) {//NOPMD
//            case 9:
//            case 10:
//                return new IActivityManagerProxyR09(manager, intentProxy, binderProxy);
//            case 11:
//                return new IActivityManagerProxyR11(manager, intentProxy, binderProxy);
//            case 12:
//                return new IActivityManagerProxyR12(manager, intentProxy, binderProxy);
//            case 13:
//                return new IActivityManagerProxyR13(manager, intentProxy, binderProxy);
//            case 14: 
//                return new IActivityManagerProxyR14(manager, intentProxy, binderProxy);
//            case 15: 
//                return new IActivityManagerProxyR15(manager, intentProxy, binderProxy);
//            case 16: 
//                return new IActivityManagerProxyR16(manager, intentProxy, binderProxy);
            case 17:
            	return new IActivityManagerProxyR17(manager, intentProxy, binderProxy, logMethodCalls);
            default:
                throw new PlatformNotSupportedException("IActivityManagerProxy");
        }
    }
    
    private static IApplicationThread createApplicationThread(IApplicationThread appThread, 
    		DataProxy<Intent> intentProxy,
    		DataProxy<IBinder> binderProxy,
    		boolean logMethodCalls) throws PlatformNotSupportedException {
            
        switch(Build.VERSION.SDK_INT) {//NOPMD
            case 17:
            	return new IApplicationThreadProxyR17(appThread, intentProxy, binderProxy, logMethodCalls);
            default:
                throw new PlatformNotSupportedException("IApplicationThreadProxy");
        }
    }
        
    public static void setProxy(DataProxy<Intent> intentProxy, 
    		DataProxy<IBinder> binderProxy,
    		boolean logMethodCalls)
            throws PlatformNotSupportedException {
    
        synchronized (ActivityManagerProxifier.class) {
            if (intentProxy != null && binderProxy != null) {
            	if (sOriginalActivityManager == null) {
            		sOriginalActivityManager = ActivityManagerNative.getDefault();
            	}
            	if (sOriginalApplicationThread == null) {
            		sOriginalApplicationThread = getApplicationThread();
            	}
            	
            	IActivityManager originalActivityManager = sOriginalActivityManager;
            	if (originalActivityManager == null) {
            		throw new IllegalStateException("Original activity manager is null.");
            	}
            	
            	IApplicationThread originalApplicationThread = sOriginalApplicationThread;
            	if (originalApplicationThread == null) {
            		throw new IllegalStateException("Original application thread is null");
            	}
            	
                IActivityManager activityManagerProxy = 
            		createActivityManagerProxy(originalActivityManager,	intentProxy, binderProxy, logMethodCalls);
                IApplicationThread applicationThreadProxy = 
                	createApplicationThread(sOriginalApplicationThread, intentProxy, binderProxy, logMethodCalls);
                
                if (replaceActivityManager(activityManagerProxy) == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
                
                if (replaceApplicationThread(applicationThreadProxy) == null) {
                    throw new PlatformNotSupportedException("replaceApplicationThread");
                }
                
            } else if (sOriginalActivityManager != null) {
                IActivityManager activityManager = replaceActivityManager(sOriginalActivityManager);
                sOriginalActivityManager = null;
                if (activityManager == null) {
                    throw new PlatformNotSupportedException("replaceActivityManager");
                }
                
                IApplicationThread applicationThread = 
                		replaceApplicationThread(sOriginalApplicationThread);
                sOriginalApplicationThread = null;
                if (applicationThread == null) {
                    throw new PlatformNotSupportedException("replaceApplicationThread");
                }
            }
        }        
    }    
}
