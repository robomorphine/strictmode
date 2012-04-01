package com.robomorphine.prefs.remote.provider;

import com.google.common.base.Preconditions;
import com.robomorphine.prefs.domain.DomainManager;
import com.robomorphine.prefs.remote.provider.RemotePrefsContract.Domain;
import com.robomorphine.prefs.remote.provider.RemotePrefsContract.Variable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class RemotePrefsProvider extends ContentProvider {
        
    DomainManager mPreferenceDomainManager;
    
    @Override
    public boolean onCreate() {
        mPreferenceDomainManager = new DomainManager(getContext(), false);
        mPreferenceDomainManager.startTracking();
        return true;
    }
    
    @Override
    public void shutdown() {
        mPreferenceDomainManager.stopTracking();
    }
        
    @Override
    public String getType(Uri uri) {
        
        return null;
    }
    
    /**
     * Only @param uri argument is honored. All other arguments are ignored.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, 
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        Preconditions.checkArgument(projection == null, "Not supported.");
        Preconditions.checkArgument(selection == null, "Not supported.");
        Preconditions.checkArgument(selectionArgs == null, "Not supported.");
        Preconditions.checkArgument(sortOrder == null, "Not supported.");
        
        /* Do we really want to refresh them here? */
        mPreferenceDomainManager.refreshMemoryPreferences();
        
        List<String> segments = uri.getPathSegments();
        if(segments.size() == 0) {
            /* enumerate shared preferences */
            MatrixCursor cursor = new MatrixCursor(Domain.COLUMN_NAMES);
            for(String name : mPreferenceDomainManager.getPreferences()) {
                cursor.addRow(new String[] { name });    
            }             
            return cursor;
        } else if(segments.size() == 1) {
            /* enumerate variables in specified preference domain */
            MatrixCursor cursor = new MatrixCursor(Variable.COLUMN_NAMES);
            
            String domainName = segments.get(0);
            if(!mPreferenceDomainManager.getPreferences().contains(domainName)) {
                /* no such domain exists */
                return cursor;
            }            
            
            SharedPreferences sharedPrefs = getContext().getSharedPreferences(domainName, Context.MODE_PRIVATE);
            Map<String, ?> variables = sharedPrefs.getAll();
            for(Entry<String, ?> entry : variables.entrySet()) {
                String [] row = Variable.newRow(entry.getKey(), entry.getValue());
                cursor.addRow(row);
            }
            return cursor;
        } else if(segments.size() == 2) {
            /* get values for specified variable */
            return null;
        } else {
            throw new IllegalArgumentException("Invalid uri: " + uri);
        }
    }
    
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
    
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
        
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
