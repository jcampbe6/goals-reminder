package com.goalsreminder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GoalsTable
{
	public static final String GOALS_DATABASE_TABLE = "goals";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_ROWID = "_id";
	
	// TAG for log messages
	private static final String TAG = "GoalsTable";
	
	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table" + GOALS_DATABASE_TABLE + " ("
			+ COLUMN_ROWID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_BODY + " text not null);";
	
	public static void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) 
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + GOALS_DATABASE_TABLE);
		onCreate(database);
	}

}
