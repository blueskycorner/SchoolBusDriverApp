package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class TripDAO extends SchoolBusDAO 
{
    // Books table name
    protected static final String TABLE = "trip";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SCHOOL_ID = "school_id";
    private static final String KEY_DESTINATION_ID = "destination_id";
    private static final String KEY_DAY_ID = "day_id";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    
    private TripDestinationDAO m_tripDestinationDAO = null;
    
    public TripDAO(Context context) 
    {
        super(context);
        m_tripDestinationDAO = new TripDestinationDAO(context);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public ArrayList<Trip> GetTrips(int pi_schoolId, E_DAY pi_day, int pi_hour, int pi_minute)
	{
		ArrayList<Trip> trips = new ArrayList<Trip>();
        // 1. build the query
        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_SCHOOL_ID + " = " + pi_schoolId;
        
        if (pi_day != null)
        {
        	query += " AND " + KEY_DAY_ID + " = " + Integer.toString(pi_day.getValue());
        }
//        if (pi_hour != -1)
//        {
//        	query += "";
//        }
//        if (pi_minute != -1)
//        {
//        	query += "";
//        }
    	
        // 2. get reference to writable DB
        Cursor cursor = m_database.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Trip trip = null;
        if (cursor.moveToFirst()) {
            do {
            	try
            	{
            		trip = FillupTrip(cursor);
	            	
	            	// Add book to books
            		trips.add(trip);
            	}
            	catch (Exception e) 
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 
            } while (cursor.moveToNext());
        }

		return trips;
	}

	public Trip GetTrip(int pi_tripId) 
	{
		Trip trip = null;
		
        // 1. build the query
        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_ID + " = " + pi_tripId;
            	
        // 2. get reference to writable DB
        Cursor c = m_database.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        if ( (c != null) && (c.moveToFirst()) )
        {
        	trip = FillupTrip(c);
        }
        
		return trip;
	}
	
	private Trip FillupTrip(Cursor cursor) 
	{
		Trip trip = null;
		String sDestination = m_tripDestinationDAO.GetDestination(m_database, cursor.getInt(cursor.getColumnIndex(KEY_DESTINATION_ID)));
		trip = new Trip();
		trip.m_id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
		trip.m_destination = sDestination;
		trip.m_hour = cursor.getInt(cursor.getColumnIndex(KEY_HOUR));
		trip.m_minute = cursor.getInt(cursor.getColumnIndex(KEY_MINUTE));
		return trip;
	}

}
