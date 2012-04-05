package com.robomorphine.remoteprefs.loader;

import com.robomorphine.prefs.domain.DomainManager;
import com.robomorphine.prefs.domain.DomainObserver;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.HashSet;
import java.util.Set;

public class DomainListLoader extends AsyncTaskLoader<Set<String>> implements DomainObserver {
    
    private final DomainManager mDomainManager;
    private Set<String> mDomains;

    public DomainListLoader(Context context, String packageName) {
        super(context);
        mDomainManager = new DomainManager(context, true);
        mDomainManager.setObserver(this);
        mDomainManager.startTracking();
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(takeContentChanged() || mDomains == null) {
            forceLoad();
        } else {
            deliverResult(mDomains);
        }
        
    }
    
    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }
    
    @Override
    public Set<String> loadInBackground() {
        mDomainManager.refreshDiskPreferences();
        mDomainManager.refreshMemoryPreferences();
                
        mDomains = new HashSet<String>(mDomainManager.getPreferences());
        return mDomains;
    }
   
      
    @Override
    protected void onAbandon() {
        super.onAbandon();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
        mDomains = null;
    }
    
    @Override
    public void deliverResult(Set<String> data) {
        super.deliverResult(data);
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
