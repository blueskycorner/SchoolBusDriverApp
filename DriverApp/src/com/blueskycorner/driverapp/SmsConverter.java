package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.telephony.SmsMessage;

public class SmsConverter 
{

	public static ArrayList<DriverAppMessage> Convert(DriverAppSmsMessage pi_sms) 
	{
		ArrayList<DriverAppMessage> list = new ArrayList<DriverAppMessage>();
		
		DriverAppMessage m = new DriverAppMessage();
		m.m_cellNumer = pi_sms.m_number;
		m.m_text = pi_sms.m_body;
		if ( (m.m_cellNumer.equals("111")) || (m.m_cellNumer.equals("+639157912584")) )
		{
			m.m_from = E_SMS_FROM.FROM_PARENT;
		}
		else if (m.m_cellNumer.equals("222"))
		{
			m.m_from = E_SMS_FROM.FROM_SCHOOL;
		}
		else if (m.m_cellNumer.equals("333"))
		{
			m.m_from = E_SMS_FROM.FROM_DRIVER_COMPANY;
		}
		else
		{
			m.m_from = E_SMS_FROM.FROM_UNKNOWN;
		}
		
		if (m.m_cellNumer.equals("+639157912584"))
		{
			m.m_fromId = 1;
		}
		else
		{
			m.m_fromId = 0;
		}
		
		list.add(m);
		
		return list;
	}

}
