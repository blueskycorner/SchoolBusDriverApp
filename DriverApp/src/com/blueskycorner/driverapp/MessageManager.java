package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.System;
import android.telephony.SmsMessage;

public class MessageManager implements ISmsListener 
{
	private IncomingSmsReceiver m_incommingSmsReceiver = null;
	private Activity m_activity = null;
	private ReentrantLock m_lock = null;
	private int m_messageId = 0;
	private HashMap<Integer, MessageThread> m_messageThreads = null;
	private Trip m_trip = null;
	private ArrayList<IMessageListener> m_messageListener;
	
	public MessageManager(Activity pi_activity) 
	{
		m_activity = pi_activity;
		m_messageThreads = new HashMap<Integer, MessageManager.MessageThread>();
		m_lock = new ReentrantLock();
		m_messageListener = new ArrayList<IMessageListener>();
		
		m_incommingSmsReceiver = new IncomingSmsReceiver();
		m_incommingSmsReceiver.AddListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
        m_activity.registerReceiver(m_incommingSmsReceiver, filter);
	}
	
	public void SetTrip(Trip pi_trip)
	{
		m_trip = pi_trip;
	}

	@Override
	public void OnSmsReceived(DriverAppSmsMessage pi_sms) 
	{
		if (pi_sms.HasToBeShown() == true)
		{
			MessageThread t = new MessageThread(pi_sms, m_messageId);
			m_messageThreads.put(m_messageId, t);
			m_messageId ++;
			t.start();	
		}
		BroadCastMessage(pi_sms);
	}
	
	private void BroadCastMessage(DriverAppSmsMessage pi_sms) 
	{
		for (IMessageListener l : m_messageListener)
		{
			l.onMessageReceived(pi_sms);
		}
	}
	
	public void AddMessageListener(IMessageListener pi_listener)
	{
		if (m_messageListener.contains(pi_listener) == false)
		{
			m_messageListener.add(pi_listener);
		}
	}
	
	public void RemoveMessageListener(IMessageListener pi_listener)
	{
		if (m_messageListener.contains(pi_listener) == true)
		{
			m_messageListener.remove(pi_listener);
		}
	}

	public void OnMessageAcknowledge(int pi_messageId)
	{
		MessageThread t = m_messageThreads.get(pi_messageId);
		if (t != null)
		{
			t.m_handler.post(new Runnable() 
			{
				@Override
				public void run() {
					m_lock.unlock();
				}
			});
			m_messageThreads.remove(t);
		}

	}
	
	private class MessageThread extends Thread
	{
		private DriverAppSmsMessage m_message;
		private int m_messageId;
		Handler m_handler = null;
		
		public MessageThread(DriverAppSmsMessage pi_message, int pi_messageId)
		{
			m_message = pi_message;
			m_messageId = pi_messageId;
		}
		
		@Override
		public void run() 
		{
			Looper.prepare();
			m_handler = new Handler();
			
			m_lock.lock();
			Intent it = new Intent("intent.message");
	        it.setComponent(new ComponentName(m_activity.getPackageName(), MessageActivity.class.getName()));
//			it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        it.setAction(Intent.ACTION_MAIN);
	        it.addCategory(Intent.CATEGORY_LAUNCHER);
	        it.putExtra(MessageActivity.MESSAGE, m_message);
	        m_activity.startActivityForResult(it, m_messageId);
	        Looper.loop();
		}
	}
}
