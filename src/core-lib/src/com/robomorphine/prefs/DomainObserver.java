package com.robomorphine.prefs;

public interface DomainObserver {
    void onDomainAdded(String name);
    void onDomainChanged(String name);
}
