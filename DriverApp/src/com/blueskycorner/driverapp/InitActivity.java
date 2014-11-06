package com.blueskycorner.driverapp;

import com.blueskycorner.mediaLib.NetworkManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class InitActivity extends Activity implements IBackEndManagerListener 
{
	private static final int MAIN_ACTIVITY = 0;
	private TextView m_tvInitState = null;
	private ProgressBar m_progressBar = null;
	private BackEndManager m_backEndManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		NetworkManager.GetInstance().SetContext(this);
		
		m_tvInitState = (TextView) findViewById(R.id.textViewInitState);
		m_progressBar = (ProgressBar) findViewById(R.id.progressBarInit);
		
		m_backEndManager = new BackEndManager(this);
		m_backEndManager.addListener(this);
		
		CheckDeviceInfo();
	}

	private void CheckDeviceInfo() 
	{
		if (DriverAppParamHelper.IsDeviceInfoOutdated(this) == true)
		{
			if (NetworkManager.GetInstance().IsNetworkAvailable() == true)
			{
				m_progressBar.setVisibility(View.VISIBLE);
				m_tvInitState.setText(getResources().getText(R.string.app_initialisation));
				
				m_backEndManager.launchDeviceInfoGrabing(com.blueskycorner.system.System.GetSimSerialNumber(this));
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.internet_connection_required_to_init_app)
					   .setTitle(R.string.warning)
				       .setCancelable(false)
				       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   dialog.dismiss();
				               CheckDeviceInfo();
				           }
				       })
				       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   android.os.Process.killProcess(android.os.Process.myPid());
			                   System.exit(1);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		else
		{
			CheckDbUpdate();
		}
	}
	
	private void CheckDbUpdate() 
	{
		if (DriverAppParamHelper.IsDbOutdated(this) == true)
		{
			if (NetworkManager.GetInstance().IsNetworkAvailable() == true)
			{
				m_progressBar.setVisibility(View.VISIBLE);
				m_tvInitState.setText(getResources().getText(R.string.db_update));
				m_backEndManager.launchDbModificationGrabing(DriverAppParamHelper.GetDeviceId(this));
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.internet_connection_required_to_init_app)
					   .setTitle(R.string.warning)
				       .setCancelable(false)
				       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   dialog.dismiss();
				               CheckDeviceInfo();
				           }
				       })
				       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   android.os.Process.killProcess(android.os.Process.myPid());
			                   System.exit(1);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		else
		{
			StartMainActivity();
		}
	}

	private void StartMainActivity() 
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivityForResult(intent, MAIN_ACTIVITY);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		switch (requestCode)
		{
			case MAIN_ACTIVITY:
			{
				System.exit(1);
				break;
			}
		}
    }

	@Override
	public void OnDeviceInfoGrabed(Boolean pi_bResult) 
	{
		DeviceInfo deviceInfo = m_backEndManager.getDeviceInfo();
		
		try
		{
			if (pi_bResult)
			{
				DriverAppParamHelper.SetLastDeviceInfoUpdate(this, System.currentTimeMillis());
				DriverAppParamHelper.SetDeviceId(this, deviceInfo.m_id);
				DriverAppParamHelper.SetDeviceGateway(this, deviceInfo.m_gateway);
				CheckDbUpdate();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.server_unavailable)
					   .setTitle(R.string.error)
				       .setCancelable(false)
				       .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   dialog.dismiss();
				               CheckDeviceInfo();
				           }
				       })
				       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   android.os.Process.killProcess(android.os.Process.myPid());
			                   System.exit(1);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void OnDbModificationGrabed(Boolean pi_bResult) 
	{
		try
		{
			if (pi_bResult)
			{
				DriverAppParamHelper.SetLastDBUpdateTime(this, System.currentTimeMillis());
				StartMainActivity();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.db_update_failed)
					   .setTitle(R.string.error)
				       .setCancelable(false)
				       .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   dialog.dismiss();
				               CheckDeviceInfo();
				           }
				       })
				       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				       {
				           public void onClick(DialogInterface dialog, int id) 
				           {
				        	   android.os.Process.killProcess(android.os.Process.myPid());
			                   System.exit(1);
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
