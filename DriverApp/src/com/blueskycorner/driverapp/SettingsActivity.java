package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener 
{
	private TextView m_appVersion = null;
	private TextView m_lastDeviceUpdate = null;
	private TextView m_lastDBUpdate = null;
	private TextView m_deviceID = null;
	private TextView m_gateway = null;
	private EditText m_autoUpdateHour = null;
	private EditText m_AutoUpdatePeriod = null;
	private EditText m_checkPeriod = null;
	private Button m_buttonSchool = null;
	
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
			
			Date lastDeviceUpdate = DriverAppParamHelper.Long2Date(DriverAppParamHelper.GetInstance().GetLastDeviceInfoUpdate());
			String sLastDeviceUpdate = DriverAppParamHelper.Date2String(lastDeviceUpdate);
			m_lastDeviceUpdate.setText(sLastDeviceUpdate);
			
			Date lastDbUpdate = DriverAppParamHelper.Long2Date(DriverAppParamHelper.GetInstance().GetLastDBUpdateTime());
			String sLastDbUpdate = DriverAppParamHelper.Date2String(lastDbUpdate);
			m_lastDBUpdate.setText(sLastDbUpdate);
			
			m_deviceID.setText(Integer.toString(DriverAppParamHelper.GetInstance().GetDeviceId()));
			
			m_gateway.setText(DriverAppParamHelper.GetInstance().GetDeviceGateway());
			
			m_autoUpdateHour.setText(Integer.toString(DriverAppParamHelper.GetInstance().GetAutoUpdateCheckHour()));
			
			m_AutoUpdatePeriod.setText(Integer.toString(DriverAppParamHelper.GetInstance().GetAutoUpdatePeriod()/(60*1000)));
			
			m_checkPeriod.setText(Integer.toString(DriverAppParamHelper.GetInstance().GetCheckTimerPeriod()/(60*1000)));
			
			m_buttonSchool = (Button) findViewById(R.id.buttonSchool);	
			
			m_buttonSchool.setOnClickListener(this);
			School s = DataManager.GetInstance().getSchool(DriverAppParamHelper.GetInstance().GetLastSchoolId());
			SetSchoolButtonText(s);
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
	}	
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.buttonSchool:
			{
				final ArrayList<School> schools = DataManager.GetInstance().GetSchools();
				String[] schoolNames = GetSchoolNames(schools);
				final int currentIndex = GetCurrentSchoolIndex(schools);
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
		    	builder.setTitle(R.string.school);
		    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		     	{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				});
		     	builder.setSingleChoiceItems(schoolNames,currentIndex, new DialogInterface.OnClickListener() 
		     	{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						School school = schools.get(which);
						SetSchoolButtonText(school);
						DriverAppParamHelper.GetInstance().SetLastSchoolId(school.m_id);
						dialog.dismiss();
					}
				});
		    	builder.show();
				break;
			}
		}
	}
	
	private int GetCurrentSchoolIndex(ArrayList<School> pi_schools)
	{
		int index = -1;
		int currentSchoolId = DriverAppParamHelper.GetInstance().GetLastSchoolId();
		if (currentSchoolId != -1)
		{
			for (int i=0; i<pi_schools.size(); i++) 
			{
				if (pi_schools.get(i).m_id == currentSchoolId)
				{
					index = i;
				}
			}
		}
		return index;
	}
	
	private String[] GetSchoolNames(ArrayList<School> pi_schools) 
	{
		String[] schoolNames = new String[pi_schools.size()];
		for (int i=0; i<pi_schools.size(); i++)
		{
			schoolNames[i] = pi_schools.get(i).m_name;
		}
		return schoolNames;
	}

	private void SetSchoolButtonText(School pi_school) 
	{
		String schoolName = "";
		if (pi_school != null)
		{
			schoolName = pi_school.m_name;
		}
		else
		{
			schoolName = getResources().getString(R.string.choose_one);
		}
		String text = getString(R.string.school) + " :  " + schoolName;
		m_buttonSchool.setText(text);
	}


	public void Gateway(View v)
	{
		String s = m_gateway.getText().toString();
		DriverAppParamHelper.GetInstance().SetDeviceGateway(s);
	}

	public void Hour(View v)
	{
		String s = m_autoUpdateHour.getText().toString();
		DriverAppParamHelper.GetInstance().SetAutoUpdateCheckHour(Integer.parseInt(s));
		DataSynchronyzer.CancelAlarm(this);
		DataSynchronyzer.SetAlarm(this);
	}

	public void Period(View v)
	{
		String s = m_AutoUpdatePeriod.getText().toString();
		DriverAppParamHelper.GetInstance().SetAutoUpdatePeriod(Integer.parseInt(s) * 60 * 1000);
		DataSynchronyzer.CancelAlarm(this);
		DataSynchronyzer.SetAlarm(this);
	}

	public void Check(View v)
	{
		String s = m_checkPeriod.getText().toString();
		DriverAppParamHelper.GetInstance().SetCheckTimerPeriod(Integer.parseInt(s) * 60 * 1000);
	}
}
