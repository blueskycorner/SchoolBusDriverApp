package com.blueskycorner.driverapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public abstract class SchoolBusDAO 
{
	// Database fields
    protected SQLiteDatabase m_database = null;
    protected SqlLiteHelper m_dbHelper = null;
	  
    SchoolBusDAO(Context pi_context)
	{
		m_dbHelper = new SqlLiteHelper(pi_context);
	}
	
	public void open() throws SQLException 
	{
		m_database = m_dbHelper.getWritableDatabase();
	}
	
	public void close() 
	{
	    m_dbHelper.close();
	}
	
	public void Delete()
	{
		m_database.execSQL("delete from "+ GetTableName());
	}

	protected abstract String GetTableName();
}
