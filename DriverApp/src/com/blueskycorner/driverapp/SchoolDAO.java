package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class SchoolDAO extends SchoolBusDAO 
{
    // Books table name
    protected static final String TABLE = "school";

    // Books Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    
    public SchoolDAO(SqlLiteHelper pi_sqliteHelper) 
    {
        super(pi_sqliteHelper);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public ArrayList<School> GetSchools()
	{
		ArrayList<School> schools = new ArrayList<School>();
        // 1. build the query
        String query = "SELECT * FROM " + TABLE;
    	
        // 2. get reference to writable DB
        Cursor cursor = m_database.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        School school = null;
        if (cursor.moveToFirst()) {
            do {
            	try
            	{
	            	school = new School();
	            	school.m_id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
	            	school.m_name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
	            	
	            	// Add book to books
	            	schools.add(school);
            	}
            	catch (Exception e) 
            	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 
            } while (cursor.moveToNext());
        }

		return schools;
	}

	public School GetSchool(int pi_schoolId) 
	{
		School school = null;
		
        // 1. build the query
        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_ID + " = " + pi_schoolId;
    	
        // 2. get reference to writable DB
        Cursor c = m_database.rawQuery(query, null);
        
        if ( (c != null) && (c.moveToFirst()) )
        {
        	school = new School();
        	school.m_id = c.getInt(c.getColumnIndex(KEY_ID));
        	school.m_name = c.getString(c.getColumnIndex(KEY_NAME));			
        }

        return school;
	}
	
	public void InsertSchool(int pi_schoolId, String pi_name)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ID, pi_schoolId);
		values.put(KEY_NAME, pi_name);
		
	    // 1. build the query
		m_database.insert(TABLE, null, values);
	}

	@Override
	protected String GetTableName() 
	{
		return TABLE;
	}
}
