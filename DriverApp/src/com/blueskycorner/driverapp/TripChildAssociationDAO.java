package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TripChildAssociationDAO extends SchoolBusDAO 
{
    // Books table name
    protected static final String TABLE = "trip_child_association";

    // Books Table Columns names
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_CHILD_ID = "child_id";
    public static final String KEY_PICKUP_TIME_HOUR = "pickup_time_hour";
    public static final String KEY_PICKUP_TIME_MINUTE = "pickup_time_minute";
	private static final String KEY_ADDRESS_ID = "address_id";
    
    public TripChildAssociationDAO(SqlLiteHelper pi_sqliteHelper) 
    {
        super(pi_sqliteHelper);
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
            		child.m_activeAddress = cursor.getInt(cursor.getColumnIndex(KEY_ADDRESS_ID));
            		if ( (child.m_activeAddress != 0) && (child.m_activeAddress != 1) )
            		{
            			child.m_activeAddress = 0;
            		}
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
	
	public void InsertTripChildAssociation(int pi_tripId, int pi_childId, int pi_pickupTimeHour, int pi_pickupTimeMinute, int pi_addressId) throws Exception
	{
		ContentValues values = new ContentValues();
		values.put(KEY_TRIP_ID, pi_tripId);
		values.put(KEY_CHILD_ID, pi_childId);
		values.put(KEY_PICKUP_TIME_HOUR, pi_pickupTimeHour);
		values.put(KEY_PICKUP_TIME_MINUTE, pi_pickupTimeMinute);
		values.put(KEY_ADDRESS_ID, pi_addressId);
		
	    // 1. build the query
		Insert(values);
	}

	@Override
	protected String GetTableName() 
	{
		return TABLE;
	}
}
