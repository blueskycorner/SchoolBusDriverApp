package com.blueskycorner.driverapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class SchoolBusDAO 
{
	// Database fields
    protected SQLiteDatabase m_database = null;
    protected SqlLiteHelper m_dbHelper = null;
	  
    SchoolBusDAO(SqlLiteHelper pi_sqliteHelper)
	{
		m_dbHelper = pi_sqliteHelper;
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
