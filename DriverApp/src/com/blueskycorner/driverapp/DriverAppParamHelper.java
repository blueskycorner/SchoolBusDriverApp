package com.blueskycorner.driverapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class DriverAppParamHelper 
{
	private Context m_context = null;
	private static DriverAppParamHelper m_driverParamHelper = new DriverAppParamHelper();
	
	private ArrayList<IDriverAppParamHelperListener> m_listeners = new ArrayList<IDriverAppParamHelperListener>();
	
	private static SimpleDateFormat m_dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private static final String LAST_SCHOOL_ID = "LAST_SCHOOL_ID";
	private static final String LAST_TRIP_ID = "LAST_TRIP_ID";
	private static final String PARENT_BACKGROUND_COLOR = "PARENT_BACKGROUND_COLOR";
	private static final String SCHOOL_BACKGROUND_COLOR = "SCHOOL_BACKGROUND_COLOR";
	private static final String COMPANY_BACKGROUND_COLOR = "COMPANY_BACKGROUND_COLOR";
	private static final String UNKNOWN_BACKGROUND_COLOR = "UNKNOWN_BACKGROUND_COLOR";
	private static final String DEVICE_GETWAY = "GETWAY_NUMBER";
	private static final String CHECK_TIMER_PERIOD = "CHECK_TIMER_PERIOD";
	public static final String LAST_DB_UPDATE_TIME = "LAST_DB_UPDATE_TIME";
	private static final String AUTO_UPDATE_PERIOD = "AUTO_UPDATE_PERIOD";
	private static final String LAST_DEVICE_INFO_UPDATE = "LAST_DEVICE_INFO_UPDATE";
	private static final String DEVICE_ID = "DEVICE_ID";
	private static final String AUTO_UPDATE_CHECK_HOUR = "AUTO_UPDATE_CHECK_HOUR";
	private static final String MAX_UPDATE_ATTEMPTS = "MAX_UPDATE_ATTEMPTS";
	private static final String REAL_SMS = "REAL_SMS";
	private static final String TRIP_FILTER_BY_DAY = "FILTER_BY_DAY";
	private static final String TRIP_FILTER_BY_TIME = "TRIP_FILTER_BY_TIME";
	
	public static final int NO_SCHOOL_ID = -2;
	public static final int NO_DEVICE_ID = -2;
	
	public static final int SPECIAL_TRIP_HOME_ID = -1;
	public static final int SPECIAL_TRIP_SCHOOL_ID = -2;
	public static final int NO_TRIP_ID = -3;
	private static final String DEBUG_FORCE_DB_UPDATE = null;
	private static final String DEBUG_FORCE_DEVICE_UPDATE = null;
	
	public static DriverAppParamHelper GetInstance() 
    {
	   return m_driverParamHelper;
    }

	public void SetContext(Context pi_context)
	{
		m_context = pi_context;
	}	
	
	public void AddListener(IDriverAppParamHelperListener pi_listener) 
	{
		if (m_listeners.contains(pi_listener) == false)
		{
			m_listeners.add(pi_listener);
		}
	}    

	public void RemoveListener(IDriverAppParamHelperListener pi_listener) 
	{
		if (m_listeners.contains(pi_listener) == false)
		{
			m_listeners.remove(pi_listener);
		}
	}    
	
	private void BroadcastParamChange(String pi_paramName)
	{
		for (IDriverAppParamHelperListener l : m_listeners) 
		{
			l.OnParamValueChange(pi_paramName);
		}
	}

	
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

	public int GetLastSchoolId() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(LAST_SCHOOL_ID, NO_SCHOOL_ID);
		return val;
	}

	public int GetLastTripId() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(LAST_TRIP_ID, NO_TRIP_ID);
		return val;
	}

	public void SetLastSchoolId(int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(LAST_SCHOOL_ID, m_id).commit();
	}
	
	public void SetLastTripId(int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(LAST_TRIP_ID, m_id).commit();		
	}
	
	public String GetDeviceGateway()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		String sGatewayNumber = sharedPref.getString(DEVICE_GETWAY, "+639157912584");
		return sGatewayNumber;
	}

	public void SetDeviceGateway(String m_gateway) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putString(DEVICE_GETWAY, m_gateway).commit();
	}

	public int GetParentBackgroudColor() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(PARENT_BACKGROUND_COLOR, Color.YELLOW);
		return val;
	}

	public int GetSchoolBackgroudColor() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(SCHOOL_BACKGROUND_COLOR, Color.RED);
		return val;
	}

	public int GetCompanyBackgroudColor() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(COMPANY_BACKGROUND_COLOR, Color.GREEN);
		return val;
	}

	public int GetUnknownBackgroudColor() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(UNKNOWN_BACKGROUND_COLOR, Color.WHITE);
		return val;
	}

	public int GetCheckTimerPeriod() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(CHECK_TIMER_PERIOD, 1000*60*5);
		return val;
	}
	
	public void SetCheckTimerPeriod(int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(CHECK_TIMER_PERIOD, m_period).commit();
	}

	
	public int GetAutoUpdatePeriod() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(AUTO_UPDATE_PERIOD, 24*60*60*1000);
		return val;
	}
	
	public void SetAutoUpdatePeriod(int m_period) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(AUTO_UPDATE_PERIOD, m_period).commit();
	}

	public long GetLastDBUpdateTime() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		long val = sharedPref.getLong(LAST_DB_UPDATE_TIME, 0);
		return val;
	}
	
	public void SetLastDBUpdateTime(long pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putLong(LAST_DB_UPDATE_TIME, pi_time).commit();
		BroadcastParamChange(LAST_DB_UPDATE_TIME);
	}

	public void SetLastDeviceInfoUpdate(long pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putLong(LAST_DEVICE_INFO_UPDATE, pi_time).commit();
	}

	public long GetLastDeviceInfoUpdate() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		long time = sharedPref.getLong(LAST_DEVICE_INFO_UPDATE, 0);	
		return time;
	}

	public void SetDeviceId(int m_id) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(DEVICE_ID, m_id).commit();
	}

	public int GetDeviceId() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(DEVICE_ID, NO_DEVICE_ID);	
		return val;
	}

	public boolean IsDbOutdated() 
	{
		Boolean b = false;
		long l = GetLastDBUpdateTime();
		if (DriverAppParamHelper.HasDayChanged(l))
		{
			b = true;
		}
		if (GetDebugForceDbUpdate() == true)
		{
			b = false;
		}
		return b;
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

	public boolean IsDeviceInfoOutdated() 
	{
		Boolean b = false;
		long l = GetLastDeviceInfoUpdate();
		if (DriverAppParamHelper.HasDayChanged(l))
		{
			b = true;
		}
		if (GetDebugForceDeviceUpdate() == true)
		{
			b = false;
		}
		return b;
	}

	public int GetAutoUpdateCheckHour() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(AUTO_UPDATE_CHECK_HOUR, 2);
		return val;
	}

	public void SetAutoUpdateCheckHour(int pi_time) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(AUTO_UPDATE_CHECK_HOUR, pi_time).commit();
		DataSynchronyzer.CancelAlarm(m_context);
		DataSynchronyzer.SetAlarm(m_context);
	}

	public int GetMaxUpdateAttempts() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		int val = sharedPref.getInt(MAX_UPDATE_ATTEMPTS, 3);
		return val;
	}

	public void SetMaxUpdateAttempts(int pi_max) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putInt(MAX_UPDATE_ATTEMPTS, pi_max).commit();
	}

	public boolean GetRealSms() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		boolean val = sharedPref.getBoolean(REAL_SMS, false);
		return val;
	}
	
	public void SetRealSms(boolean pi_realSms) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putBoolean(REAL_SMS, pi_realSms).commit();
	}

	public boolean GetTripFilterByDay()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		boolean val = sharedPref.getBoolean(TRIP_FILTER_BY_DAY, true);
		return val;
	}
	
	public void SetFilterByDay(boolean pi_filterByDay) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putBoolean(TRIP_FILTER_BY_DAY, pi_filterByDay).commit();
	}

	public boolean GetTripFilterByTime()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		boolean val = sharedPref.getBoolean(TRIP_FILTER_BY_TIME, true);
		return val;
	}
	
	public void SetFilterByTime(boolean pi_filterByDay) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putBoolean(TRIP_FILTER_BY_TIME, pi_filterByDay).commit();
	}
	


	private boolean GetDebugForceDbUpdate() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		boolean val = sharedPref.getBoolean(DEBUG_FORCE_DB_UPDATE, false);
		return val;
	}

	private boolean GetDebugForceDeviceUpdate() 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		boolean val = sharedPref.getBoolean(DEBUG_FORCE_DEVICE_UPDATE, false);
		return val;
	}
	
	public void SetDebugForceDbUpdate(boolean pi_debugForceDbUpdate) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putBoolean(DEBUG_FORCE_DB_UPDATE, pi_debugForceDbUpdate).commit();
	}

	public void SetDebugForceDeviceUpdate(boolean pi_debugForceDeviceUpdate) 
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(m_context);
		sharedPref.edit().putBoolean(DEBUG_FORCE_DEVICE_UPDATE, pi_debugForceDeviceUpdate).commit();
	}

}
