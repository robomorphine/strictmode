package com.robomorphine.log.filter.remote;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.robomorphine.log.Log;
import com.robomorphine.log.filter.remote.RemoteFilterContract.Filter;
import com.robomorphine.log.tag.RbmTags;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteTransactionListener;
import android.net.Uri;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class RemoteFilterProvider extends ContentProvider { //NOPMD
    
    private final static String TAG = RbmTags.getTag(RemoteFilterProvider.class);
        
    @VisibleForTesting protected final static int MATCH_FILTERS = 0;
    @VisibleForTesting protected final static int MATCH_FILTERS_PACKAGE = 1;
    @VisibleForTesting protected final static int MATCH_FILTERS_ITEM = 2;
    
    @VisibleForTesting protected static final UriMatcher sUriMatcher;
    @VisibleForTesting protected static final Map<Integer, String> sUriContentTypes;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(RemoteFilterContract.AUTHORITY, 
                           Filter.URI_SUFFIX,
                           MATCH_FILTERS);
        
        sUriMatcher.addURI(RemoteFilterContract.AUTHORITY,                            
                           Filter.URI_PACKAGE_SUFFIX + "/*",
                           MATCH_FILTERS_PACKAGE);
        
        sUriMatcher.addURI(RemoteFilterContract.AUTHORITY, 
                           Filter.URI_SUFFIX + "/#", 
                           MATCH_FILTERS_ITEM);        
                        
        Map<Integer, String> contentTypes = new HashMap<Integer, String>();
        contentTypes.put(MATCH_FILTERS, Filter.CONTENT_TYPE);
        contentTypes.put(MATCH_FILTERS_PACKAGE, Filter.CONTENT_TYPE);
        contentTypes.put(MATCH_FILTERS_ITEM, Filter.CONTENT_ITEM_TYPE);
        
                        
        sUriContentTypes = Collections.unmodifiableMap(contentTypes);
    }
    
    private RemoteFilterDatabaseHelper mOpenHelper;
        
    @Override
    public boolean onCreate() {
        Log.v(TAG, "onCreate()");
        
        mOpenHelper = new RemoteFilterDatabaseHelper(getContext());     
        return true;
    }       
    
    @Override
    public String getType(Uri uri) {
        Preconditions.checkNotNull(uri);        
        Log.d(TAG, "getType() for %s", uri);
        
        int match = sUriMatcher.match(uri);
        String type = sUriContentTypes.get(match);
        if (type == null) {
            throw new IllegalArgumentException("Invalid uri: " + uri.toString());
        }
        return type;
    }    
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, 
                        String[] selectionArgs, String sortOrder) {
        
        Preconditions.checkNotNull(uri);
        Log.d(TAG, "Querying at %s", uri);
        
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Filter.TABLE_NAME);
        String defaultSortOrder = Filter.DEFAULT_SORT_ORDER;
        
        switch(sUriMatcher.match(uri)) {
            case MATCH_FILTERS:
                break;
            case MATCH_FILTERS_PACKAGE:                
                qb.appendWhere(Filter.PACKAGE + " = \"" + uri.getLastPathSegment()+"\"");
                defaultSortOrder = Filter.DEFAULT_SORT_ORDER;
                break;
            case MATCH_FILTERS_ITEM:                
                qb.appendWhere(Filter._ID + " = " + ContentUris.parseId(uri));                
                break;
            default:
                throw new IllegalArgumentException("Unknown URL \"" + uri + "\".");
        }

        if (sortOrder == null) {
            sortOrder = defaultSortOrder;
        }

        if (projection == null) {
            projection = Filter.FULL_PROJECTION;
        }
        
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                
        c.setNotificationUri(getContext().getContentResolver(), uri);        
        return c;        
    }
    
    /******************************************
     *     Helpers: Notifications
     ******************************************/
    
    /**
     * Takes an ordered set of pending notifications and fires notifications
     * one by one.
     * @param notificationUris
     */
    private void firePendingNotifications(String action, LinkedHashSet<Uri> notificationUris) { //NOPMD        
        for (Uri uri : notificationUris) {
            Log.v(TAG, "Change notification (%s) for " + uri.toString()+" is fired.", action);
            getContext().getContentResolver().notifyChange(uri, null);            
        }
        notificationUris.clear();
    }
    
    
    /******************************************
     *     Helpers: ContentValues checks
     ******************************************/    
    @VisibleForTesting
    protected static void badValue(ContentValues values, String key) {
        badValue(values, key, null);
    }
    
    /**
     * Throws an exception with message that describes bad value in details.
     */
    @VisibleForTesting
    protected static void badValue(ContentValues values, String key, String customMessage) {        
        Object obj = values.get(key);
        String value = null;
        if (obj != null) {
            value = obj.toString();
        }
        
        if (customMessage == null) {
            customMessage = "";
        }
        
        String msg = String.format("Value %s = %s has wrong type or is invalid. %s", 
                                    key, value, customMessage);        
        throw new IllegalArgumentException(msg);
    }    
    
    /**
     * Verifies all values are of valid type and within valid domains.
     */
    @VisibleForTesting
    protected static void checkFilterValues(ContentValues values) { //NOPMD   
        String pkg = values.getAsString(Filter.PACKAGE); 
        if(pkg == null || pkg.length() == 0) {
            badValue(values, Filter.PACKAGE);
            return;
        }       
    
        Integer priority = values.getAsInteger(Filter.PRIORITY); 
        if(priority == null || priority < 0) {
            badValue(values, Filter.PRIORITY);
            return;
        }
        
        Integer action = values.getAsInteger(Filter.ACTION); 
        if(action == null) {
            badValue(values, Filter.ACTION);
            return;
        }
        
        if(action != 0 && action != 1 && action != 2) {
            badValue(values, Filter.ACTION, "Invalid action value.");
            return;
        }        
        
        Integer level = values.getAsInteger(Filter.LEVEL);
        if(level == null) {
            badValue(values, Filter.LEVEL);
            return;
        }
                
        if(!Log.isValidLevel(level)) {
            badValue(values, Filter.LEVEL, "Invalid log level.");
            return;
        }
        
        String tag = values.getAsString(Filter.TAG); 
        if(tag == null) {
            badValue(values, Filter.TAG);
            return;
        }
        
        String msg = values.getAsString(Filter.MSG);
        if(msg == null) {
            badValue(values, Filter.MSG);
            return;
        }
    }
    
    /**************************************
     *              Insert
     **************************************/
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Preconditions.checkNotNull(uri);
        Preconditions.checkNotNull(values);
        
        LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>(); 
        Uri result = insert(uri, values, notificationUris);
        firePendingNotifications("insert", notificationUris);
        return result;
    }    
    
    public Uri insert(Uri uri, ContentValues values, LinkedHashSet<Uri> notify) { //NOPMD
        Preconditions.checkNotNull(uri);
        Preconditions.checkNotNull(values);
        Preconditions.checkNotNull(notify);
        
        Log.d(TAG, "Inserting at %s", uri);
        
        switch(sUriMatcher.match(uri)) {
        case MATCH_FILTERS:                         
            break;
        case MATCH_FILTERS_PACKAGE:            
            if(values.containsKey(Filter.PACKAGE)) {
                Log.w(TAG, "PACKAGE in ContentValues is ignored."); 
            }
            values = new ContentValues(values);
            values.put(Filter.PACKAGE, uri.getLastPathSegment());            
            break;
        default:
            throw new IllegalArgumentException("Unknown URL \""+uri+"\".");
        }        
        
        checkFilterValues(values);

                        
        try {            
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            
            /* execute insert */
            long rowId = db.insert(Filter.TABLE_NAME, null, values);            
            if (rowId <= 0) {
                throw new SQLException("Failed to insert row into " + uri);
            }            
            
            /* result uri */
            Uri itemUri = ContentUris.withAppendedId(Filter.CONTENT_URI, rowId);
            
            /* notification */
            String pkg = values.getAsString(Filter.PACKAGE);            
            notify.add(Filter.CONTENT_URI);
            notify.add(Uri.withAppendedPath(Filter.CONTENT_PKG_URI, pkg));
            
            /* done */        
            Log.v(TAG, "Inserted " + itemUri);
            return itemUri;
            
        } catch(SQLiteConstraintException ex) {
            throw new IllegalArgumentException("Non-unique data", ex);
        }
    }
    
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        Preconditions.checkNotNull(uri);
        Preconditions.checkNotNull(values);
        
        Log.d(TAG, "Bulk insert of %d items at %s", values.length, uri);
                
        int count = 0;      
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        final LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();        
        db.beginTransactionWithListener(new SQLiteTransactionListener() {            
            @Override
            public void onRollback() {
            }
            
            @Override
            public void onCommit() {
                /* notify about changes */
                firePendingNotifications("bulk insert", notificationUris);
            }
            
            @Override
            public void onBegin() {                
            }
        });
        
        try
        {           
            for(ContentValues curValues : values)
            {
                insert(uri, curValues, notificationUris);
                count++;
            }
            db.setTransactionSuccessful();
            Log.d(TAG, "Bulk insert at %s ( %d rows ) is completed.", uri, count);
        }       
        finally
        {
            db.endTransaction();                        
        }
        
        return count;
    }
    
    /******************************************
     *        Helpers: Where composers
     ******************************************/
    
    public String whereAppendId(long id, String where) {
        String clause = Filter._ID + "=" + id;
        if(where != null) {
            clause = " AND ( " + where + ")";
        }
        return clause;
    }
    
    public String whereAppendPkg(String pkg, String where) {
        String clause = Filter.PACKAGE + "= \"" + pkg + "\"";
        if(where != null) {
            clause = " AND ( " + where + ")";
        }
        return clause;
    }
    
    /******************************************
     *        Helpers: notification URIs
     ******************************************/
       
    @VisibleForTesting
    protected void addNotificationUris(LinkedHashSet<Uri> uris, String where, String [] whereArgs) { //NOPMD
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(Filter.TABLE_NAME, new String[] { Filter.PACKAGE }, 
                                 where, whereArgs, Filter.PACKAGE, 
                                 null, Filter.PACKAGE + " ASC");
        
        if(cursor.getCount() == 0) {
            return;
        }
                
        /* adding root uri */
        uris.add(Filter.CONTENT_URI);        
               
        /* adding package uris */
        cursor.moveToFirst();
        do {
            String pkg = cursor.getString(cursor.getColumnIndex(Filter.PACKAGE));
            Uri pkgUri = Uri.withAppendedPath(Filter.CONTENT_PKG_URI, pkg);
            uris.add(pkgUri);
            
        } while(cursor.moveToNext());
    }
    
    /**************************************
     *              delete
     **************************************/
         
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "Deleting at "+uri);
                
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        String table = Filter.TABLE_NAME;        
        switch (sUriMatcher.match(uri)) {
            case MATCH_FILTERS:                
                break;
            case MATCH_FILTERS_PACKAGE:
                String pkg = uri.getLastPathSegment();
                selection = whereAppendPkg(pkg, selection);                
                break;
            case MATCH_FILTERS_ITEM:
                long productId = ContentUris.parseId(uri);
                selection = whereAppendId(productId, selection);                
                break;

            default:
                throw new IllegalArgumentException("Unknown URL \"" + uri + "\"");
        }
        
        LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();        
        addNotificationUris(notificationUris, selection, selectionArgs);
                
        int count = db.delete(table, selection, selectionArgs);        
        if(count > 0) {                            
            firePendingNotifications("delete", notificationUris);
        }        
        return count;        
    }   
    
    /**************************************
     *              update
     **************************************/
    
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.d(TAG, "Updating at %s", uri);
       
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                
        switch (sUriMatcher.match(uri)) 
        {
        case MATCH_FILTERS:
            //nothing to do
            break;
        case MATCH_FILTERS_PACKAGE:
            String pkg = uri.getLastPathSegment();
            selection = whereAppendPkg(pkg, selection);
            break;
        case MATCH_FILTERS_ITEM:
            long productId =  ContentUris.parseId(uri);                        
            selection = whereAppendId(productId, selection);                        
            break;       
        default:
            throw new IllegalArgumentException("Unknown URL \"" + uri + "\"");
        }        
        
        LinkedHashSet<Uri> notificationUris = new LinkedHashSet<Uri>();        
        addNotificationUris(notificationUris, selection, selectionArgs);        
        
        try {
            
            int count = db.update(Filter.TABLE_NAME, values, selection, selectionArgs);            
            if(count > 0) {
                firePendingNotifications("update", notificationUris);
            }            
            return count;
            
        } catch (SQLiteConstraintException ex ){
            throw new IllegalArgumentException("Non unique data.", ex);
        }
    }
    
    public void shutdown() {
    	mOpenHelper.close();
    }
}
