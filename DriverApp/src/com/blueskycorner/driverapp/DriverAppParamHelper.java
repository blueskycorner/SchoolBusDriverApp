package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	private static final String AUTO_UPDATE_PERIOD = "AUTO_UPDATE_PERIOD";
	private static final String LAST_DEVICE_INFO_UPDATE = "LAST_DEVICE_INFO_UPDATE";
	private static final String DEVICE_ID = "DEVICE_ID";
	private static final String AUTO_UPDATE_CHECK_HOUR = "AUTO_UPDATE_CHECK_HOUR";
	private static final String MAX_UPDATE_ATTEMPTS = "MAX_UPDATE_ATTEMPTS";
	private static final String REAL_SMS = "REAL_SMS";

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

	
	public static int GetAutoUpdatePeriod(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(AUTO_UPDATE_PERIOD, 24*60*60*1000);
		return val;
	}
	
	public static void SetAutoUpdatePeriod(Context pi_context, int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(AUTO_UPDATE_PERIOD, m_period).commit();
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
		long l = DriverAppParamHelper.GetLastDBUpdateTime(pi_context);
		if (DriverAppParamHelper.HasDayChanged(l))
		{
			b = true;
		}
		return true;
	}

	private static boolean HasDayChanged(long l) 
	{
		Boolean b = false;
		
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(System.currentTimeMillis());
		int currentMonth = c1.get(Calendar.MONTH);
		int currentDay = c1.get(Calendar.DAY_OF_MONTH);
		int currentHour = c1.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c1.get(Calendar.MINUTE);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(l);
		int lastMonth = c2.get(Calendar.MONTH);
		int lastHour = c2.get(Calendar.HOUR_OF_DAY);
		int lastDay = c2.get(Calendar.DAY_OF_MONTH);
		int lastMinute = c2.get(Calendar.MINUTE);
		
		if ( (currentMonth != lastMonth) || 
			 (currentDay != lastDay) /*|| 
			 (currentHour != lastHour) ||
			 (currentMinute != lastMinute)*/ )
		{
			b = true;
		}
		
		return b;
	}

	public static boolean IsDeviceInfoOutdated(Context pi_context) 
	{
		Boolean b = false;
		long l = DriverAppParamHelper.GetLastDeviceInfoUpdate(pi_context);
		if (DriverAppParamHelper.HasDayChanged(l))
		{
			b = true;
		}
		return b;
	}

	public static int GetAutoUpdateCheckHour(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(AUTO_UPDATE_CHECK_HOUR, 2);
		return val;
	}

	public static void SetAutoUpdateCheckHour(Context pi_context, int pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(AUTO_UPDATE_CHECK_HOUR, pi_time).commit();
		DataSynchronyzer.CancelAlarm(pi_context);
		DataSynchronyzer.SetAlarm(pi_context);
	}

	public static int GetMaxUpdateAttempts(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		int val = sharedPref.getInt(MAX_UPDATE_ATTEMPTS, 3);
		return val;
	}

	public static void SetMaxUpdateAttempts(Context pi_context, int pi_max) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putInt(MAX_UPDATE_ATTEMPTS, pi_max).commit();
	}

	public static boolean GetRealSms(Context pi_context) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		boolean val = sharedPref.getBoolean(REAL_SMS, false);
		return val;
	}
	
	public static void SetRealSms(Context pi_context, boolean pi_realSms) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(pi_context);
		sharedPref.edit().putBoolean(REAL_SMS, pi_realSms).commit();
	}
}
