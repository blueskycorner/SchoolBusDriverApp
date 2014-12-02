package com.blueskycorner.driverapp;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.blueskycorner.system.BatteryState;
import com.blueskycorner.system.System;

import android.app.AlertDialog;
import android.app.ApplicationErrorReport.BatteryInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

public class TimerManager 
{
	private static TimerManager m_timerManager = new TimerManager();
	
	private Context m_context = null;
	private Handler m_handler = null;
	private Runnable m_checkRunnable = null;
	private Timer m_updateTimer = null;
	
	public static TimerManager GetInstance() 
    {
	   return m_timerManager;
    }
	
	public void SetContext(Context pi_context)
	{
		m_context = pi_context;
	}

	public TimerManager() 
	{
		m_handler = new Handler();
		m_checkRunnable = new Runnable() 
		{
			@Override
			public void run() 
			{
				CheckBattery();
			}
		};
		m_handler.post(m_checkRunnable);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(java.lang.System.currentTimeMillis());
		if (calendar.get(Calendar.HOUR_OF_DAY) >= DriverAppParamHelper.GetInstance().GetAutoUpdateCheckHour())
		{
			calendar.add(Calendar.HOUR_OF_DAY, 24);
		}
		calendar.set(Calendar.HOUR_OF_DAY, DriverAppParamHelper.GetInstance().GetAutoUpdateCheckHour());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date d = new Date(calendar.getTimeInMillis());

		m_updateTimer = new Timer();
//		m_updateTimer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() 
//			{
//				DataSynchronyzer ds = new DataSynchronyzer();
//				ds.Synchronize(m_context, E_SYNCHRONISATION_MODE.MODE_AUTO, false);
//			}
//		}, d, DriverAppParamHelper.GetInstance().GetAutoUpdatePeriod());
	}
	
	private void CheckGps() 
	{
		if (System.IsGpsActivated(m_context) == false)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		    builder.setMessage(m_context.getResources().getText(R.string.enable_gps))
		           .setCancelable(false)
		           .setPositiveButton(m_context.getResources().getText(R.string.open_localisation_parameter), new DialogInterface.OnClickListener() 
		           	  {
		               public void onClick(final DialogInterface dialog, final int id) 
		               {
		                   m_context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		                   m_handler.postDelayed(m_checkRunnable, DriverAppParamHelper.GetInstance().GetCheckTimerPeriod());
		               }
		           })
		           .setNegativeButton(m_context.getResources().getText(R.string.later), new DialogInterface.OnClickListener() 
		           {
		               public void onClick(final DialogInterface dialog, final int id) 
		               {
		                    dialog.cancel();
		                    m_handler.postDelayed(m_checkRunnable, DriverAppParamHelper.GetInstance().GetCheckTimerPeriod());
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}
		else
		{
			m_handler.postDelayed(m_checkRunnable, DriverAppParamHelper.GetInstance().GetCheckTimerPeriod());
		}
	}
	
	private void CheckBattery() 
	{
		BatteryState info = System.GetBatteryState(m_context);
		if (info.plugged == 0)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		    builder.setMessage(m_context.getResources().getText(R.string.plug_charger))
		           .setCancelable(false)
		           .setPositiveButton(m_context.getResources().getText(R.string.ok), new DialogInterface.OnClickListener() 
		           	  {
		               public void onClick(final DialogInterface dialog, final int id) 
		               {
		            	   CheckGps();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}
		else
		{
			CheckGps();
		}
	}


}
