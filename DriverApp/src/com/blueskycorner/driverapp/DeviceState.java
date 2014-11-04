package com.blueskycorner.driverapp;

import android.location.Location;

import com.blueskycorner.system.BatteryState;

public class DeviceState 
{
	public BatteryState m_batteryState;
	public Location m_locationState;
	public String toSMS() 
	{
		String s = m_locationState.getLatitude() + "," + m_locationState.getLongitude();
		return s;
	}
}
