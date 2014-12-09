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
    private static final String KEY_GRADE = "grade";
    private static final String KEY_ADDRESS_1 = "address1";
    private static final String KEY_ADDRESS_2 = "address2";
    private static final String KEY_ADDRESS_3 = "address3";
    private static final String KEY_ADDRESS_DATE_1 = "address1_date";
    private static final String KEY_ADDRESS_DATE_2 = "address2_date";
    private static final String KEY_ADDRESS_DATE_3 = "address3_date";
    private static final String KEY_DIARY_INFO = "diary_info";
    private static final String KEY_USEFUL_INFO = "useful_info";
    
    private SimpleDateFormat m_parser = new SimpleDateFormat("yyyy/MM/dd");
    
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
			child.m_addressDate = info.m_addressDate;
			child.m_diaryInfo = info.m_diaryInfo;
			child.m_usefulInfo = info.m_usefulInfo;
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
		child.m_grade = c.getString(c.getColumnIndex(KEY_GRADE));
		child.m_address.add(c.getString(c.getColumnIndex(KEY_ADDRESS_1)));
		child.m_address.add(c.getString(c.getColumnIndex(KEY_ADDRESS_2)));
		child.m_address.add(c.getString(c.getColumnIndex(KEY_ADDRESS_3)));
		child.m_addressDate.add(m_parser.parse(c.getString(c.getColumnIndex(KEY_ADDRESS_DATE_1))));
		child.m_addressDate.add(m_parser.parse(c.getString(c.getColumnIndex(KEY_ADDRESS_DATE_2))));
		child.m_addressDate.add(m_parser.parse(c.getString(c.getColumnIndex(KEY_ADDRESS_DATE_3))));
		child.m_diaryInfo = c.getString(c.getColumnIndex(KEY_DIARY_INFO));
		child.m_usefulInfo = c.getString(c.getColumnIndex(KEY_USEFUL_INFO));
		return child;
	}

	public void InsertChild(int pi_childId, 
							String pi_firstName, 
							String pi_lastName,
							String pi_birthdate,
							String pi_grade,
							String pi_address1,
							String pi_address2,
							String pi_address3,
							String pi_addressDate1,
							String pi_addressDate2,
							String pi_addressDate3, 
							String pi_diaryInfo,
							String pi_usefulInfo) throws Exception
	{
		ContentValues values = new ContentValues();
		values.put(KEY_ID, pi_childId);
		values.put(KEY_FIRST_NAME, pi_firstName);
		values.put(KEY_LAST_NAME, pi_lastName);
		values.put(KEY_BIRTHDATE, pi_birthdate);
		values.put(KEY_GRADE, pi_grade);
		values.put(KEY_ADDRESS_1, pi_address1);
		values.put(KEY_ADDRESS_2, pi_address2);
		values.put(KEY_ADDRESS_3, pi_address3);
		values.put(KEY_ADDRESS_DATE_1, pi_addressDate1);
		values.put(KEY_ADDRESS_DATE_2, pi_addressDate2);
		values.put(KEY_ADDRESS_DATE_3, pi_addressDate3);
		values.put(KEY_DIARY_INFO, pi_diaryInfo);
		values.put(KEY_USEFUL_INFO, pi_usefulInfo);
		
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
