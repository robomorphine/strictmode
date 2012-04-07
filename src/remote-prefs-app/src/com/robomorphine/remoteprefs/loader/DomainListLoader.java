package com.robomorphine.remoteprefs.loader;

import com.robomorphine.loader.AsyncLoader;
import com.robomorphine.prefs.domain.DomainManager;
import com.robomorphine.prefs.domain.DomainObserver;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public class DomainListLoader extends AsyncLoader<Set<String>> implements DomainObserver {
    
    private final DomainManager mDomainManager;

    public DomainListLoader(Context context, String packageName) {
        super(context);
        mDomainManager = new DomainManager(context, true);
        mDomainManager.setObserver(this);
        mDomainManager.startTracking();
    }
      
    @Override
    public Set<String> loadInBackground() {
        mDomainManager.refreshDiskPreferences();
        mDomainManager.refreshMemoryPreferences();
        return new HashSet<String>(mDomainManager.getPreferences());
    }
    
    @Override
    protected void onRegisterObservers() {
        super.onRegisterObservers();
        mDomainManager.startTracking();
    }
    
    @Override
    protected void onUnregisterObservers() {
        super.onUnregisterObservers();
        mDomainManager.stopTracking();
    }
  
    
    /*******************************/
    /**        DomainObserver     **/
    /*******************************/
    
    @Override
    public void onDomainChanged(String name) {
        onContentChanged();     
    }
    
    @Override
    public void onDomainAdded(String name) {
        onContentChanged();
    }
}
