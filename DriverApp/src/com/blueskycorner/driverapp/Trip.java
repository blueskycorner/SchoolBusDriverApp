package com.blueskycorner.driverapp;

import java.util.ArrayList;

public class Trip 
{
	public int m_id = -1;
	public String m_destination = null;
	public int m_hour;
	public int m_minute;
	public ArrayList<Child> m_childs = null;
	public boolean m_isCancel;
	public boolean m_isReturn = false;
	public int GetRemainChildCount() 
	{
		int remainCount = 0;
		for (Child child : m_childs) 
		{
			if (child.m_state == E_CHILD_STATE.STATE_WAITING)
			{
				remainCount ++;
			}
		}
		return remainCount;
	}
	public void Init(ArrayList<Child> pi_childs) 
	{
		m_isCancel = false;
		m_isReturn = false;
		m_childs = pi_childs;
		for (Child child : m_childs) 
		{
			child.Init();
		}
	}
	public Child GetChild(int pi_fromId) 
	{
		Child child = null;
		for (Child c : m_childs) 
		{
			if (c.m_id == pi_fromId)
			{
				child = c;
			}
		}
		return child;
	}
	public String GetTime() 
	{
		int hour = m_hour;
		String s = "AM";
		if (m_hour > 12)
		{
			hour -= 12;
			s = "PM";
		}
		String time = Integer.toString(hour) + ":" + Integer.toString(m_minute) + " " + s;

		return time;
	}
}
