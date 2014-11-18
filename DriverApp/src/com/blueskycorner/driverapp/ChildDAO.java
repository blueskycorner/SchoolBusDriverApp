package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChildDAO extends SchoolDAO 
{
    // Books table name
    protected static final String TABLE = "child";
    protected static final String TABLE_TRIP_CHILD_ASSOCIATION = "trip_child_association";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LANGUAGE_ID = "language_id";
    private static final String KEY_CREATION_DATE = "creation_date";
    private static final String KEY_MODIFICATION_DATE = "modification_date";
    
    private SimpleDateFormat m_parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public ChildDAO(Context context) 
    {
        super(context);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public ArrayList<Child> GetChildInfo(ArrayList<Child> pi_childs)
	{
		for (Child child : pi_childs)
		{
			Child info = GetChild(m_database, child.m_id);
			child.m_firstName = info.m_firstName;
			child.m_lastName = info.m_lastName;
			child.m_address = info.m_address;
			child.m_language = info.m_language;
			child.m_creationDate = info.m_creationDate;
			child.m_modificationDate = info.m_modificationDate;
		}
		
		return pi_childs;
	}

	public Child GetChild(SQLiteDatabase pi_db, int pi_ChildId) 
	{
		Child child = null;
		
		try
		{
	        // 1. build the query
	        String query = "SELECT * FROM " + TABLE + " WHERE " + KEY_ID + " = " + pi_ChildId;
	    	
	        // 2. get reference to writable DB
	        Cursor c = pi_db.rawQuery(query, null);
	        
	        if (c != null)
	        {
	        	c.moveToFirst();
	        	child = new Child();
	        	child.m_id = c.getInt(c.getColumnIndex(KEY_ID));
	        	child.m_firstName = c.getString(c.getColumnIndex(KEY_FIRST_NAME));
	        	child.m_lastName = c.getString(c.getColumnIndex(KEY_LAST_NAME));
	        	child.m_address = c.getString(c.getColumnIndex(KEY_ADDRESS));
	        	child.m_language = E_LANGUAGE.FromInt(c.getInt(c.getColumnIndex(KEY_LANGUAGE_ID)));
	        	child.m_creationDate = m_parser.parse(c.getString(c.getColumnIndex(KEY_CREATION_DATE)));
	        	child.m_modificationDate = m_parser.parse(c.getString(c.getColumnIndex(KEY_MODIFICATION_DATE)));
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        return child;
	}

}
