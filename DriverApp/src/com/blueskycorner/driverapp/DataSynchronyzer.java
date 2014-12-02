package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import com.blueskycorner.mediaLib.IMobileDataListener;
import com.blueskycorner.mediaLib.NetworkManager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

public class DataSynchronyzer extends BroadcastReceiver implements IBackEndManagerListener, IMobileDataListener 
{
	private static final int CHECK_DEVICE_INFO = 0;
	private static final int CHECK_DB_UPDATE = 1;
	private static final int ON_DEVICE_INFO_GRABBED = 2;
	private static final int ON_DB_MODIFCATION_GRABBED = 3;
	
	private BackEndManager m_backEndManager = null;
	private Context m_context = null;
	private ArrayList<ISynchronizerListener> m_listeners = new ArrayList<ISynchronizerListener>();
	private static ReentrantLock m_lock = new ReentrantLock();
	private E_SYNCHRONISATION_MODE m_mode = E_SYNCHRONISATION_MODE.MODE_AUTO;
	private int m_attemptNumber = 0;
	private Boolean m_bForce = false;
	
	Handler m_handler = new Handler();
	
	static public void SetAlarm(Context context)
    {
		DriverAppParamHelper.GetInstance().SetContext(context);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (calendar.get(Calendar.HOUR_OF_DAY) >= DriverAppParamHelper.GetInstance().GetAutoUpdateCheckHour())
		{
			calendar.add(Calendar.HOUR_OF_DAY, 24);
		}
//		calendar.set(Calendar.HOUR_OF_DAY, DriverAppParamHelper.GetInstance().GetAutoUpdateCheckHour());
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		calendar.set(Calendar.MINUTE, 42);
		calendar.set(Calendar.SECOND, 0);
		
		
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, DataSynchronyzer.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), DriverAppParamHelper.GetInstance().GetAutoUpdatePeriod(), pi); // Millisec * Second * Minute
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*5, pi); // Millisec * Second * Minute
//        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, DriverAppParamHelper.GetAutoUpdatePeriod(context), pi); // Millisec * Second * Minute
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

    public void Synchronize(Context pi_context, E_SYNCHRONISATION_MODE pi_mode, Boolean pi_bForce)
    {
    	m_lock.lock();
    	m_mode = pi_mode;
    	m_context = pi_context;
    	m_bForce = pi_bForce;
    	m_backEndManager = new BackEndManager();
    	m_backEndManager.addListener(this);
    	
    	CheckDeviceInfo();
    }
    
    @Override
    public void onReceive(Context pi_context, Intent pi_intent) 
    {
    	Synchronize(pi_context, E_SYNCHRONISATION_MODE.MODE_AUTO, true);
    }
    
	private void CheckDeviceInfo() 
	{
		if ( (DriverAppParamHelper.GetInstance().IsDeviceInfoOutdated() == true) || (m_bForce == true) ) 
		{
			BroadcastStepChanged(E_INIT_STEP.STEP_DEVICE_UPDATE);
			if (NetworkManager.GetInstance().IsNetworkAvailable() == false)
			{
				NetworkManager.SetMobileDataEnabled(m_context, true, this, CHECK_DEVICE_INFO, true);
			}
			else
			{
				m_backEndManager.launchDeviceInfoGrabing(com.blueskycorner.system.System.GetSimSerialNumber(m_context));
			}
		}
		else
		{
			CheckDbUpdate();
		}
	}
	
	private void CheckDbUpdate() 
	{
		if ( (DriverAppParamHelper.GetInstance().IsDbOutdated() == true) || (m_bForce == true) ) 
		{
			BroadcastStepChanged(E_INIT_STEP.STEP_DB_UPDATE);
			if (NetworkManager.GetInstance().IsNetworkAvailable() == false)
			{
				NetworkManager.SetMobileDataEnabled(m_context, true, this, CHECK_DB_UPDATE, true);
			}
			else
			{
				m_backEndManager.launchDbModificationGrabing(DriverAppParamHelper.GetInstance().GetDeviceId());
			}
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
			if (NetworkManager.GetInstance().IsMobileDataEnabled() == true)
			{
				NetworkManager.SetMobileDataEnabled(m_context, false, this, ON_DEVICE_INFO_GRABBED, pi_bResult);
			}
			else
			{
				if (pi_bResult)
				{
					DriverAppParamHelper.GetInstance().SetLastDeviceInfoUpdate(System.currentTimeMillis());
					DriverAppParamHelper.GetInstance().SetDeviceId(deviceInfo.m_id);
					DriverAppParamHelper.GetInstance().SetDeviceGateway(deviceInfo.m_gateway);
					CheckDbUpdate();
				}
				else
				{
					if ( (m_mode == E_SYNCHRONISATION_MODE.MODE_STARTUP) || (m_mode == E_SYNCHRONISATION_MODE.MODE_MANUALY) )
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
						builder.setMessage(R.string.device_update_failed)
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
						        	   CheckDbUpdate();
//						        	   android.os.Process.killProcess(android.os.Process.myPid());
//					                   System.exit(1);
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
					}
					else
					{
						if (m_attemptNumber < DriverAppParamHelper.GetInstance().GetMaxUpdateAttempts())
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
			if (NetworkManager.GetInstance().IsMobileDataEnabled() == true)
			{
				NetworkManager.SetMobileDataEnabled(m_context, false, this, ON_DB_MODIFCATION_GRABBED, pi_bResult);
			}
			else
			{
				if (pi_bResult)
				{
					DriverAppParamHelper.GetInstance().SetLastDBUpdateTime(System.currentTimeMillis());
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
	//					        	   android.os.Process.killProcess(android.os.Process.myPid());
	//				                   System.exit(1);
						        	   dialog.dismiss();
						        	   BroadcastDataSynchronized();
						           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
					}
					else
					{
						if (m_attemptNumber < DriverAppParamHelper.GetInstance().GetMaxUpdateAttempts())
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

	@Override
	public void OnMobileDataUpdated(int pi_id, boolean pi_bData) 
	{
		switch (pi_id) 
		{
			case CHECK_DEVICE_INFO:
			{
				CheckDeviceInfo();
				break;
			}
			case CHECK_DB_UPDATE:
			{
				CheckDbUpdate();
				break;
			}
			case ON_DEVICE_INFO_GRABBED:
			{
				OnDeviceInfoGrabed(pi_bData);
				break;
			}
			case ON_DB_MODIFCATION_GRABBED:
			{
				OnDbModificationGrabed(pi_bData);
				break;
			}
		}
	}
}
