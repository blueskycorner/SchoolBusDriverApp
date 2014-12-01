package com.blueskycorner.driverapp;

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
	private Context m_context = null;
	private Handler m_handler = null;
	private Runnable m_checkRunnable = null;
	
	public TimerManager(Context pi_context) 
	{
		m_context = pi_context;
		m_handler = new Handler();
		m_checkRunnable = new Runnable() 
		{
			@Override
			public void run() 
			{
				CheckBattery();
			}
		};
	}
	
	public void StartTimer()
	{
		m_handler.post(m_checkRunnable);
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
