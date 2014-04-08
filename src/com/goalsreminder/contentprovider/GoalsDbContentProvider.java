package com.goalsreminder.contentprovider;

import java.util.Arrays;
import java.util.HashSet;
import com.goalsreminder.database.GoalsDatabaseHelper;
import com.goalsreminder.database.GoalsTable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GoalsDbContentProvider extends ContentProvider
{
	
	// variable declarations
	// database
	private GoalsDatabaseHelper database;
	
	// used for the UriMacher
	private static final int GOALS = 10;
	private static final int GOAL_ID = 20;
	
	private static final String AUTHORITY = "com.goalsreminder.contentprovider";
	private static final String BASE_PATH = "goals";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/goals";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/goal";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static
	{
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, GOALS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", GOAL_ID);
	}
	
	@Override
	public boolean onCreate()
	{
		database = new GoalsDatabaseHelper(getContext());
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		// Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // check if the caller has requested a column which does not exists
	    checkColumns(projection);

	    // Set the table
	    queryBuilder.setTables(GoalsTable.GOALS_DATABASE_TABLE);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case GOALS:
	      break;
	    case GOAL_ID:
	      // adding the ID to the original query
	      queryBuilder.appendWhere(GoalsTable.COLUMN_ROWID + "="  + uri.getLastPathSegment());
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }

	    SQLiteDatabase db = database.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    
	    // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}
	
	@Override
	public String getType(Uri uri)
	{
		return null;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    long id = 0;
	    switch (uriType) {
	    case GOALS:
	      id = sqlDB.insert(GoalsTable.GOALS_DATABASE_TABLE, null, values);
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case GOALS:
	      rowsDeleted = sqlDB.delete(GoalsTable.GOALS_DATABASE_TABLE, selection,
	          selectionArgs);
	      break;
	    case GOAL_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(GoalsTable.GOALS_DATABASE_TABLE,
	            GoalsTable.COLUMN_ROWID + "=" + id, 
	            null);
	      } 
	      
	      else {
	        rowsDeleted = sqlDB.delete(GoalsTable.GOALS_DATABASE_TABLE,
	            GoalsTable.COLUMN_ROWID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
	    case GOALS:
	      rowsUpdated = sqlDB.update(GoalsTable.GOALS_DATABASE_TABLE, 
	          values, 
	          selection,
	          selectionArgs);
	      break;
	    case GOAL_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsUpdated = sqlDB.update(GoalsTable.GOALS_DATABASE_TABLE, 
	            values,
	            GoalsTable.COLUMN_ROWID + "=" + id, 
	            null);
	      } else {
	        rowsUpdated = sqlDB.update(GoalsTable.GOALS_DATABASE_TABLE, 
	            values,
	            GoalsTable.COLUMN_ROWID + "=" + id 
	            + " and " 
	            + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
	    String[] available = { GoalsTable.COLUMN_TITLE,
	        GoalsTable.COLUMN_BODY,
	        GoalsTable.COLUMN_ROWID };
	    if (projection != null) {
	      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
	      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
	      // check if all columns which are requested are available
	      if (!availableColumns.containsAll(requestedColumns)) {
	        throw new IllegalArgumentException("Unknown columns in projection");
	      }
	    }
	  }
}
