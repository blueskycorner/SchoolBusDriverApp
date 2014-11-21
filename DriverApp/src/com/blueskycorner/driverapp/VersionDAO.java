package com.blueskycorner.driverapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class VersionDAO extends SchoolBusDAO 
{
    // Books table name
    protected static final String TABLE = "version";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VERSION = "version";
    
    public VersionDAO(SqlLiteHelper pi_sqliteHelper) 
    {
        super(pi_sqliteHelper);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS" + TABLE;
 
        return s;
	}

	public int GetVersion(int pi_id) 
	{
		int version = 0;
		
        // 1. build the query
        String query = "SELECT " + KEY_VERSION + " FROM " + TABLE + " WHERE " + KEY_ID + " = " + pi_id;
    	
        // 2. get reference to writable DB
        Cursor c = m_database.rawQuery(query, null);
        
        if ( (c != null) && (c.moveToFirst()) )
        {
        	version = c.getInt(c.getColumnIndex(KEY_VERSION));			
        }

        return version;
	}
	
	public void UpdateVersion(int pi_id, int pi_version) 
	{
		ContentValues values = new ContentValues();
		values.put(KEY_VERSION, pi_version); // get 
		
        // 1. build the query
		m_database.update(TABLE, values, KEY_ID + " = ?", new String[] { String.valueOf(pi_id) });
	}


	
	public void InsertVersion(int pi_id, int pi_version) throws Exception 
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ID, pi_id);
		values.put(KEY_VERSION, pi_version);
		
        // 1. build the query
		Insert(values);
	}

	@Override
	protected String GetTableName() 
	{
		return TABLE;
	}
}
