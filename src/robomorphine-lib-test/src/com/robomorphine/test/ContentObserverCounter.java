package com.robomorphine.test;

import junit.framework.Assert;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.robomorphine.log.Log;

public class ContentObserverCounter extends ContentObserver {
    
    private final static String TAG = RbmtTags.getTag(ContentObserverCounter.class);
    
    private final Uri mUri;
    private final boolean mNotifyForDescedants;
    private int mCount;
    
    private Context mRegContext;
            
    public ContentObserverCounter(Handler handler, Uri uri, boolean descendant) {
        super(handler);  
        mUri = uri;
        mNotifyForDescedants = descendant;
    }        
    
    public void register(Context ctx) {
        mRegContext = ctx;
        ctx.getContentResolver().registerContentObserver(mUri, mNotifyForDescedants, this);            
    }
    
    public void unregister() {
        if(mRegContext != null) {
            mRegContext.getContentResolver().unregisterContentObserver(this);
        }
    }
    
    @Override
    public void onChange(boolean selfChange) {
        mCount++;       
        String msg = String.format("%d - Fired %s%s, count = %d",                    
                                    hashCode(), mUri, mNotifyForDescedants ? "/**/*" : "", mCount);
        Log.e(TAG, msg);
    }
    
    public int getCount() {
        return mCount;
    }
    
    public void resetCount() {
        mCount = 0;
    }
    
    public void checkFired(int expectedCount) {        
        Assert.assertEquals(expectedCount, mCount);
        Assert.assertTrue(mCount > 0);
    }
    
    public void checkNotFired() {
        Assert.assertEquals(0, mCount);
    }
}