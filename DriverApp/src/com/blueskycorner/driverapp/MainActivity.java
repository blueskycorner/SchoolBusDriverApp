package com.blueskycorner.driverapp;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends FragmentActivity implements DriverAppCommunicator, OnCheckedChangeListener, IMessageListener
{
	private Trip m_trip;
	DriverAppFragment m_currentFragment = null;
	private MessageManager m_messageManager = null;
	private ToggleButton m_buttonEmergency = null;

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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void TripStarted(Trip pi_trip)
	{
		m_trip = pi_trip;
		LaunchTripFragment(m_trip);
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
		LaunchTripFragment(m_trip);
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
	}

	@Override
	public void onMessageReceived(DriverAppMessage pi_message) 
	{
		if (m_trip != null)
		{
			Child c = m_trip.GetChild(pi_message.m_fromId);
			if (c != null)
			{
				c.m_state = E_CHILD_STATE.SKIPPED;
				m_currentFragment.RefreshState(c);
			}
			else
			{
				// TODO reply to parent
				Toast.makeText(this, R.string.child_is_not_part_of_current_trip, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
