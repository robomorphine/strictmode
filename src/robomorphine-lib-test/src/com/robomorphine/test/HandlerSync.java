package com.robomorphine.test;

import junit.framework.Assert;
import android.os.Handler;
import android.os.Looper;

import com.robomorphine.log.Log;

public class HandlerSync extends Handler implements Runnable {
    
    private static final String TAG = RbmtTags.getTag(HandlerSync.class);

    private final Runnable mCallback;
    private boolean mSync;

    public HandlerSync(Looper looper, Runnable callback) {
        super(looper);
        mCallback = callback;
        mSync = false;
    }

    public HandlerSync(Looper looper) {
        this(looper, null);
    }

    @Override
    public void run() {
        if (mCallback != null) {
            mCallback.run();
        }
        synchronized (this) {
            mSync = true;
            notifyAll();
        }
    }

    public void waitForSync() {
        Assert.assertNotSame(getLooper(), Looper.myLooper());
        post(this);

        Log.v(TAG, "Starting handler sync.");
        synchronized (this) {
            while (!mSync) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        Log.v(TAG, "Handler synced!");
    }
}