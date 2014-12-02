package com.blueskycorner.driverapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChildDAO extends SchoolBusDAO
{
    // Books table name
    protected static final String TABLE = "child";
    protected static final String TABLE_TRIP_CHILD_ASSOCIATION = "trip_child_association";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_BIRTHDATE = "birthdate";
    private static final String KEY_GRADE_ID = "grade_id";
    private static final String KEY_ADDRESS_1 = "address1";
    private static final String KEY_ADDRESS_2 = "address2";
    private static final String KEY_LANGUAGE_ID = "language_id";
    private static final String KEY_CREATION_DATE = "creation_date";
    private static final String KEY_MODIFICATION_DATE = "modification_date";
    private static final String KEY_MONDAY_INFO = "monday_info";
    private static final String KEY_TUESDAY_INFO = "tuesday_info";
    private static final String KEY_WEDNESDAY_INFO = "wednesday_info";
    private static final String KEY_THURSDAY_INFO = "thursday_info";
    private static final String KEY_FRIDAY_INFO = "friday_info";
    
    private SimpleDateFormat m_parser = new SimpleDateFormat("yyyy:MM:dd");
    
    public ChildDAO(SqlLiteHelper pi_sqliteHelper) 
    {
        super(pi_sqliteHelper);
    }
	
	static public String GetOnUpgrade() 
	{
        String s = "DROP TABLE IF EXISTS " + TABLE;
 
        return s;
	}

	public ArrayList<Child> GetChildInfo(ArrayList<Child> pi_childs)
	{
		for (Child child : pi_childs)
		{
			Child info = GetChild(m_database, child.m_id);
			child.m_firstName = info.m_firstName;
			child.m_lastName = info.m_lastName;
			child.m_birthdate = info.m_birthdate;
			child.m_grade = info.m_grade;
			child.m_address = info.m_address;
			child.m_language = info.m_language;
			child.m_creationDate = info.m_creationDate;
			child.m_modificationDate = info.m_modificationDate;
			child.m_mondayInfo = info.m_mondayInfo;
			child.m_tuesdayInfo = info.m_tuesdayInfo;
			child.m_wednesdayInfo = info.m_wednesdayInfo;
			child.m_thursdayInfo = info.m_thursdayInfo;
			child.m_fridayInfo = info.m_fridayInfo;
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
	        
	        if ( (c != null) && (c.moveToFirst()) )
	        {
	        	child = FillupChild(c);
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        return child;
	}

	public Child FillupChild(Cursor c) throws ParseException 
	{
		Child child = new Child();
		child.m_id = c.getInt(c.getColumnIndex(KEY_ID));
		child.m_firstName = c.getString(c.getColumnIndex(KEY_FIRST_NAME));
		child.m_lastName = c.getString(c.getColumnIndex(KEY_LAST_NAME));
		child.m_birthdate = m_parser.parse(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));
		child.m_grade = E_GRADE.FromInt(c.getInt(c.getColumnIndex(KEY_GRADE_ID)));
		child.m_address.add(c.getString(c.getColumnIndex(KEY_ADDRESS_1)));
		child.m_address.add(c.getString(c.getColumnIndex(KEY_ADDRESS_2)));
		child.m_language = E_LANGUAGE.FromInt(c.getInt(c.getColumnIndex(KEY_LANGUAGE_ID)));
		child.m_creationDate = m_parser.parse(c.getString(c.getColumnIndex(KEY_CREATION_DATE)));
		child.m_modificationDate = m_parser.parse(c.getString(c.getColumnIndex(KEY_MODIFICATION_DATE)));
		child.m_mondayInfo = c.getString(c.getColumnIndex(KEY_MONDAY_INFO));
		child.m_tuesdayInfo = c.getString(c.getColumnIndex(KEY_TUESDAY_INFO));
		child.m_wednesdayInfo = c.getString(c.getColumnIndex(KEY_WEDNESDAY_INFO));
		child.m_thursdayInfo = c.getString(c.getColumnIndex(KEY_THURSDAY_INFO));
		child.m_fridayInfo = c.getString(c.getColumnIndex(KEY_FRIDAY_INFO));
		return child;
	}

	public void InsertChild(int pi_childId, 
							String pi_firstName, 
							String pi_lastName,
							String pi_birthdate,
							int pi_gradeId,
							String pi_address1, 
							String pi_address2, 
							int pi_languageId, 
							String pi_creationDate, 
							String pi_modificationDate,
							String pi_mondayInfo,
							String pi_tuesdayInfo,
							String pi_wednesdayInfo,
							String pi_thursdayInfo,
							String pi_fridayInfo) throws Exception
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ID, pi_childId);
		values.put(KEY_FIRST_NAME, pi_firstName);
		values.put(KEY_LAST_NAME, pi_lastName);
		values.put(KEY_BIRTHDATE, pi_birthdate);
		values.put(KEY_GRADE_ID, pi_gradeId);
		values.put(KEY_ADDRESS_1, pi_address1);
		values.put(KEY_ADDRESS_2, pi_address2);
		values.put(KEY_LANGUAGE_ID, pi_languageId);
		values.put(KEY_CREATION_DATE, pi_creationDate);
		values.put(KEY_MODIFICATION_DATE, pi_modificationDate);
		values.put(KEY_MONDAY_INFO, pi_mondayInfo);
		values.put(KEY_TUESDAY_INFO, pi_tuesdayInfo);
		values.put(KEY_WEDNESDAY_INFO, pi_wednesdayInfo);
		values.put(KEY_THURSDAY_INFO, pi_thursdayInfo);
		values.put(KEY_FRIDAY_INFO, pi_fridayInfo);
		
	    // 1. build the query
		Insert(values);
	}

	@Override
	protected String GetTableName() 
	{
		return TABLE;
	}

	public ArrayList<Child> GetAllChilds() 
	{
		ArrayList<Child> childs = new ArrayList<Child>();
		
		try
		{
	        // 1. build the query
	        String query = "SELECT * FROM " + TABLE;
	    	
	        // 2. get reference to writable DB
	        Cursor c = m_database.rawQuery(query, null);
	        
	        if (c.moveToFirst()) {
	            do {
	            	try
	            	{
	            		Child child = FillupChild(c);
	            		childs.add(child);
	            	}
	            	catch (Exception e) 
	            	{
						e.printStackTrace();
					}
	 
	            } while (c.moveToNext());
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        return childs;
	}
}
