package com.robomorphine.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * TODO: add tests
 */
public abstract class AsyncLoader<D> extends AsyncTaskLoader<D> { //NOPMD
    private static final String TAG = AsyncLoader.class.getSimpleName();
    private D mData;    
    private boolean mObserversRegistered = false;
    private boolean mDataObserversRegistered = false;
    
    public AsyncLoader(@Nonnull Context context) {
        super(context);
    }
    
    /***************************/
    /**          Data         **/
    /***************************/
    
    @Nullable 
    protected D getData() {
        synchronized (this) {
            return mData;    
        }
    }
    
    @Nullable 
    protected void setData(@Nullable D data) {
        synchronized (this) {
            if(data == mData) {
                return;
            }
            
            D oldData = mData;
            mData = data;
            
            if(oldData != null) {
                unregisterDataObservers(oldData);
            }
            
            if(mData != null) {
                registerDataObservers(data);
            }
            
            if(oldData != null) {
                onReleaseResources(oldData);
            }
        }
    }
    
    protected void onReleaseResources(D data) {//NOPMD
    }
    
    
    /***************************/
    /**       Observers       **/
    /***************************/
    
    private void registerObservers() {
        synchronized (this) {
            if(!mObserversRegistered) {
                onRegisterObservers();
                mObserversRegistered = true;
            }   
        }
    }
    
    private void unregisterObservers() {
        synchronized (this) {
            if(mObserversRegistered) {
                onUnregisterObservers();
                mObserversRegistered = false;
            }
        }
    }
    
    protected void onRegisterObservers() {//NOPMD
    }
    
    protected void onUnregisterObservers() {//NOPMD
    }
    
    private void registerDataObservers(D data) {
        synchronized (this) {
            if(!mDataObserversRegistered) {
                onRegisterDataObservers(data);
                mDataObserversRegistered = true;
            }   
        }
    }
    
    private void unregisterDataObservers(D data) {
        synchronized (this) {
            if(mDataObserversRegistered) {
                onUnregisterDataObservers(data);
                mDataObserversRegistered = false;
            }
        }
    }
    
    protected void onRegisterDataObservers(@Nonnull D data) {//NOPMD
    }
    
    protected void onUnregisterDataObservers(@Nonnull D data) {//NOPMD
    }
    
    /***************************/
    /**          Lifecycle    **/
    /***************************/
    
    @Override
    protected D onLoadInBackground() {
        D data = super.onLoadInBackground();
        D oldData = getData();
        if(oldData == data) {//NOPMD, comparing with "==" on purpose
            String name = getClass().getSimpleName();
            Log.e(TAG, name + ": setData() is called with object that was already set. "+
                       "Note that onLoadFinished will not be called. " + 
                       "Create new object and set it using setData().");
        }
        
        return data;
    }
    
    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            /* release old data */
            setData(null);
            /* release new data */
            onReleaseResources(data);
            return;
        }
        
        if(isStarted()) {
            super.deliverResult(data);
        }
        setData(data);
    }  
    
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        
        registerObservers();
        
        D data = getData();
        if(takeContentChanged() || data == null) {
            forceLoad();
        } else {
            deliverResult(data);
        }
    }
    
    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }
    
    @Override
    protected void onAbandon() {//NOPMD
        super.onAbandon();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
        unregisterObservers();
        setData(null);
    }
    
    @Override
    public void onCanceled(D data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }

}
