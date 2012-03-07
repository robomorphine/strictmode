package com.robomorphine.prefs.remote;

import com.google.common.base.Preconditions;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.List;


public class RemotePrefsProvider extends ContentProvider {
        
    @Override
    public boolean onCreate() {
        return true;
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
        
        List<String> segments = uri.getPathSegments();
        if(segments.size() == 0) {
            /* enumerate shared preferences */
            MatrixCursor cursor = new MatrixCursor(RemotePrefsContract.SharedPrefs.COLUMN_NAMES);
            return cursor;
        } else if(segments.size() == 1) {
            /* enumerate variables in specified shared preferences */
            String name = segments.get(0);
            SharedPreferences sharedPrefs = getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
            sharedPrefs.getAll().keySet();
            return null;
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
