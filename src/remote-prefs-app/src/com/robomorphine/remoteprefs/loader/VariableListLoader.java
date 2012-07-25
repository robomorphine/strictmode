package com.robomorphine.remoteprefs.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Map;

public class VariableListLoader extends AsyncTaskLoader<Map<String, Object>>{
    
    public VariableListLoader(Context context) {
        super(context);
    }
    
    @Override
    public Map<String, Object> loadInBackground() {
        return null;
    }
}
