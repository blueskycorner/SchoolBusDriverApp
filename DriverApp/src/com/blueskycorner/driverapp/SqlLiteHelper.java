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
			
			String s = null;
			s = "INSERT INTO school VALUES (0, \"LFM\")";
			db.execSQL(s);
			s = "INSERT INTO school VALUES (1, \"GESM\")";
			db.execSQL(s);
			
			s = "INSERT INTO trip_destination VALUES (0, \"Alabang up\")";
			db.execSQL(s);

			s = "INSERT INTO trip VALUES (0, 0, 0, 0, 13, 30, 1)";
			db.execSQL(s);
			
			s = "INSERT INTO child VALUES (0, \"John\", \"STEWART\", \"21, Jump Street, MAKATI\", 0, \"2014-02-12 00:00\", \"2014-02-12 00:00\")";
			db.execSQL(s);
			s = "INSERT INTO child VALUES (1, \"Steve\", \"MAC QUEEN\", \"47, Kalamansi Street, MAKATI\", 0, \"2014-02-12 00:00\", \"2014-02-12 00:00\")";
			db.execSQL(s);

			s = "INSERT INTO trip_child_association VALUES (0, 0, 13, 50)";
			db.execSQL(s);
			s = "INSERT INTO trip_child_association VALUES (0, 1, 14, 10)";
			db.execSQL(s);
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
