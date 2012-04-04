package com.robomorphine.prefs.remote.provider;

import com.robomorphine.prefs.domain.DomainManager;
import com.robomorphine.prefs.domain.PreferencesRemover;
import com.robomorphine.prefs.remote.provider.RemotePrefsContract;
import com.robomorphine.prefs.remote.provider.RemotePrefsProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.test.ProviderTestCase2;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

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
        int nameIndex = cursor.getColumnIndex(RemotePrefsContract.Domain.NAME);
        if(cursor.moveToFirst()) {
            String value = cursor.getString(nameIndex);
            assertNotNull(value);
        }
        cursor.close();
    }
    
    public void testPrefManager() throws Exception {
        DomainManager manager = new DomainManager(getContext(), false);
        manager.startTracking();        
        mContext.getSharedPreferences("test1", Context.MODE_PRIVATE).edit().commit();
        
        Thread.sleep(1000);
        manager.stopTracking();
    }
    
    public void testStaticPrivate() throws Exception {
        Class<?> clz = Class.forName("android.app.ContextImpl");
        
        Field field = clz.getDeclaredField("sSharedPrefs");
        field.setAccessible(true);
        HashMap<String, SharedPreferences> prefs = new HashMap<String, SharedPreferences>();
        field.set(null, prefs);
        
        assertSame(prefs, field.get(null));
        getContext().getSharedPreferences("test", 0).edit().commit();
        assertEquals(1, prefs.size());
    }
    
    public void testSharedPrefsNotifications() throws Exception {
        DomainManager manager = new DomainManager(getContext(), false);
        manager.startTracking();
        getContext().getSharedPreferences("zz2z", 0).edit().putBoolean("sd", true).commit();
        getContext().getSharedPreferences("zz2z", 0).edit().clear().commit();
        Thread.sleep(2000);
        manager.stopTracking();
                
    }
}