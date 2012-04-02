package com.robomorphine.remoteprefs.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Set;

public class DomainListLoader extends AsyncTaskLoader<Set<String>>{

    public DomainListLoader(Context context, String packageName) {
        super(context);
    }    
    
    @Override
    public Set<String> loadInBackground() {
        
        return null;
    }
}
