package com.blueskycorner.driverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) 
		{   
			DataSynchronyzer.SetAlarm(context);
			
			Log.println(0, "ALARM", "TESTSTTTTTTTTTTTTTTTTTTT");
			
			Intent i = new Intent(context, InitActivity.class);  
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(i);
		}
	}

}
