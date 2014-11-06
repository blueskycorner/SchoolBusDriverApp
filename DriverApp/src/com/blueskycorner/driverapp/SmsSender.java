package com.blueskycorner.driverapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsSender 
{
	public static void SendDeviceState(Context pi_context, DeviceState pi_deviceState)
	{
		String sMessage = pi_deviceState.toSMS();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(DriverAppParamHelper.GetDeviceGateway(pi_context), null, sMessage, null, null);
	}
	
	public static void SendTripStarted(final Context pi_context, int m_id) 
	{
		SmsManager sms = SmsManager.getDefault();
        try
        {
        	String sMessage = "TripId:" + m_id + ";Status:0;";
//        	String SENT = "SMS_SENT";
//            String DELIVERED = "SMS_DELIVERED";
//
//            PendingIntent sentPI = PendingIntent.getBroadcast(pi_context, 0,
//                new Intent(SENT), 0);
//
//            PendingIntent deliveredPI = PendingIntent.getBroadcast(pi_context, 0,
//                new Intent(DELIVERED), 0);
//
//            //---when the SMS has been sent---
//            pi_context.registerReceiver(new BroadcastReceiver(){
//                @Override
//                public void onReceive(Context arg0, Intent arg1) {
//                    switch (getResultCode())
//                    {
//                        case Activity.RESULT_OK:
//                            Toast.makeText(pi_context, "SMS sent", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                            Toast.makeText(pi_context, "Generic failure", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                        case SmsManager.RESULT_ERROR_NO_SERVICE:
//                            Toast.makeText(pi_context, "No service", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                        case SmsManager.RESULT_ERROR_NULL_PDU:
//                            Toast.makeText(pi_context, "Null PDU", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                        case SmsManager.RESULT_ERROR_RADIO_OFF:
//                            Toast.makeText(pi_context, "Radio off", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//            }, new IntentFilter(SENT));
//
//            //---when the SMS has been delivered---
//            pi_context.registerReceiver(new BroadcastReceiver(){
//                @Override
//                public void onReceive(Context arg0, Intent arg1) {
//                    switch (getResultCode())
//                    {
//                        case Activity.RESULT_OK:
//                            Toast.makeText(pi_context, "SMS delivered", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;
//                        case Activity.RESULT_CANCELED:
//                            Toast.makeText(pi_context, "SMS not delivered", 
//                                    Toast.LENGTH_SHORT).show();
//                            break;                        
//                    }
//                }
//            }, new IntentFilter(DELIVERED));        

//        	sms.sendTextMessage(GetServerNumber(), null, sMessage, sentPI, deliveredPI);
//        	sms.sendTextMessage(GetServerNumber(), null, sMessage, null, null);
        	Toast.makeText(pi_context, sMessage, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
        
        }
		
	}

	public static void SendTripFinished(int m_id) 
	{
		// TODO Auto-generated method stub
		
	}

	public static void SendChildState(Child pi_child) 
	{
		// TODO Auto-generated method stub
		
	}

	public static void SendEmergency(boolean arg1) 
	{
		// TODO Auto-generated method stub
		
	}

}
