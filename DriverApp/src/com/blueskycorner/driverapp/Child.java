package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Date;

public class Child 
{
	public Child()
	{
		Init();
	}
		
	// DB fields
	public int m_id = 0;
	public String m_firstName = "";
	public String m_lastName = "";
	public Date m_birthdate = new Date(0);
	public E_GRADE m_grade = E_GRADE.GRADE_PS;
	public ArrayList<String> m_address = new ArrayList<String>();
	public int m_activeAddress = 0;
	public E_LANGUAGE m_language = E_LANGUAGE.ENGLISH;
	public Date m_creationDate = new Date(0);
	public Date m_modificationDate = new Date(0);
	public int m_pickupTimeHour = 0;
	public int m_pickupTimeMinute = 0;
	public String m_mondayInfo = "";
	public String m_tuesdayInfo = "";
	public String m_wednesdayInfo = "";
	public String m_thursdayInfo = "";
	public String m_fridayInfo = "";
	// Other fields
	public boolean m_isPresent = false;
	public boolean m_isAdded = false;
	public E_CHILD_STATE m_state = E_CHILD_STATE.STATE_WAITING;
	
	public void Init() 
	{
		m_isPresent = false;
		m_state = E_CHILD_STATE.STATE_WAITING;
	}

	public String GetPickupTime() 
	{
		String time = "";
		if ( (m_pickupTimeHour != 0) && (m_pickupTimeMinute != 0) )
		{
			int hour = m_pickupTimeHour;
			String s = "AM";
			if (m_pickupTimeHour > 12)
			{
				hour -= 12;
				s = "PM";
			}
			time = Integer.toString(hour) + ":" + Integer.toString(m_pickupTimeMinute) + " " + s;
		}

		return time;

	}

	public String GetAddress() 
	{
		String s = m_address.get(m_activeAddress);
		return s;
	}
}
