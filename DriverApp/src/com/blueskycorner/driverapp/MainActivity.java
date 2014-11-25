package com.blueskycorner.driverapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements IDriverAppCommunicator, OnCheckedChangeListener, IMessageListener, IDeviceDataListener
{
	DriverAppFragment m_currentFragment = null;
	private MessageManager m_messageManager = null;
	private ToggleButton m_buttonEmergency = null;
	private TimerManager m_timerManager = null;
	
	private TripChoiceFragment m_tripChoiceFragment = null;
	private TripFragment m_tripFragment = null;
	private ChildFragment m_childFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

		m_messageManager = new MessageManager(this);
		m_messageManager.AddMessageListener(this);
		
		m_buttonEmergency = (ToggleButton) findViewById(R.id.toggleButtonEmmergency);
		m_buttonEmergency .setOnCheckedChangeListener(this);
		
		InitFragments(savedInstanceState);
		
		m_timerManager = new TimerManager(this);
		m_timerManager.StartTimer();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		Trip trip = m_tripChoiceFragment.GetTrip();
		Child child = m_childFragment.GetChild();
		String currentFragment = m_currentFragment.GetName();
	}

	private void InitFragments(Bundle pi_savedInstanceState) 
	{
		m_tripChoiceFragment = new TripChoiceFragment();
		m_tripFragment = new TripFragment();
		m_childFragment = new ChildFragment();
		
		if (pi_savedInstanceState != null)
		{
			// TODO get name of current fragment
			// TODO get trip and child info
		}
		else
		{
			m_tripChoiceFragment.Init();
			ActivateFragment(m_tripChoiceFragment);
		}
	}

	private void ActivateFragment(DriverAppFragment pi_fragment)
	{
		if (pi_fragment != null)
		{
			m_currentFragment = pi_fragment;
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        ft.replace(R.id.main_content_frame, pi_fragment, pi_fragment.GetName()).commit();
		}
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
		if (pi_trip.m_isReturn == false)
		{
			SmsSender.SendTripSchoolStarted(this, pi_trip.m_id);
		}
		else
		{
			SmsSender.SendTripHomeStarted(this, pi_trip);
		}
		
		try
		{
			m_tripFragment.UpdateTrip(pi_trip);
			ActivateFragment(m_tripFragment);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void ChildSelected(Child pi_child) 
	{
        m_childFragment.SetChild(pi_child);
        m_childFragment.SetReturn(GetTrip().m_isReturn);
        ActivateFragment(m_childFragment);
	}

	private Trip GetTrip() 
	{
		return m_tripChoiceFragment.GetTrip();
	}

	@Override
	public void childStateUpdated(Child pi_child) 
	{
		switch (pi_child.m_state) 
		{
			case STATE_ON_THE_WAY_STARTED:
			{
				SmsSender.SendOntheWayStarted(this, pi_child.m_id);
				break;
			}
			case STATE_ON_THE_WAY_FINISHED:
			{
				SmsSender.SendOntheWayFinished(this, pi_child.m_id);
				break;
			}
			case STATE_ON_THE_WAY_CANCELED:
			{
				SmsSender.SendOntheWayCanceled(this, pi_child.m_id);
				break;
			}
			case STATE_MISSING:
			case STATE_WAITING:
			{
				// Nothing to do
				break;
			}
		}
		if (pi_child.m_state != E_CHILD_STATE.STATE_ON_THE_WAY_STARTED)
		{
			m_tripFragment.UpdateUI();
			ActivateFragment(m_tripFragment);
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
		if (GetTrip().m_isCancel == false)
		{
			SmsSender.SendTripFinished(this, GetTrip().m_id);
		}
		else
		{
			SmsSender.SendTripCanceled(this, GetTrip().m_id);
		}
		m_tripChoiceFragment.GetTrip().Init();
		ActivateFragment(m_tripChoiceFragment);
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
		SmsSender.SendEmergency(this, arg1);
	}

	@Override
	public void onMessageReceived(DriverAppSmsMessage pi_message) 
	{
		switch (pi_message.GetType())
		{
			case 0:
			{
//				if (m_trip != null)
//				{
//					Child c = m_trip.GetChild(pi_message.GetChildId());
//					if (c != null)
//					{
//						c.m_state = E_CHILD_STATE.STATE_SKIPPED;
//						m_currentFragment.RefreshState(c);
//					}
//					else
//					{
//						// TODO reply to parent
//						Toast.makeText(this, R.string.child_is_not_part_of_current_trip, Toast.LENGTH_SHORT).show();
//					}
//				}

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
