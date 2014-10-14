package com.blueskycorner.driverapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DriverAppParamHelper 
{

	private static final String LAST_SCHOOL_ID = "LAST_SCHOOL_ID";
	private static final String LAST_TRIP_ID = "LAST_TRIP_ID";

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

}
