package com.robomorphine.prefs;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This implementation is designed to trigger notifications when new
 * keys are put into the map. It won't catch all possible HashMap usage scenarios,
 * only those that are used by ContextImpl to store SharedPreferneces. 
 * 
 * In short, this class implementation may be considered as tightly coupled with 
 * ContextImpl implementation. 
 */
class DomainMap extends HashMap<String, SharedPreferences> {
    
    private static final long serialVersionUID = 1L;
        
    public interface DomainMapObserver {
        void onDomainAdded(String name);
    }
    
    WeakHashMap<DomainMapObserver, Void> mObservers;
    
    public DomainMap() {
        mObservers = new WeakHashMap<DomainMapObserver, Void>();
    }
    
    @Override
    public SharedPreferences put(String key, SharedPreferences value) {
        SharedPreferences prefs = super.put(key, value);
        
        Set<DomainMapObserver> observers;
        synchronized (this) {
            observers = new HashSet<DomainMapObserver>(mObservers.keySet());
        }        
        for(DomainMapObserver observer : observers) {
            observer.onDomainAdded(key);
        }
        
        return prefs;
    }
  
    public void addObserver(DomainMapObserver observer) {
        synchronized (this) {
            mObservers.put(observer, null);
        }
    }
    
    public void removeObserver(DomainMapObserver observer) {
        synchronized (this) {
            mObservers.remove(observer);
        }
    }
}
