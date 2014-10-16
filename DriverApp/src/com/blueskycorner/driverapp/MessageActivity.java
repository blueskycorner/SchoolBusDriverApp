package com.blueskycorner.driverapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
		
		m_from = (TextView) findViewById(R.id.textViewFrom);
		m_cellNumber = (TextView) findViewById(R.id.textViewCellNumber);
		m_message = (TextView) findViewById(R.id.textViewMessage);
		m_buttonOK = (Button) findViewById(R.id.buttonMessageOk);
		
		DriverAppMessage message = getIntent().getParcelableExtra(MESSAGE);
		m_cellNumber.setText(message.m_cellNumer);
		m_message.setText(message.m_text);
		
		int color = 0;
		String sFrom = "";
		switch (message.m_from) 
		{
			case FROM_PARENT:
			{
				color = DriverAppParamHelper.GetParentBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_parent);
				break;
			}
			case FROM_SCHOOL:
			{
				color = DriverAppParamHelper.GetSchoolBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_school);
				break;
			}
			case FROM_DRIVER_COMPANY:
			{
				color = DriverAppParamHelper.GetCompanyBackgroudColor(this);
				sFrom = (String) getResources().getText(R.string.from_company);
				break;
			}
			case FROM_UNKNOWN:
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
