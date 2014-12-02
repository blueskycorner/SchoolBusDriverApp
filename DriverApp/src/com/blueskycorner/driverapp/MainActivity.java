package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements IDriverAppCommunicator, OnCheckedChangeListener, IDriverAppParamHelperListener
{
	private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
	DriverAppFragment m_currentFragment = null;
	private MessageManager m_messageManager = null;
	private ToggleButton m_buttonEmergency = null;
	private Button m_buttonAddChild = null;
	
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

		m_messageManager = new MessageManager(this,this);
		
		m_buttonEmergency = (ToggleButton) findViewById(R.id.toggleButtonEmmergency);
		m_buttonEmergency .setOnCheckedChangeListener(this);
		
		m_buttonAddChild = (Button) findViewById(R.id.buttonAddChild);
		m_buttonAddChild.setEnabled(false);
		
		InitFragments(savedInstanceState);
		
		DriverAppParamHelper.GetInstance().AddListener(this);
		
		TimerManager.GetInstance().SetContext(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		
		outState.putString(CURRENT_FRAGMENT, m_currentFragment.GetName());
	}

	private void InitFragments(Bundle pi_savedInstanceState) 
	{
		m_tripChoiceFragment = new TripChoiceFragment();
		m_tripFragment = new TripFragment();
		m_childFragment = new ChildFragment();
		
		Trip trip = null;
		if (pi_savedInstanceState != null)
		{
			trip = DataManager.GetInstance().GetCurrentTrip();
			
			String sFragmentName = pi_savedInstanceState.getString(CURRENT_FRAGMENT);
			if (sFragmentName.equals(TripChoiceFragment.NAME))
			{
				m_currentFragment = m_tripChoiceFragment;
			}
			else if (sFragmentName.equals(TripFragment.NAME))
			{
				m_currentFragment = m_tripFragment;
			}
			else if (sFragmentName.equals(ChildFragment.NAME))
			{
				m_currentFragment = m_childFragment;
			}
		}
		else
		{
			trip = LoadLastTrip();
			
			m_currentFragment = m_tripChoiceFragment;
		}

		m_tripChoiceFragment.SetTrip(trip);
		m_tripFragment.SetTrip(trip);
		m_childFragment.SetTrip(trip);
		
		if (trip != null)
		{
			m_buttonAddChild.setEnabled(true);
		}
		
		ActivateFragment(m_currentFragment);
	}

	public Trip LoadLastTrip() 
	{
		Trip trip = null;
		int tripId = DriverAppParamHelper.GetInstance().GetLastTripId();
		
		if (tripId != DriverAppParamHelper.NO_TRIP_ID)
		{
			trip = DataManager.GetInstance().getTrip(tripId);
			ArrayList<Child> list = DataManager.GetInstance().GetChilds(trip.m_id);
			trip.Init(list);
			DataManager.GetInstance().SetCurrentTrip(trip);
		}
		return trip;
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
	public void TripSelected(Trip pi_trip) 
	{
		m_tripFragment.SetTrip(pi_trip);
		m_childFragment.SetTrip(pi_trip);
		DataManager.GetInstance().SetCurrentTrip(pi_trip);
		
		if (pi_trip != null)
		{
			m_buttonAddChild.setEnabled(true);
		}
	}

	@Override
	public void TripStarted()
	{
		Trip trip = DataManager.GetInstance().GetCurrentTrip();
		trip.m_isStarted = true;
		if (trip.m_isReturn == false)
		{
			SmsSender.SendTripSchoolStarted(this, trip.m_id);
		}
		else
		{
			SmsSender.SendTripHomeStarted(this, trip);
		}
		
		try
		{
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
		DataManager.GetInstance().GetCurrentTrip().SetCurrentChild(pi_child);
        ActivateFragment(m_childFragment);
	}

	private Trip GetTrip() 
	{
		return DataManager.GetInstance().GetCurrentTrip();
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
		GetTrip().Init();
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
		m_buttonAddChild.setEnabled(!arg1);
		SmsSender.SendEmergency(this, arg1);
	}

	public void AddChild(View v)
	{
		AddChildDialog d = new AddChildDialog(this, this, DataManager.GetInstance().GetCurrentTrip().m_childs);
		d.show();
	}

	@Override
	public void ChildAdded(Child pi_child) 
	{
		if (pi_child != null)
		{
			Trip trip = DataManager.GetInstance().GetCurrentTrip();
			if (trip != null)
			{
				pi_child.m_isPresent = true;
				trip.AddChild(pi_child);
			}
			m_currentFragment.UpdateUI();
		}
	}

	@Override
	public void childStateImplicitlyUpdated(Child pi_child) 
	{
		m_currentFragment.RefreshState(pi_child);
	}

	@Override
	public void OnParamValueChange(String pi_paramName)
	{
		if (pi_paramName == DriverAppParamHelper.LAST_DB_UPDATE_TIME)
		{
			if (DriverAppParamHelper.GetInstance().GetLastTripId() != DriverAppParamHelper.NO_TRIP_ID)
			{
				Trip trip = DataManager.GetInstance().GetCurrentTrip();
				ArrayList<Child> list = DataManager.GetInstance().GetChilds(trip.m_id);
				trip.Init(list);
			}
			else
			{
				DataManager.GetInstance().SetCurrentTrip(null);
				m_tripChoiceFragment.SetTrip(null);
				m_tripFragment.SetTrip(null);
				m_childFragment.SetTrip(null);

			}

			m_currentFragment.UpdateUI();
		}
	}
}
