package com.robomorphine.prefs.remote;

import com.robomorphine.prefs.InternalPreferenceManager;
import com.robomorphine.prefs.PreferencesRemover;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.test.ProviderTestCase2;

import java.io.File;

public class RemotePrefsProviderTest extends ProviderTestCase2<RemotePrefsProvider>{
    
    private static String AUTHORITY = "com.robomorphine.prefs.remote";
    
    public RemotePrefsProviderTest() {
        super(RemotePrefsProvider.class, AUTHORITY);
    }
    
    private static class MyContextWrapper extends ContextWrapper {
        private final String mPrefix;
        
        public MyContextWrapper(Context context, String prefix) {
            super(context);
            mPrefix = prefix; 
        }
        
        @Override
        public File getFilesDir() {
            return new File(super.getFilesDir(), mPrefix);
        }
    }
    
    private Context mContext;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();       
        mContext = new MyContextWrapper(getContext(), "file");
        PreferencesRemover remover = new PreferencesRemover(getContext());
        remover.clear();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testQuery() {
        Uri.Builder builder = new Builder();
        builder.scheme("content");
        builder.authority(AUTHORITY);
        
        mContext.getSharedPreferences("test1", Context.MODE_PRIVATE).edit().commit();
        mContext.getSharedPreferences("test2", Context.MODE_PRIVATE).edit().commit();
        
        Cursor cursor = getContext().getContentResolver().query(builder.build(), null, null, null, null);
        int nameIndex = cursor.getColumnIndex(RemotePrefsContract.SharedPrefs.NAME);
        if(cursor.moveToFirst()) {
            String value = cursor.getString(nameIndex);
            assertNotNull(value);
        }
        cursor.close();
    }
    
    public void testPrefManager() throws Exception {
        InternalPreferenceManager manager = new InternalPreferenceManager(getContext());
        manager.startTracking();        
        mContext.getSharedPreferences("test1", Context.MODE_PRIVATE).edit().commit();
        
        Thread.sleep(1000);
        manager.stopTracking();
        
    }
}
