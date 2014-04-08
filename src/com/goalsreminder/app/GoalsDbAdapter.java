package com.goalsreminder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GoalsDbAdapter
{
	private static final String DATABASE_NAME = "goals_data";
	public static final String DATABASE_TABLE = "goals";
	private static final int DATABASE_VERSION = 4;
	
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	
	private static final String TAG = "GoalsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table goals (_id integer primary key autoincrement, title text not null, body text not null);";
	
	private final Context mContext;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	/**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param context the Context within which to work
     */
	public GoalsDbAdapter(Context context)
	{
		mContext = context;
	}
	
	/**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
	public GoalsDbAdapter open() throws SQLException
	{
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		mDbHelper.close();
	}
	
	/**
     * Create a new goal using the title and body provided. If the goal is
     * successfully created return the new rowId for that goal, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the goal
     * @param body the body of the goal
     * @return rowId or -1 if failed
     */
	public long createGoal(String title, String body)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	/**
     * Delete the goal with the given rowId
     * 
     * @param rowId id of goal to delete
     * @return true if deleted, false otherwise
     */
	public boolean deleteGoal(long rowId)
	{
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
     * Return a Cursor over the list of all goals in the database
     * 
     * @return Cursor over all goals
     */
	public Cursor fetchAllGoals()
	{
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY}, null, null, null, null, null);
	}
	
	/**
     * Return a Cursor positioned at the goal that matches the given rowId
     * 
     * @param rowId id of goal to retrieve
     * @return Cursor positioned to matching goal, if found
     * @throws SQLException if goal could not be found/retrieved
     */
	public Cursor fetchGoal(long rowId) throws SQLException
	{
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
     * Update the goal using the details provided. The goal to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of goal to update
     * @param title value to set goal title to
     * @param body value to set goal body to
     * @return true if the goal was successfully updated, false otherwise
     */
	public boolean updateGoal(long rowId, String title, String body)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor getCount()
	{
		String countQuery = "SELECT  * FROM " + DATABASE_TABLE;
		return mDb.rawQuery(countQuery, null);
	}
}