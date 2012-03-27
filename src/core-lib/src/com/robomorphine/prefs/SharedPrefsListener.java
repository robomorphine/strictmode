package com.robomorphine.prefs;

public interface SharedPrefsListener {
    void onDomainAdded(String name);
    void onDomainRemoved(String name);
    void onDomainCleared(String name);
    
    void onVariableAdded(String domain, String name);
    void onVariableRemoved(String domain, String name);
    
    void onValueChanged(String domain, String name);
    
}
