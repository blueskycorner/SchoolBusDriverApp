package com.blueskycorner.driverapp;

import java.util.Date;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity 
{
	private TextView m_appVersion = null;
	private TextView m_lastDeviceUpdate = null;
	private TextView m_lastDBUpdate = null;
	private TextView m_deviceID = null;
	private TextView m_gateway = null;
	private EditText m_autoUpdateHour = null;
	private EditText m_AutoUpdatePeriod = null;
	private EditText m_checkPeriod = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		m_appVersion = (TextView) findViewById(R.id.textViewAppVersionData);
		m_lastDeviceUpdate = (TextView) findViewById(R.id.textViewLastDeviceInfoUpdateData);
		m_lastDBUpdate = (TextView) findViewById(R.id.textViewLastDbUpdateData);
		m_deviceID = (TextView) findViewById(R.id.textViewDeviceIdData);
		m_gateway = (TextView) findViewById(R.id.textViewGatewayData);
		m_autoUpdateHour = (EditText) findViewById(R.id.editTextAutoUpdateHourData);
		m_AutoUpdatePeriod = (EditText) findViewById(R.id.editTextAutoUpdatePeriodData);
		m_checkPeriod = (EditText) findViewById(R.id.editTextDeviceCheckPeriodData);
		
		try 
		{
			m_appVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
			
			Date lastDeviceUpdate = DriverAppParamHelper.Long2Date(DriverAppParamHelper.GetLastDeviceInfoUpdate(this));
			String sLastDeviceUpdate = DriverAppParamHelper.Date2String(lastDeviceUpdate);
			m_lastDeviceUpdate.setText(sLastDeviceUpdate);
			
			Date lastDbUpdate = DriverAppParamHelper.Long2Date(DriverAppParamHelper.GetLastDBUpdateTime(this));
			String sLastDbUpdate = DriverAppParamHelper.Date2String(lastDbUpdate);
			m_lastDBUpdate.setText(sLastDbUpdate);
			
			m_deviceID.setText(Integer.toString(DriverAppParamHelper.GetDeviceId(this)));
			
			m_gateway.setText(DriverAppParamHelper.GetDeviceGateway(this));
			
			m_autoUpdateHour.setText(Integer.toString(DriverAppParamHelper.GetAutoUpdateCheckHour(this)));
			
			m_AutoUpdatePeriod.setText(Integer.toString(DriverAppParamHelper.GetAutoUpdatePeriod(this)/(60*1000)));
			
			m_checkPeriod.setText(Integer.toString(DriverAppParamHelper.GetCheckTimerPeriod(this)/(60*1000)));
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	public void Gateway(View v)
	{
		String s = m_gateway.getText().toString();
		DriverAppParamHelper.SetDeviceGateway(this, s);
	}

	public void Hour(View v)
	{
		String s = m_autoUpdateHour.getText().toString();
		DriverAppParamHelper.SetAutoUpdateCheckHour(this, Integer.parseInt(s));
		DataSynchronyzer.CancelAlarm(this);
		DataSynchronyzer.SetAlarm(this);
	}

	public void Period(View v)
	{
		String s = m_AutoUpdatePeriod.getText().toString();
		DriverAppParamHelper.SetAutoUpdatePeriod(this, Integer.parseInt(s) * 60 * 1000);
		DataSynchronyzer.CancelAlarm(this);
		DataSynchronyzer.SetAlarm(this);
	}

	public void Check(View v)
	{
		String s = m_checkPeriod.getText().toString();
		DriverAppParamHelper.SetCheckTimerPeriod(this, Integer.parseInt(s) * 60 * 1000);
	}
}
