package com.blueskycorner.driverapp;

import java.util.ArrayList;

//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.widget.Toast;
import android.telephony.SmsManager;
import android.content.Context;

public class SmsSender 
{
	static final String SEPARATOR_STRING = "|";
	
	private static final String START_TRIP_SCHOOL_MESSAGE_ID = "0";
	private static final String START_TRIP_HOME_MESSAGE_ID = "1";
	private static final String ON_THE_WAY_STARTED_MESSAGE_ID = "2";
	private static final String ON_THE_WAY_FINISHED_MESSAGE_ID = "3";
	private static final String ON_THE_WAY_CANCELED_MESSAGE_ID = "4";
	private static final String CANCELATION_PROBLEM_MESSAGE_ID = "5";
	private static final String EMERGENCY_MESSAGE_ID = "6";
	private static final String DEVICE_STATE_MESSAGE_ID = "7";
	private static final String TRIP_FINISHED_MESSAGE_ID = "8";
	private static final String TRIP_CANCELED_MESSAGE_ID = "9";

	private static void SendSMS(Context pi_context, String sMessage) 
	{
		SmsManager sms = SmsManager.getDefault();
//		Toast.makeText(pi_context, sMessage, Toast.LENGTH_SHORT).show();
		if (DriverAppParamHelper.GetRealSms(pi_context) == true)
		{			
			sms.sendTextMessage(DriverAppParamHelper.GetDeviceGateway(pi_context), null, sMessage, null, null);
		}
	}

	private static String GetSmsHead(final Context pi_context, final String pi_sMessageId) 
	{
		String sMessage = pi_sMessageId + SEPARATOR_STRING +
				DriverAppParamHelper.GetDeviceId(pi_context) + SEPARATOR_STRING;
		return sMessage;
	}
	
	// SEND TRIP SCHOOL
	public static void SendTripSchoolStarted(final Context pi_context, int pi_tripId) 
	{
		String sMessage = GetSmsHead(pi_context, START_TRIP_SCHOOL_MESSAGE_ID);
		sMessage += String.valueOf(pi_tripId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	// SEND TRIP HOME
	public static void SendTripHomeStarted(final Context pi_context, Trip pi_trip) 
	{
		String sMessage = GetSmsHead(pi_context, START_TRIP_HOME_MESSAGE_ID);
		sMessage += String.valueOf(pi_trip.m_id) + SEPARATOR_STRING;
		
		ArrayList<Integer> missingChilds = pi_trip.GetMissingChilds();

		sMessage += String.valueOf(missingChilds.size()) + SEPARATOR_STRING;
		for (Integer missedChildId : missingChilds) 
		{
			sMessage += String.valueOf(missedChildId) + SEPARATOR_STRING;
		}
		
		ArrayList<Integer> addedChilds = pi_trip.GetAddedChilds();

		sMessage += String.valueOf(addedChilds.size()) + SEPARATOR_STRING;
		for (Integer addedChildId : addedChilds) 
		{
			sMessage += String.valueOf(addedChildId) + SEPARATOR_STRING;
		}
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND ON THE WAY STARTED
	public static void SendOntheWayStarted(final Context pi_context, int pi_childId)
	{
		String sMessage = GetSmsHead(pi_context, ON_THE_WAY_STARTED_MESSAGE_ID);
		sMessage += String.valueOf(pi_childId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND ON THE WAY FINISHED
	public static void SendOntheWayFinished(final Context pi_context, int pi_childId)
	{
		String sMessage = GetSmsHead(pi_context, ON_THE_WAY_FINISHED_MESSAGE_ID);
		sMessage += String.valueOf(pi_childId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND ON THE WAY CANCELED
	public static void SendOntheWayCanceled(final Context pi_context, int pi_childId)
	{
		String sMessage = GetSmsHead(pi_context, ON_THE_WAY_CANCELED_MESSAGE_ID);
		sMessage += String.valueOf(pi_childId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND CANCELATION PROBLEM
	public static void SendCancelationProblem(final Context pi_context, int pi_childId)
	{
		String sMessage = GetSmsHead(pi_context, CANCELATION_PROBLEM_MESSAGE_ID);
		sMessage += String.valueOf(pi_childId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND EMERGENCY STATE
	public static void SendEmergency(final Context pi_context, boolean pi_begin)
	{
		String sMessage = GetSmsHead(pi_context, EMERGENCY_MESSAGE_ID);
		sMessage += String.valueOf(Boolean.compare(pi_begin, false)) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}
	
	///// SEND DEVICE STATE
	public static void SendDeviceState(Context pi_context, DeviceState pi_deviceState)
	{
		String sMessage = DEVICE_STATE_MESSAGE_ID + SEPARATOR_STRING + 	pi_deviceState.toSMS(pi_context);
		SendSMS(pi_context, sMessage);
	}

	///// SEND TRIP FINISHED
	public static void SendTripFinished(Context pi_context, int pi_tripId) 
	{
		String sMessage = GetSmsHead(pi_context, TRIP_FINISHED_MESSAGE_ID);
		sMessage += String.valueOf(pi_tripId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}

	///// SEND TRIP FINISHED
	public static void SendTripCanceled(Context pi_context, int pi_tripId) 
	{
		String sMessage = GetSmsHead(pi_context, TRIP_CANCELED_MESSAGE_ID);
		sMessage += String.valueOf(pi_tripId) + SEPARATOR_STRING;
		SendSMS(pi_context, sMessage);
	}

}
//String SENT = "SMS_SENT";
//String DELIVERED = "SMS_DELIVERED";
//
//PendingIntent sentPI = PendingIntent.getBroadcast(pi_context, 0,
//  new Intent(SENT), 0);
//
//PendingIntent deliveredPI = PendingIntent.getBroadcast(pi_context, 0,
//  new Intent(DELIVERED), 0);
//
////---when the SMS has been sent---
//pi_context.registerReceiver(new BroadcastReceiver(){
//  @Override
//  public void onReceive(Context arg0, Intent arg1) {
//      switch (getResultCode())
//      {
//          case Activity.RESULT_OK:
//              Toast.makeText(pi_context, "SMS sent", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//              Toast.makeText(pi_context, "Generic failure", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//          case SmsManager.RESULT_ERROR_NO_SERVICE:
//              Toast.makeText(pi_context, "No service", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//          case SmsManager.RESULT_ERROR_NULL_PDU:
//              Toast.makeText(pi_context, "Null PDU", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//          case SmsManager.RESULT_ERROR_RADIO_OFF:
//              Toast.makeText(pi_context, "Radio off", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//      }
//  }
//}, new IntentFilter(SENT));
//
////---when the SMS has been delivered---
//pi_context.registerReceiver(new BroadcastReceiver(){
//  @Override
//  public void onReceive(Context arg0, Intent arg1) {
//      switch (getResultCode())
//      {
//          case Activity.RESULT_OK:
//              Toast.makeText(pi_context, "SMS delivered", 
//                      Toast.LENGTH_SHORT).show();
//              break;
//          case Activity.RESULT_CANCELED:
//              Toast.makeText(pi_context, "SMS not delivered", 
//                      Toast.LENGTH_SHORT).show();
//              break;                        
//      }
//  }
//}, new IntentFilter(DELIVERED));        

//sms.sendTextMessage(GetServerNumber(), null, sMessage, sentPI, deliveredPI);
//sms.sendTextMessage(GetServerNumber(), null, sMessage, null, null);

