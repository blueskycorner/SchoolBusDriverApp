package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import com.blueskycorner.mediaLib.NetworkManager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;

public class DataSynchronyzer extends BroadcastReceiver implements IBackEndManagerListener 
{
	private BackEndManager m_backEndManager = null;
	private Context m_context = null;
	private ArrayList<ISynchronizerListener> m_listeners = new ArrayList<ISynchronizerListener>();
	private ReentrantLock m_lock = new ReentrantLock();
	private E_SYNCHRONISATION_MODE m_mode = E_SYNCHRONISATION_MODE.MODE_AUTO;
	private int m_attemptNumber = 0;
	
	static public void SetAlarm(Context context)
    {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, DriverAppParamHelper.GetAutoUpdateCheckHour(context));
		
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, DataSynchronyzer.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), DriverAppParamHelper.GetAutoUpdatePeriod(context), pi); // Millisec * Second * Minute
    }

    static public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, DataSynchronyzer.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
    public void addListener(ISynchronizerListener listener)
    {
	    if (listener != null)
	    {
	    	m_listeners.add(listener);
    	}
    }
   
    public void removeListener(ISynchronizerListener listener)
    {
	    if (listener != null)
	    {
	    	m_listeners.remove(listener);
	    }
    }

    public void Synchronize(Context pi_context, E_SYNCHRONISATION_MODE pi_mode)
    {
    	m_lock.lock();
    	m_mode = pi_mode;
    	m_context = pi_context;
    	m_backEndManager = new BackEndManager(m_context);
    	m_backEndManager.addListener(this);
    	
    	CheckDeviceInfo();
    }
    
    @Override
    public void onReceive(Context pi_context, Intent pi_intent) 
    {
    	Synchronize(pi_context, E_SYNCHRONISATION_MODE.MODE_AUTO);
    }
    
	private void CheckDeviceInfo() 
	{
		if (DriverAppParamHelper.IsDeviceInfoOutdated(m_context) == true)
		{
			if (NetworkManager.GetInstance().IsNetworkAvailable() == false)
			{
				//TODO
			}
			BroadcastStepChanged(E_INIT_STEP.STEP_DEVICE_UPDATE);
			
			m_backEndManager.launchDeviceInfoGrabing(com.blueskycorner.system.System.GetSimSerialNumber(m_context));
		}
		else
		{
			CheckDbUpdate();
		}
	}
	
	private void CheckDbUpdate() 
	{
		if (DriverAppParamHelper.IsDbOutdated(m_context) == true)
		{
			if (NetworkManager.GetInstance().IsNetworkAvailable() == false)
			{
				// TODO
			}
			BroadcastStepChanged(E_INIT_STEP.STEP_DB_UPDATE);
			
			m_backEndManager.launchDbModificationGrabing(DriverAppParamHelper.GetDeviceId(m_context));
		}
		else
		{
			BroadcastDataSynchronized();
		}
	}

	private void BroadcastStepChanged(E_INIT_STEP pi_step) 
	{
		for (ISynchronizerListener o : m_listeners)
		{
		    o.OnStepChanged(pi_step);
		}
	}

	@Override
	public void OnDeviceInfoGrabed(Boolean pi_bResult) 
	{
		DeviceInfo deviceInfo = m_backEndManager.getDeviceInfo();
		
		try
		{
			if (pi_bResult)
			{
				DriverAppParamHelper.SetLastDeviceInfoUpdate(m_context, System.currentTimeMillis());
				DriverAppParamHelper.SetDeviceId(m_context, deviceInfo.m_id);
				DriverAppParamHelper.SetDeviceGateway(m_context, deviceInfo.m_gateway);
				CheckDbUpdate();
			}
			else
			{
				if ( (m_mode == E_SYNCHRONISATION_MODE.MODE_STARTUP) || (m_mode == E_SYNCHRONISATION_MODE.MODE_MANUALY) )
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
					builder.setMessage(R.string.server_unavailable)
						   .setTitle(R.string.error)
					       .setCancelable(false)
					       .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() 
					       {
					           public void onClick(DialogInterface dialog, int id) 
					           {
					        	   dialog.dismiss();
					               CheckDeviceInfo();
					           }
					       })
					       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
					       {
					           public void onClick(DialogInterface dialog, int id) 
					           {
					        	   android.os.Process.killProcess(android.os.Process.myPid());
				                   System.exit(1);
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
				else
				{
					if (m_attemptNumber < DriverAppParamHelper.GetMaxUpdateAttempts(m_context))
					{
						m_attemptNumber ++;
						CheckDeviceInfo();
					}
					else
					{
						m_attemptNumber = 0;
						CheckDbUpdate();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void OnDbModificationGrabed(Boolean pi_bResult) 
	{
		try
		{
			if (pi_bResult)
			{
				DriverAppParamHelper.SetLastDBUpdateTime(m_context, System.currentTimeMillis());
				BroadcastDataSynchronized();
			}
			else
			{
				if ( (m_mode == E_SYNCHRONISATION_MODE.MODE_STARTUP) || (m_mode == E_SYNCHRONISATION_MODE.MODE_MANUALY) )
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
					builder.setMessage(R.string.db_update_failed)
						   .setTitle(R.string.error)
					       .setCancelable(false)
					       .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() 
					       {
					           public void onClick(DialogInterface dialog, int id) 
					           {
					        	   dialog.dismiss();
					        	   CheckDbUpdate();
					           }
					       })
					       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
					       {
					           public void onClick(DialogInterface dialog, int id) 
					           {
					        	   android.os.Process.killProcess(android.os.Process.myPid());
				                   System.exit(1);
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
				else
				{
					if (m_attemptNumber < DriverAppParamHelper.GetMaxUpdateAttempts(m_context))
					{
						m_attemptNumber ++;
						CheckDbUpdate();
					}
					else
					{
						m_attemptNumber = 0;
						BroadcastDataSynchronized();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void BroadcastDataSynchronized() 
	{
		for (ISynchronizerListener o : m_listeners)
		{
		    o.OnDataSynchronised();
		}
		
		m_lock.unlock();
	}
}
