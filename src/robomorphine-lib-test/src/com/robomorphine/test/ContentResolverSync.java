package com.robomorphine.test;

import junit.framework.Assert;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.robomorphine.log.Log;

public class ContentResolverSync extends ContentObserver {
    private static final String TAG = RbmtTags.getTag(ContentResolverSync.class);

    private final Handler mHandler;
    private boolean mSync = false;

    public ContentResolverSync(Handler handler) {
        super(handler);
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {        
        mHandler.post(new Runnable() {                
            @Override
            public void run() {
                synchronized (ContentResolverSync.this) {
                    mSync = true;
                    ContentResolverSync.this.notifyAll();
                }
            }
        });        
    }

    public void sync(ContentResolver resolver, String authority) {
        Assert.assertNotSame("Can't sync from same thread.",        
                              mHandler.getLooper(), Looper.myLooper());

        Uri uri = Uri.parse("content://" + authority);
        Uri syncUri = Uri.withAppendedPath(uri, ContentResolverSync.class.getName());

        mSync = false;
        resolver.registerContentObserver(syncUri, false, this);

        Log.v(TAG, "ContentResolver sync started...");
        resolver.notifyChange(syncUri, null);
        synchronized (this) {
            while (!mSync) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        Log.v(TAG, "ContentResolver sync completed!");
    }
}