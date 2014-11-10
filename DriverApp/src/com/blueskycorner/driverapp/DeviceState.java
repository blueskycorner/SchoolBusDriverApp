package com.blueskycorner.driverapp;

import android.content.Context;
import android.location.Location;

import com.blueskycorner.system.BatteryState;

public class DeviceState 
{
	private static final String ID_MESSAGE = "7";
	public BatteryState m_batteryState;
	public Location m_locationState;
	public String toSMS(Context pi_context) 
	{
		String s = ID_MESSAGE + DriverAppSmsMessage.SEPARATOR_STRING + 
				   DriverAppParamHelper.GetDeviceId(pi_context) + DriverAppSmsMessage.SEPARATOR_STRING +
				   Integer.toString((int)m_batteryState.percentage) + DriverAppSmsMessage.SEPARATOR_STRING +
				   m_batteryState.plugged + DriverAppSmsMessage.SEPARATOR_STRING +
				   Double.toString(m_locationState.getLatitude()) + DriverAppSmsMessage.SEPARATOR_STRING +
				   Double.toString(m_locationState.getLongitude()) + DriverAppSmsMessage.SEPARATOR_STRING +
				   Integer.toString((int)m_locationState.getAccuracy()) + DriverAppSmsMessage.SEPARATOR_STRING;
		return s;
	}
}
