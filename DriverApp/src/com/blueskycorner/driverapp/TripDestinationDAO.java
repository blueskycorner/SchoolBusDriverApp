package com.blueskycorner.driverapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TripDestinationDAO extends SchoolBusDAO 
{
    // Books table name
	public static final String TABLE = "trip_destination";

    // Books Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_DESTINATION = "destination";
    
    public TripDestinationDAO(Context context) 
    {
        super(context);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public String GetDestination(SQLiteDatabase pi_db, int pi_id)
	{
		String sDestination = null;
		
        // 1. build the query
        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_ID + " = " + pi_id;
            	
        // 2. get reference to writable DB
        Cursor c = pi_db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        if ( (c != null) && (c.moveToFirst()) )
        {
        	sDestination = c.getString(c.getColumnIndex(KEY_DESTINATION));
        }
        
		return sDestination;
	}
	
	public void InsertDestination(int pi_id, String pi_destination)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ID, pi_id);
		values.put(KEY_DESTINATION, pi_destination);
		
        // 1. build the query
		m_database.insert(TABLE, null, values);

	}

	@Override
	protected String GetTableName() 
	{
		return TABLE;
	}
}
