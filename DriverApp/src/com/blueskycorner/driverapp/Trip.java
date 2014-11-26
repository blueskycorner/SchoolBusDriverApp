package com.blueskycorner.driverapp;

import java.util.ArrayList;

public class Trip 
{
	public int m_id = -1;
	public String m_destination = null;
	public int m_hour = 0;
	public int m_minute = 0;
	public ArrayList<Child> m_childs = null;
	public boolean m_isCancel = false;
	public boolean m_isReturn = false;
	private Child m_currentChild = null;
	
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
		m_childs = pi_childs;
		Init();
	}

	public void Init() 
	{
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
	
	private String GetTime() 
	{
		int hour = m_hour;
		String s = "AM";
		if (m_hour > 12)
		{
			hour -= 12;
			s = "PM";
		}
		String format = "%1$02d";
		String time = String.format(format, hour) + ":" + String.format(format, m_minute) + " " + s;

		return time;
	}
	
	public ArrayList<Integer> GetMissingChilds() 
	{
		ArrayList<Integer> missingChilds = new ArrayList<Integer>();
		
		for (Child child : m_childs) 
		{
			if ( (child.m_isAdded == false) && (child.m_isPresent == false) )
			{
				missingChilds.add(child.m_id);
			}
		}
		return missingChilds;
	}
	public ArrayList<Integer> GetAddedChilds() 
	{
		ArrayList<Integer> addedChilds = new ArrayList<Integer>();
		
		for (Child child : m_childs) 
		{
			if ( (child.m_isAdded == true) && (child.m_isPresent == true) )
			{
				addedChilds.add(child.m_id);
			}
		}
		return addedChilds;
	}
	
	public String ToString() 
	{
		String s = m_destination + " @ " + GetTime();
		return s;
	}

	public void SetCurrentChild(Child pi_child) 
	{
		m_currentChild  = pi_child;
	}
	
	public Child GetCurrentChild()
	{
		return m_currentChild;
	}
}
