package com.blueskycorner.driverapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;

import com.blueskycorner.system.System;

public class DataGrabber implements LocationListener 
{
	private DeviceState m_ds = null;
	private IDeviceDataListener m_listener = null;
	
	public void GetDeviceState(final Context pi_context,
							   IDeviceDataListener pi_listener)
	{
		m_listener = pi_listener;
		m_ds = new DeviceState();
		m_ds.m_batteryState = System.GetBatteryState(pi_context);
//		System.GetLocationState(pi_context, this);
		Location location = System.GetLastLocation(pi_context);
		if (m_listener != null)
		{			
			m_ds.m_locationState = location;
			m_listener.onDeviceDataChanged(m_ds);
			m_listener = null;
		}
		
//		AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>() 
//		{
//			@Override
//			protected Void doInBackground(Void... params) 
//			{
//				System.GetLocationState(pi_context, DataGrabber.this);
//				return null;
//			}
//		};
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		if (m_listener != null)
		{			
			m_ds.m_locationState = location;
			m_listener.onDeviceDataChanged(m_ds);
			m_listener = null;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
