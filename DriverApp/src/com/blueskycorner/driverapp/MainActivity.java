package com.blueskycorner.driverapp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements IDriverAppCommunicator, OnCheckedChangeListener, IMessageListener, IDeviceDataListener
{
	private Trip m_trip;
	DriverAppFragment m_currentFragment = null;
	private MessageManager m_messageManager = null;
	private ToggleButton m_buttonEmergency = null;
	private TimerManager m_timerManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		
		DataManager.GetInstance().Init();

		m_messageManager = new MessageManager(this);
		m_messageManager.AddMessageListener(this);
		
		m_buttonEmergency = (ToggleButton) findViewById(R.id.toggleButtonEmmergency);
		m_buttonEmergency .setOnCheckedChangeListener(this);
		
		LaunchTripChoiceFragment();
		
		m_timerManager = new TimerManager(this);
		m_timerManager.StartTimer();
	}

	private void LaunchTripChoiceFragment() 
	{
		TripChoiceFragment f1 = (TripChoiceFragment) getSupportFragmentManager().findFragmentByTag(TripChoiceFragment.NAME);
		
		if (f1 == null)
		{
			f1 = new TripChoiceFragment();
		}
		
		m_currentFragment = f1;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.main_content_frame, f1, TripChoiceFragment.NAME);
//	        ft.add(R.id.main_content_frame, f2, TripFragment.NAME);
        ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			startActivity(new Intent(this, SettingsActivity.class));
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void TripStarted(Trip pi_trip)
	{
		m_trip = pi_trip;
		SmsSender.SendTripStarted(this, m_trip.m_id);
		
		try
		{
			LaunchTripFragment(m_trip);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private void setMobileDataEnabled(Context context, boolean enabled) 
	{
		try
		{
		    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    final Class conmanClass = Class.forName(conman.getClass().getName());
		    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		    iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);
	
		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void LaunchTripFragment(Trip pi_trip) 
	{
		TripFragment f = (TripFragment) getSupportFragmentManager().findFragmentByTag(TripFragment.NAME);
		
		if (f == null)
		{
			f = new TripFragment();
		}
		
		m_currentFragment = f;
		f.UpdateTrip(pi_trip);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.main_content_frame, f, TripFragment.NAME);
        ft.commit();
	}

	@Override
	public void ChildSelected(Child pi_child) 
	{
		ChildFragment f = (ChildFragment) getSupportFragmentManager().findFragmentByTag(ChildFragment.NAME);
		
		if (f == null)
		{
			f = new ChildFragment();
		}
		
		m_currentFragment = f;
		f.SetChild(pi_child);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.main_content_frame, f, ChildFragment.NAME);
        ft.commit();

	}

	@Override
	public void childStateUpdated(Child pi_child) 
	{
		if ( (pi_child.m_state != E_CHILD_STATE.STATE_WAITING) && (pi_child.m_state != E_CHILD_STATE.STATE_MISSING) )
		{
			SmsSender.SendChildState(pi_child);
		}
		if (pi_child.m_state != E_CHILD_STATE.STATE_ON_THE_WAY)
		{			
			LaunchTripFragment(m_trip);
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		if (m_currentFragment != null)
		{
			m_currentFragment.BackPressed();
		}
		else
		{
			super.onBackPressed();
		}
	}

	@Override
	public void TripFinished()
	{
		SmsSender.SendTripFinished(m_trip.m_id);
		LaunchTripChoiceFragment();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) 
	{
		m_messageManager.OnMessageAcknowledge(arg0);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1)
	{
		m_currentFragment.SetEnabled(!arg1);
		SmsSender.SendEmergency(arg1);
	}

	@Override
	public void onMessageReceived(DriverAppSmsMessage pi_message) 
	{
		switch (pi_message.GetType())
		{
			case 0:
			{
				if (m_trip != null)
				{
					Child c = m_trip.GetChild(pi_message.GetChildId());
					if (c != null)
					{
						c.m_state = E_CHILD_STATE.STATE_SKIPPED;
						m_currentFragment.RefreshState(c);
					}
					else
					{
						// TODO reply to parent
						Toast.makeText(this, R.string.child_is_not_part_of_current_trip, Toast.LENGTH_SHORT).show();
					}
				}

			}
			case 3:
			{
				DataGrabber dg = new DataGrabber();
				dg.GetDeviceState(this, this);
			}
		}
	}

	@Override
	public void onDeviceDataChanged(DeviceState pi_deviceState) 
	{
		SmsSender.SendDeviceState(this, pi_deviceState);
	}

}
