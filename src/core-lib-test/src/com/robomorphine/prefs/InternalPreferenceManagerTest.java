package com.robomorphine.prefs;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.Tags;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.AndroidTestCase;

public class InternalPreferenceManagerTest extends AndroidTestCase {

    private static final String TAG = Tags.getTag(InternalPreferenceManager.class);
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PreferencesRemover remover = new PreferencesRemover(getContext());
        remover.clear();
    }
    
    public void testPreferences() {
        InternalPreferenceManager prefManager = new InternalPreferenceManager(getContext());
        
        for(String name : prefManager.getMemoryPreferences()) {
            Log.e(TAG, "MEM1: " + name);
        }
        
        for(String name : prefManager.getDiskPreferences()) {
            Log.e(TAG, "DISK1: " + name);
        }
        
        SharedPreferences prefs1 = getContext().getSharedPreferences("test1", Context.MODE_PRIVATE);
        SharedPreferences prefs2 = getContext().getSharedPreferences("test2", Context.MODE_PRIVATE);
        
        for(String name : prefManager.getMemoryPreferences()) {
            Log.e(TAG, "MEM2: " + name);
        }
        
        for(String name : prefManager.getDiskPreferences()) {
            Log.e(TAG, "DISK2: " + name);
        }
        
        prefs1.edit().commit();
        prefs2.edit().commit();
        
        for(String name : prefManager.getDiskPreferences()) {
            Log.e(TAG, "DISK3: " + name);
        }
    }
}
