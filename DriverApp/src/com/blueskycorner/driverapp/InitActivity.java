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

public class InitActivity extends Activity implements ISynchronizerListener
{
	private static final int MAIN_ACTIVITY = 0;
	private TextView m_tvInitState = null;
	private ProgressBar m_progressBar = null;
	private DataSynchronyzer m_dataSynchronizer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		NetworkManager.GetInstance().SetContext(this);
		
		m_tvInitState = (TextView) findViewById(R.id.textViewInitState);
		m_progressBar = (ProgressBar) findViewById(R.id.progressBarInit);
		
		m_dataSynchronizer = new DataSynchronyzer();
		m_dataSynchronizer.addListener(this);
		
		m_progressBar.setVisibility(View.VISIBLE);
		m_dataSynchronizer.Synchronize(this, E_SYNCHRONISATION_MODE.MODE_STARTUP);
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
	public void OnStepChanged(E_INIT_STEP pi_step) 
	{
		switch (pi_step)
		{
			case STEP_DEVICE_UPDATE:
			{
				m_tvInitState.setText(getResources().getText(R.string.app_initialisation));
				break;
			}
			case STEP_DB_UPDATE:
			{
				m_tvInitState.setText(getResources().getText(R.string.db_update));
				break;
			}
		}
	}

	@Override
	public void OnDataSynchronised()
	{
		m_progressBar.setVisibility(View.INVISIBLE);
		StartMainActivity();
	}
}
