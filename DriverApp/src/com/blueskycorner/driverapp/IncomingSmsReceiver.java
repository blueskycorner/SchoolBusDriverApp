package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSmsReceiver extends BroadcastReceiver {
    
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private ArrayList<ISmsListener> m_listeners = new ArrayList<ISmsListener>();
     
    public void onReceive(Context context, Intent intent) 
    {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try 
        {
            if (bundle != null) 
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                DriverAppSmsMessage message = new DriverAppSmsMessage();
                for (int i = 0; i < pdusObj.length; i++) 
                {
                	SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                	if (currentMessage.getOriginatingAddress().equals(DriverAppParamHelper.GetDeviceGateway(context)) == false)
                	{
                		return;
                	}
                    message.m_body += currentMessage.getMessageBody();
                    message.m_date = currentMessage.getTimestampMillis();
                 
                } // end for loop
                
                BroadcastSms(message);
              } // bundle is null
           
        } 
        catch (Exception e) 
        {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
        finally
        {
//        	abortBroadcast();
        }
    }

	public void AddListener(ISmsListener pi_listener) 
	{
		if (m_listeners.contains(pi_listener) == false)
		{
			m_listeners.add(pi_listener);
		}
	}    

	public void RemoveListener(ISmsListener pi_listener) 
	{
		if (m_listeners.contains(pi_listener) == false)
		{
			m_listeners.remove(pi_listener);
		}
	}    
	
	private void BroadcastSms(DriverAppSmsMessage pi_sms)
	{
		for (ISmsListener l : m_listeners) 
		{
			l.OnSmsReceived(pi_sms);
		}
	}
}
