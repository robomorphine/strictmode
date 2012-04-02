package com.robomorphine.remoteprefs.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Map;

public class PackageListLoader extends AsyncTaskLoader<Map<String, String>>{
    
    public PackageListLoader(Context context, String metaDataKey) {
        super(context);
    }
    
    @Override
    public Map<String, String> loadInBackground() {
        return null;
    }
    
    
}
