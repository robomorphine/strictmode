package com.robomorphine.test;

import android.os.Handler;
import android.os.Looper;

public class MainThread {
    
    static public Handler newHandler() {
        return new Handler(Looper.getMainLooper());
    }    
    
    /**
     * Waits for all events posted before call to this function
     * to be processed before returning. 
     */
    static public void sync() {
        HandlerSync handler = new HandlerSync(Looper.getMainLooper());        
        handler.waitForSync();
    }    

    
    /**
     * Waits for message queue to become idle
     */
    static public void waitForIdleSync() {
        
        //1. check we are not running on the main thread!
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Can't wait for main thread from main thread!");
        }
        
        //2. sync!
        final MessageQueueSync idler = new MessageQueueSync(null);        
        newHandler().post(new Runnable() {
            @Override
            public void run() {
                 Looper.myQueue().addIdleHandler(idler);                
            }
        });
        idler.waitForIdle();
    }   
}
