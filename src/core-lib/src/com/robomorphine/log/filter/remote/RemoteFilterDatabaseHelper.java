package com.robomorphine.log.filter.remote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.robomorphine.log.Log;
import com.robomorphine.log.tag.RbmTags;

public class RemoteFilterDatabaseHelper extends SQLiteOpenHelper
{
	private final static String TAG = RbmTags.getTag(RemoteFilterDatabaseHelper.class);
	
	private static final String DATABASE_NAME = "rbm-remote-filters.db";
    private static final int DATABASE_VERSION = 1;    
	
	public RemoteFilterDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{	
		db.execSQL("CREATE TABLE " + RemoteFilterContract.Filter.TABLE_NAME +
					" (" + RemoteFilterContract.Filter._ID +   " INTEGER PRIMARY KEY AUTOINCREMENT,"
						 + RemoteFilterContract.Filter.PACKAGE +  " STRING," 
					     + RemoteFilterContract.Filter.PRIORITY + " INTEGER, "
					     + RemoteFilterContract.Filter.ACTION + " INTEGER, "
					     + RemoteFilterContract.Filter.LEVEL + " INTEGER, "
					     + RemoteFilterContract.Filter.TAG + " STRING, "
					     + RemoteFilterContract.Filter.MSG + " STRING, "
					     +  " UNIQUE (" + RemoteFilterContract.Filter.PACKAGE + ", " 
			                            + RemoteFilterContract.Filter.PRIORITY + ") "  
					     +  " ON CONFLICT FAIL"
			        + ");");	   
	}
	
	@Override
	public void onOpen(SQLiteDatabase db)
	{	
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    /*TODO: implement a better upgrade procedure */
	    Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " + RemoteFilterContract.Filter.TABLE_NAME);
		onCreate(db);
	}
}
