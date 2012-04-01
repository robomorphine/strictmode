package com.robomorphine.prefs;

import com.robomorphine.log.tag.Tags;

import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class DomainManagerTest extends AndroidTestCase {

    private static final String TAG = Tags.getTag(DomainManagerTest.class);
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PreferencesRemover remover = new PreferencesRemover(getContext());
        remover.clear();
    }
    
    public void testPreferences() throws Exception {
        DomainManager manager =  new DomainManager(getContext(), true);
        manager.startTracking();
        
        SharedPreferences prefs = getContext().getSharedPreferences("test", 0);
        
        Thread.sleep(2000);
        
        prefs.edit().putBoolean("testKey", true).commit();
        
        Thread.sleep(2000);
        
        prefs.edit().clear().commit();
        
        Thread.sleep(2000);
    }
}
