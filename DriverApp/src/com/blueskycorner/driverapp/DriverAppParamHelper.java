package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class DriverAppParamHelper 
{
	private static SimpleDateFormat m_dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private static final String LAST_SCHOOL_ID = "LAST_SCHOOL_ID";
	private static final String LAST_TRIP_ID = "LAST_TRIP_ID";
	private static final String PARENT_BACKGROUND_COLOR = "PARENT_BACKGROUND_COLOR";
	private static final String SCHOOL_BACKGROUND_COLOR = "SCHOOL_BACKGROUND_COLOR";
	private static final String COMPANY_BACKGROUND_COLOR = "COMPANY_BACKGROUND_COLOR";
	private static final String UNKNOWN_BACKGROUND_COLOR = "UNKNOWN_BACKGROUND_COLOR";
	private static final String DEVICE_GETWAY = "GETWAY_NUMBER";
	private static final String CHECK_TIMER_PERIOD = "CHECK_TIMER_PERIOD";
	private static final String LAST_DB_UPDATE_TIME = "LAST_DB_UPDATE_TIME";
	private static final String DB_UPDATE_PERIOD = "DB_UPDATE_PERIOD";
	private static final String LAST_DEVICE_INFO_UPDATE = "LAST_DEVICE_INFO_UPDATE";
	private static final String DEVICE_ID = "DEVICE_ID";
	private static final String DEVICE_UPDATE_PERIOD = "DEVICE_UPDATE_PERIOD";

	public static Date Long2Date(Long pi_date)
	{
		Date d = null;
		try 
		{
			d = new Date(pi_date);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return d;
	}
	
	public static String Date2String(Date pi_date)
	{
		String s = null;
		try 
		{
			s = m_dateParser.format(pi_date);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return s;
	}
	
	public static Date String2Date(String pi_date)
	{
		Date d = null;
		try 
		{
			d = m_dateParser.parse(pi_date);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return d;
	}

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
	
	public static String GetDeviceGateway(Context pi_context)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		String sGatewayNumber = sharedPref.getString(DEVICE_GETWAY, "+639157912584");
		return sGatewayNumber;
	}

	public static void SetDeviceGateway(Context pi_context, String m_gateway) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putString(DEVICE_GETWAY, m_gateway).commit();
	}

	public static int GetParentBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(PARENT_BACKGROUND_COLOR, Color.YELLOW);
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
		int val = sharedPref.getInt(COMPANY_BACKGROUND_COLOR, Color.GREEN);
		return val;
	}

	public static int GetUnknownBackgroudColor(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(UNKNOWN_BACKGROUND_COLOR, Color.WHITE);
		return val;
	}

	public static int GetCheckTimerPeriod(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(CHECK_TIMER_PERIOD, 1000*60*5);
		return val;
	}
	
	public static void SetCheckTimerPeriod(Context pi_context, int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(CHECK_TIMER_PERIOD, m_period).commit();
	}

	
	public static int GetDBUpdatePeriod(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(DB_UPDATE_PERIOD, 60*1000);
		return val;
	}
	
	public static void SetDBUpdatePeriod(Context pi_context, int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(DB_UPDATE_PERIOD, m_period).commit();
	}

	public static long GetLastDBUpdateTime(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		long val = sharedPref.getLong(LAST_DB_UPDATE_TIME, 0);
		return val;
	}
	
	public static void SetLastDBUpdateTime(Context pi_context, long pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putLong(LAST_DB_UPDATE_TIME, pi_time).commit();		
	}

	public static void SetLastDeviceInfoUpdate(Context pi_context, long pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putLong(LAST_DEVICE_INFO_UPDATE, pi_time).commit();
	}

	public static long GetLastDeviceInfoUpdate(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		long time = sharedPref.getLong(LAST_DEVICE_INFO_UPDATE, 0);	
		return time;
	}

	public static void SetDeviceId(Context pi_context, int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(DEVICE_ID, m_id).commit();
	}

	public static int GetDeviceId(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(DEVICE_ID, -1);	
		return val;
	}

	public static boolean IsDbOutdated(Context pi_context) 
	{
		Boolean b = false;
		if (DriverAppParamHelper.GetLastDBUpdateTime(pi_context) + DriverAppParamHelper.GetDBUpdatePeriod(pi_context) < System.currentTimeMillis())
		{
			b = true;
		}
		return b;
	}

	public static boolean IsDeviceInfoOutdated(Context pi_context) 
	{
		Boolean b = false;
		if (DriverAppParamHelper.GetLastDeviceInfoUpdate(pi_context) + DriverAppParamHelper.GetDeviceUpdatePeriod(pi_context) < System.currentTimeMillis())
		{
			b = true;
		}
		return b;
	}

	public static int GetDeviceUpdatePeriod(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(DEVICE_UPDATE_PERIOD, 60*1000);
		return val;
	}
	
	public static void SetDeviceUpdatePeriod(Context pi_context, int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(DEVICE_UPDATE_PERIOD, m_period).commit();
	}


}
