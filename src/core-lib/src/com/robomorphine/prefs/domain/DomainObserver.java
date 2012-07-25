package com.robomorphine.prefs.domain;

public interface DomainObserver {
    void onDomainAdded(String name);
    void onDomainChanged(String name);
}
