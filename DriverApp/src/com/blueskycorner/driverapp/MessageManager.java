package com.blueskycorner.driverapp;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class MessageManager implements ISmsListener, IDeviceDataListener 
{
	private IncomingSmsReceiver m_incommingSmsReceiver = null;
	private Activity m_activity = null;
	private IDriverAppCommunicator m_comm = null;
	private ReentrantLock m_lock = null;
	private int m_messageId = 0;
	private HashMap<Integer, MessageThread> m_messageThreads = null;
	
	public MessageManager(Activity pi_activity, IDriverAppCommunicator pi_communicator) 
	{
		m_activity = pi_activity;
		m_comm = pi_communicator;
		m_messageThreads = new HashMap<Integer, MessageManager.MessageThread>();
		m_lock = new ReentrantLock();
		
		m_incommingSmsReceiver = new IncomingSmsReceiver();
		m_incommingSmsReceiver.AddListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
        m_activity.registerReceiver(m_incommingSmsReceiver, filter);
	}

	@Override
	public void OnSmsReceived(DriverAppSmsMessage pi_sms) 
	{
		switch (pi_sms.GetType())
		{
			case 0:
			{
				Trip trip = DataManager.GetInstance().GetCurrentTrip();
				if (trip != null)
				{
					Child c = trip.GetChild(pi_sms.GetChildId());
					if (c != null)
					{
						UserMessage um = new UserMessage();
						um.m_sender = E_SENDER.SENDER_PARENT;
						um.m_message = c.m_firstName + " " + c.m_lastName + " " + m_activity.getResources().getText(R.string.doesnt_take_the_bus);
						um.m_action = E_USER_ACTION.ACTION_ACK;
						c.m_state = E_CHILD_STATE.STATE_ON_THE_WAY_CANCELED;
						StartMessageActivity(um);
						m_comm.childStateImplicitlyUpdated(c);
					}
					else
					{
						SmsSender.SendCancelationProblem(m_activity, pi_sms.GetChildId());
					}
				}
			}
			case 3:
			{
				DataGrabber dg = new DataGrabber();
				dg.GetDeviceState(m_activity, this);
			}
		}

	}

	public void StartMessageActivity(UserMessage um) 
	{
		MessageThread t = new MessageThread(um, m_messageId);
		m_messageThreads.put(m_messageId, t);
		m_messageId ++;
		t.start();
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
		private UserMessage m_message;
		private int m_messageId;
		Handler m_handler = null;
		
		public MessageThread(UserMessage um, int pi_messageId)
		{
			m_message = um;
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

	@Override
	public void onDeviceDataChanged(DeviceState pi_deviceState) 
	{
		SmsSender.SendDeviceState(m_activity, pi_deviceState);
	}
}
