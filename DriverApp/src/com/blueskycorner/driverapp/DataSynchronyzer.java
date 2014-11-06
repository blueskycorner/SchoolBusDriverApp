package com.blueskycorner.driverapp;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class DataSynchronyzer extends BroadcastReceiver
{
	@Override
	public void onReceive(Context pi_context, Intent pi_intent) 
	{
//		DataManager.GetInstance().Update(pi_context, false);
		DriverAppParamHelper.SetLastDBUpdateTime(pi_context, System.currentTimeMillis());
//		Intent i = new Intent(pi_context, InitActivity.class);  
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        pi_context.startActivity(i);
	}
	
	static public void SetAlarm(Context context)
    {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MINUTE, 2);
//		calendar.set(Calendar.HOUR_OF_DAY, 15);
//		calendar.set(Calendar.MINUTE, 18);
		
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, DataSynchronyzer.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+120*1000, DriverAppParamHelper.GetDBUpdatePeriod(context), pi); // Millisec * Second * Minute
    }

    static public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, DataSynchronyzer.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
