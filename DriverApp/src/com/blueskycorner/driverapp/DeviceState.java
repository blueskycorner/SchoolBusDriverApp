package com.blueskycorner.driverapp;

import android.location.Location;

import com.blueskycorner.system.BatteryState;

public class DeviceState 
{
	public BatteryState m_batteryState;
	public Location m_locationState;
	public String toSMS() 
	{
		String s = DriverAppParamHelper.GetInstance().GetDeviceId() + SmsSender.SEPARATOR_STRING +
				   Integer.toString((int)m_batteryState.percentage) + SmsSender.SEPARATOR_STRING +
				   m_batteryState.plugged + SmsSender.SEPARATOR_STRING +
				   Double.toString(m_locationState.getLatitude()) + SmsSender.SEPARATOR_STRING +
				   Double.toString(m_locationState.getLongitude()) + SmsSender.SEPARATOR_STRING +
				   Integer.toString((int)m_locationState.getAccuracy()) + SmsSender.SEPARATOR_STRING;
		return s;
	}
}
