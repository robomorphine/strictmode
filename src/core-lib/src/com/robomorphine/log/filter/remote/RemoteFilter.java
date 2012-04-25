package com.robomorphine.log.filter.remote;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.robomorphine.log.filter.advanced.PriorityFilter;

//TODO: test implementation
public class RemoteFilter extends PriorityFilter<RemoteSubfilter> {

    private final Context mContext;
    private final ContentResolver mResolver;
    private final Uri mPkgContentUri;
    private ContentObserver mContentObserver;

    public RemoteFilter(Context context, String packageName, Handler handler) {
        mContext = context.getApplicationContext();
        mResolver = mContext.getContentResolver();
        mContentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                update();
            }
        };

        Uri uri = RemoteFilterContract.Filter.CONTENT_PKG_URI;
        mPkgContentUri = Uri.withAppendedPath(uri, packageName);
    }

    public RemoteFilter(Context context) {
        this(context, context.getPackageName(), new Handler());
    }

    public void startTracking() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.registerContentObserver(mPkgContentUri, true, mContentObserver);
        update();
    }

    public void stopTracking() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.unregisterContentObserver(mContentObserver);
    }

    public void update() {
        clear();
        load();
    }

    private void addSubfilter(Cursor cursor) {
        int action = cursor.getInt(cursor.getColumnIndex(RemoteFilterContract.Filter.ACTION));
        int level = cursor.getInt(cursor.getColumnIndex(RemoteFilterContract.Filter.LEVEL));
        String tag = cursor.getString(cursor.getColumnIndex(RemoteFilterContract.Filter.TAG));
        String msg = cursor.getString(cursor.getColumnIndex(RemoteFilterContract.Filter.MSG));

        FilterAction filterAction = FilterAction.Ignore;
        switch(action) {
            case RemoteFilterContract.Filter.ACTION_IGNORE:
                filterAction = FilterAction.Ignore;
                break;
            case RemoteFilterContract.Filter.ACTION_INCLUDE:
                filterAction = FilterAction.Include;
                break;
            case RemoteFilterContract.Filter.ACTION_EXCLUDE:
                filterAction = FilterAction.Exclude;
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }

        add(new RemoteSubfilter(level, tag, msg), filterAction);
    }

    public void load() {
        String [] projection = RemoteFilterContract.Filter.FULL_PROJECTION;
        String sortOrder = RemoteFilterContract.Filter.DEFAULT_SORT_ORDER;

        Cursor cursor = mResolver.query(mPkgContentUri, projection, null, null, sortOrder);
        try {
            cursor.moveToFirst();
            do {
                addSubfilter(cursor);
            } while(cursor.moveToNext());
        } finally {
            cursor.close();
        }
    }

    public void save() {
        /* clear old data */
        mResolver.delete(mPkgContentUri, null, null);

        /* save */
        ContentValues [] values = new ContentValues[size()];
        for(int i = 0; i < size(); i++) {
            FilterAction filterAction = getAction(i);
            RemoteSubfilter subfilter = getFilter(i);

            int action = RemoteFilterContract.Filter.ACTION_IGNORE;
            switch(filterAction) {
                case Ignore:
                    action = RemoteFilterContract.Filter.ACTION_IGNORE;
                    break;
                case Include:
                    action = RemoteFilterContract.Filter.ACTION_INCLUDE;
                    break;
                case Exclude:
                    action = RemoteFilterContract.Filter.ACTION_EXCLUDE;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action: " + action);
            }

            ContentValues curValues = values[i];
            curValues.put(RemoteFilterContract.Filter.ACTION, action);
            curValues.put(RemoteFilterContract.Filter.PRIORITY, i);
            curValues.put(RemoteFilterContract.Filter.LEVEL, subfilter.getLevel());
            curValues.put(RemoteFilterContract.Filter.TAG, subfilter.getTagWildcard());
            curValues.put(RemoteFilterContract.Filter.MSG, subfilter.getMsgWildcard());
        }

        mResolver.bulkInsert(mPkgContentUri, values);
    }
}
