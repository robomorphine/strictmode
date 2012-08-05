package com.robomorphine.strictmode.viewer.loader;

import com.robomorphine.loader.AsyncLoader;
import com.robomorphine.strictmode.viewer.entity.DropBoxItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.DropBoxManager;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;

public class DropBoxLoader extends AsyncLoader<List<DropBoxItem>> {
    
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
    private final String mFilter;
    
    public DropBoxLoader(Context context, String filter) {
        super(context);
        mFilter = filter;
    }
    
    public String getFilter() {
        return mFilter;
    }
    
    @Override
    public List<DropBoxItem> loadInBackground() {
        return getItems();
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
        
    public List<DropBoxItem> getItems() {
        LinkedList<DropBoxItem> items = new LinkedList<DropBoxItem>();
        DropBoxManager dbm = (DropBoxManager)getContext().getSystemService(Context.DROPBOX_SERVICE);
        
        long timestamp = 0;
        DropBoxManager.Entry dbEntry = null;
        while(true) {
            dbEntry = dbm.getNextEntry(STRICT_MODE_TAG, timestamp);
            if(dbEntry == null) {
                break;
            }
            
            String tag = dbEntry.getTag();
            String data = dbEntry.getText(MAX_DATA_LEN);
            timestamp = dbEntry.getTimeMillis();
            
            if(tag != null && data != null) {
                boolean passed = TextUtils.isEmpty(mFilter);
                if(!passed) {
                    passed = data.contains(mFilter);
                }
                
                if(passed) {
                    items.push(new DropBoxItem(tag, timestamp, data));
                }
            }
            dbEntry.close();
        } 
        return items;
    }
}

