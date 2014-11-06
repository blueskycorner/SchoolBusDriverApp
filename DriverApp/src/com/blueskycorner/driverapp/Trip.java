package com.blueskycorner.driverapp;

import java.util.ArrayList;

public class Trip 
{
	public int m_id = -1;
	public int m_schoolId = -1;
	public String m_name = null;
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
	public void Init() 
	{
		m_isCancel = false;
		m_isReturn = false;
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
}
