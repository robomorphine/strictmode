package com.robomorphine.remoteprefs.loader;

import com.robomorphine.remoteprefs.AndroidPackage;
import com.robomorphine.remoteprefs.RemotePreferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class PackageListLoader extends AsyncTaskLoader<List<AndroidPackage>> {
        
    private class PackageBroadcastReceiver extends BroadcastReceiver {
        private boolean mRegistered = false;
        
        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
        
        public void register(Context context) {
            if(!mRegistered) {
                IntentFilter intent = new IntentFilter();
                intent.addAction(Intent.ACTION_PACKAGE_ADDED);
                intent.addAction(Intent.ACTION_PACKAGE_CHANGED);
                intent.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
                intent.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH);
                intent.addAction(Intent.ACTION_PACKAGE_INSTALL);
                intent.addAction(Intent.ACTION_PACKAGE_REMOVED);
                intent.addAction(Intent.ACTION_PACKAGE_REPLACED);
                intent.addAction(Intent.ACTION_PACKAGE_RESTARTED);
                context.registerReceiver(this, intent);
                mRegistered = true;
            }
        }
        
        public void unregister(Context context) {
            if(mRegistered) {
                context.unregisterReceiver(this);
                mRegistered = false;
            }
        }
    }    
    
    private final RemotePreferences mRemotePreferences;
    private final PackageBroadcastReceiver mReceiver = new PackageBroadcastReceiver();
    private List<AndroidPackage> mPackages;
    
    public PackageListLoader(Context context) {
        super(context);
        mRemotePreferences = new RemotePreferences(context);
    }
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(takeContentChanged() || mPackages == null) {
            forceLoad();
        } else {
            deliverResult(mPackages);
        }
    }
    
    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }
    
    @Override
    public List<AndroidPackage> loadInBackground() {
        mPackages = mRemotePreferences.getPackages();
        mReceiver.register(getContext());
        return mPackages;
    }
    
    @Override
    protected void onAbandon() {
        super.onAbandon();
        mReceiver.unregister(getContext());
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
        mPackages = null;
    }
}
