package com.blueskycorner.driverapp;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MessageActivity extends Activity 
{
	public static final String MESSAGE = "MESSAGE";
	private TextView m_from = null;
	private TextView m_cellNumber = null;
	private TextView m_message = null;
	private Button m_buttonOK = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		
		Window wind = this.getWindow();
		wind.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	    wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	    wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		m_from = (TextView) findViewById(R.id.textViewFrom);
		m_message = (TextView) findViewById(R.id.textViewMessage);
		m_buttonOK = (Button) findViewById(R.id.buttonMessageOk);
		
		DriverAppSmsMessage message = getIntent().getParcelableExtra(MESSAGE);
		m_message.setText(message.GetText());
		
		int color = 0;
		String sFrom = "";
		switch (message.GetType()) 
		{
			case 0:
			case 1:
			{
				color = DriverAppParamHelper.GetParentBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_parent);
				break;
			}
			case 2:
			{
				color = DriverAppParamHelper.GetSchoolBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_school);
				break;
			}
			case 4:
			{
				color = DriverAppParamHelper.GetCompanyBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_company);
				break;
			}
			default:
			{
				color = DriverAppParamHelper.GetUnknownBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_unknown);
				break;
			}
		}
		View someView = findViewById(R.id.message_activity);
		View root = someView.getRootView();
		root.setBackgroundColor(color);
		
		m_from.setText(sFrom);
		
		m_buttonOK.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}
}
