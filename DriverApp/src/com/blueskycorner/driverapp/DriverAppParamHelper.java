package com.blueskycorner.driverapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class DriverAppParamHelper 
{

	private static final String LAST_SCHOOL_ID = "LAST_SCHOOL_ID";
	private static final String LAST_TRIP_ID = "LAST_TRIP_ID";
	private static final String PARENT_BACKGROUND_COLOR = "PARENT_BACKGROUND_COLOR";
	private static final String SCHOOL_BACKGROUND_COLOR = "SCHOOL_BACKGROUND_COLOR";
	private static final String COMPANY_BACKGROUND_COLOR = "COMPANY_BACKGROUND_COLOR";
	private static final String UNKNOWN_BACKGROUND_COLOR = "UNKNOWN_BACKGROUND_COLOR";

	public static int GetLastSchoolId(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(LAST_SCHOOL_ID, -1);
		return val;
	}

	public static int GetLastTripId(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(LAST_TRIP_ID, -1);
		return val;
	}

	public static void SetLastSchoolId(Context pi_context, int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(LAST_SCHOOL_ID, m_id).commit();
	}
	
	public static void SetLastTripId(Context pi_context, int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(LAST_TRIP_ID, m_id).commit();		
	}

	public static int GetParentBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(SCHOOL_BACKGROUND_COLOR, Color.YELLOW);
		return val;
	}

	public static int GetSchoolBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(SCHOOL_BACKGROUND_COLOR, Color.RED);
		return val;
	}

	public static int GetCompanyBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(SCHOOL_BACKGROUND_COLOR, Color.GREEN);
		return val;
	}

	public static int GetUnknownBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(SCHOOL_BACKGROUND_COLOR, Color.WHITE);
		return val;
	}

}
