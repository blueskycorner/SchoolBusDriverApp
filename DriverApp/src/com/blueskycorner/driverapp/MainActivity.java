package com.blueskycorner.driverapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements DriverAppCommunicator {

	private Trip m_trip;
	BackPressed m_currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DataManager.GetInstance().Init();
		
		LaunchTripChoiceFragment();
	}

	private void LaunchTripChoiceFragment() {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	public void StartTrip(Trip pi_trip)
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
}
