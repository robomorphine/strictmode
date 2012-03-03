package com.robomorphine.test;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentProvider;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.test.ProviderTestCase2;

public class ProviderTestCase<T extends ContentProvider> extends ProviderTestCase2<T> {    
    
    private class ManagedContentObserverCounter extends ContentObserverCounter {
        public ManagedContentObserverCounter(Handler handler, Uri uri, boolean notifyDescedants) {
            super(handler, uri, notifyDescedants);
        }
        
        @Override
        public void register(Context ctx) {
            super.register(ctx);
            addManagedContentObserver(this);
        }
    }
    
    private final Set<ContentObserver> mRegisteredObservers = new HashSet<ContentObserver>();
    private final String mAuthority;
    
    public ProviderTestCase(Class<T> clazz, String providerAuthority) {
        super(clazz, providerAuthority);
        mAuthority = providerAuthority;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncContentResolver();
        mRegisteredObservers.clear();
    }
    
    @Override
    protected void tearDown() throws Exception {
        syncContentResolver();
        for(ContentObserver observer : mRegisteredObservers) {
            getContext().getContentResolver().unregisterContentObserver(observer);
            getMockContext().getContentResolver().unregisterContentObserver(observer);
        }
        super.tearDown();
    }
    
    protected void addManagedContentObserver(ContentObserver observer) {
        mRegisteredObservers.add(observer);
    }
    
    protected ContentObserverCounter newContentObserverCounter(Uri uri, boolean notifyDescendants) {
        return new ManagedContentObserverCounter(MainThread.newHandler(), uri, notifyDescendants);
    }
    
    protected void syncContentResolver() {
        ContentResolverSync syncer = new ContentResolverSync(MainThread.newHandler());
        syncer.sync(getContext().getContentResolver(), mAuthority);
    }
}
