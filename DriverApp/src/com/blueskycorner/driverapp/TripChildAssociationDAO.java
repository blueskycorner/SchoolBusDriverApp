package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;

public class TripChildAssociationDAO extends SchoolBusDAO 
{
    // Books table name
    protected static final String TABLE = "trip_child_association";

    // Books Table Columns names
    private static final String KEY_TRIP_ID = "trip_id";
    private static final String KEY_CHILD_ID = "child_id";
    private static final String KEY_PICKUP_TIME_HOUR = "pickup_time_hour";
    private static final String KEY_PICKUP_TIME_MINUTE = "pickup_time_minute";
    
    public TripChildAssociationDAO(Context context) 
    {
        super(context);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public ArrayList<Child> GetChilds(int pi_tripId)
	{
		ArrayList<Child> childs = new ArrayList<Child>();
        // 1. build the query
        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_TRIP_ID + " = " + pi_tripId;
    	
        // 2. get reference to writable DB
        Cursor cursor = m_database.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
            	try
            	{
            		Child child = new Child();
            		child.m_id = cursor.getInt(cursor.getColumnIndex(KEY_CHILD_ID));
            		child.m_pickupTimeHour = cursor.getInt(cursor.getColumnIndex(KEY_PICKUP_TIME_HOUR));
            		child.m_pickupTimeMinute = cursor.getInt(cursor.getColumnIndex(KEY_PICKUP_TIME_MINUTE));
	            	childs.add(child);
            	}
            	catch (Exception e) 
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 
            } while (cursor.moveToNext());
        }

		return childs;
	}
}
