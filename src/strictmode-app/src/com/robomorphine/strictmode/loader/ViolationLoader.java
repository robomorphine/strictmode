package com.robomorphine.strictmode.loader;

import com.google.common.base.Objects;
import com.robomorphine.loader.AsyncLoader;
import com.robomorphine.strictmode.violation.Violation;
import com.robomorphine.strictmode.violation.ViolationParser;
import com.robomorphine.strictmode.violation.filter.ViolationFilter;
import com.robomorphine.strictmode.violation.group.ViolationGroups;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.DropBoxManager;
import android.util.Log;

public class ViolationLoader extends AsyncLoader<ViolationGroups> {
    
    private static final String TAG = ViolationLoader.class.getSimpleName();    
    
    private static final int MAX_DATA_LEN = 5 * 1024;
    private static final String STRICT_MODE_TAG = "data_app_strictmode";

    private class DropBoxBroadcastReceiver extends BroadcastReceiver {
                
        @Override
        public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
        
        public void register(Context context) {
            IntentFilter intent = new IntentFilter();
            intent.addAction(DropBoxManager.ACTION_DROPBOX_ENTRY_ADDED);
            context.registerReceiver(this, intent);
        }
        
        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }
    }
        
    private final DropBoxBroadcastReceiver mReceiver = new DropBoxBroadcastReceiver();
    private final ViolationParser mViolationParser = new ViolationParser();
    private final ViolationGroups mViolationGroups;
    
    private long mTimestamp = 0;
    private ViolationFilter mViolationFilter;
        
    public ViolationLoader(Context context) {
        super(context);
        mViolationGroups = new ViolationGroups();
    }
   
    public void setFilter(ViolationFilter filter) {
        if(!Objects.equal(mViolationFilter, filter)) {
            mViolationFilter = filter;
            onContentChanged();
        }
    }
    
    @Override
    public ViolationGroups loadInBackground() {
        fetchNewDropBoxItems();
        ViolationGroups groups = ViolationGroups.clone(mViolationGroups, mViolationFilter);
        groups.sort();
        return groups;
    }    
    
    @Override
    protected void onRegisterObservers() {
        super.onRegisterObservers();
        mReceiver.register(getContext());
    }
    
    @Override
    protected void onUnregisterObservers() {
        super.onUnregisterObservers();
        mReceiver.unregister(getContext());
    }
    
    /********************************************/
    /**                 Core                   **/            
    /********************************************/
        
    public void fetchNewDropBoxItems() {
        DropBoxManager dbm = (DropBoxManager)getContext().getSystemService(Context.DROPBOX_SERVICE);
                
        try {
            DropBoxManager.Entry dbEntry = null;
            while(true) {
                dbEntry = dbm.getNextEntry(STRICT_MODE_TAG, mTimestamp);
                if(dbEntry == null) {
                    break;
                }
                
                String tag = dbEntry.getTag();
                String data = dbEntry.getText(MAX_DATA_LEN);
                mTimestamp = dbEntry.getTimeMillis();
                
                if(tag != null && data != null) {
                    onNewDropBoxItem(data, mTimestamp);
                }
                dbEntry.close();
            }
        } catch(Throwable ex) {//NOPMD
            Log.e(TAG, "Failed to fetch drop box items.", ex);
        }
    }
        
    private void onNewDropBoxItem(String data, long timestamp) {
        try {
            Violation violation = mViolationParser.createViolation(data);
            violation.setTimestamp(timestamp);
            mViolationGroups.add(violation);
        } catch(Exception ex) {
            //TODO: save information about exception to some file to report it later.
            throw new IllegalStateException(ex);
        }
    }
}

