package com.blueskycorner.driverapp;

import java.io.IOException;
import java.sql.SQLException;

import com.blueskycorner.toolbox.SqlParser;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteHelper extends SQLiteOpenHelper
{
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "school_bus_service";
    // DataBase sql File name
    private static final String DATABASE_FILE_NAME = "DB.SQL";
    
    private AssetManager m_assetManager = null;
    
	public SqlLiteHelper(Context pi_context) 
	{
		super(pi_context, DATABASE_NAME, null, DATABASE_VERSION);
		m_assetManager = pi_context.getAssets();
	}
	
	protected void execSqlFile(String sqlFile, SQLiteDatabase db ) throws SQLException, IOException 
	{
        for( String sqlInstruction : SqlParser.parseSqlFile(sqlFile, m_assetManager)) 
        {
            db.execSQL(sqlInstruction);
        }
    }

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		try
		{
			execSqlFile(DATABASE_FILE_NAME, db);
			
			// Init version table with -1 o be updated at the first update
			for (E_TABLE_ID id : E_TABLE_ID.values()) 
			{
				String s1 = null;
				s1 = "INSERT INTO version VALUES (" + Integer.toString(id.getValue()) + ", -1)";
				db.execSQL(s1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		String s = SchoolDAO.GetOnUpgrade();
		db.execSQL(s);
	}
}
