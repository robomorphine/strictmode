
package com.robomorphine.test;

import com.robomorphine.log.Log;

public class MessageQueueSync implements android.os.MessageQueue.IdleHandler {
    
    private static final String TAG = RbmtTags.getTag(MessageQueueSync.class);
    
    private final Runnable mCallback;
    private boolean mIdle;

    public MessageQueueSync(Runnable callback) {
        mCallback = callback;
        mIdle = false;
    }

    public final boolean queueIdle() {
        if (mCallback != null) {
            mCallback.run();
        }
        synchronized (this) {
            mIdle = true;
            notifyAll();
        }
        return false;
    }

    public void waitForIdle() {
        Log.v(TAG, "Starting message queue sync.");
        synchronized (this) {
            while (!mIdle) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        Log.v(TAG, "Message queue synchronized!");
    }
}
