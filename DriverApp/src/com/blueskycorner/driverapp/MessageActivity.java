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
	private TextView m_message = null;
	private Button m_buttonAck = null;

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
		
		m_message = (TextView) findViewById(R.id.textViewMessage);
		m_buttonAck = (Button) findViewById(R.id.buttonMessageOk);
		
		UserMessage userMessage = getIntent().getParcelableExtra(MESSAGE);
		m_message.setText(userMessage.m_message);
		
		int color = 0;
		switch (userMessage.m_sender) 
		{
			case SENDER_PARENT:
			{
				color = DriverAppParamHelper.GetInstance().GetParentBackgroudColor();
				break;
			}
			case SENDER_SCHOOL:
			{
				color = DriverAppParamHelper.GetInstance().GetSchoolBackgroudColor();
				break;
			}
			case SENDER_DRIVER_COMPANY:
			{
				color = DriverAppParamHelper.GetInstance().GetCompanyBackgroudColor();
				break;
			}
		}
		View someView = findViewById(R.id.message_activity);
		View root = someView.getRootView();
		root.setBackgroundColor(color);
				
		switch (userMessage.m_action)
		{
			case ACTION_ACK:
			{
				m_buttonAck.setVisibility(View.VISIBLE);
				m_buttonAck.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						finish();
					}
				});
			}
		}
	}
}
