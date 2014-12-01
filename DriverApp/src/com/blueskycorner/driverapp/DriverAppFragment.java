package com.blueskycorner.driverapp;

import java.util.HashMap;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public abstract class DriverAppFragment extends Fragment
{
	protected Trip m_trip = null;
	protected String m_fragmentName = null;
	protected Activity m_activity;
	protected IDriverAppCommunicator m_comm = null;
	
	public abstract void BackPressed();

	public abstract ViewGroup GetViewGroup();
	
	public abstract void RefreshState(Child pi_child);
	
	public abstract void UpdateUI();
	
	public String GetName()
	{
		return m_fragmentName;
	}

	public Trip GetTrip() 
	{
		return m_trip;
	}

	public void SetTrip(Trip pi_trip) 
	{
		m_trip = pi_trip;
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		m_activity = activity;
		m_comm = (IDriverAppCommunicator) activity;
		
	}
	
	public void SetEnabled(boolean b)
	{
		ViewGroup l = GetViewGroup();
		enableDisableView(l,b);
	}
	
	private void enableDisableView(View view, boolean enabled) {
	    view.setEnabled(enabled);

	    if ( view instanceof ViewGroup ) {
	        ViewGroup group = (ViewGroup)view;

	        for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
	            enableDisableView(group.getChildAt(idx), enabled);
	        }
	    }
	}
}
