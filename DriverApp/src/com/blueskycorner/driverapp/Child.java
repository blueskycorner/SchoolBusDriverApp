package com.blueskycorner.driverapp;

public class Child 
{
	public Child(String pi_firstName, String pi_lastName, String pi_address)
	{
		m_firstName = pi_firstName;
		m_lastName = pi_lastName;
		m_address = pi_address;
		m_isPresent = false;
		m_state = E_CHILD_STATE.STATE_WAITING;
	}
	
	public String m_firstName = "";
	public String m_lastName = "";
	public String m_address = "";
	public boolean m_isPresent = false;
	public E_CHILD_STATE m_state;
	public int m_id = 0;
	
	public void Init() 
	{
		m_isPresent = false;
		m_state = E_CHILD_STATE.STATE_WAITING;
	}
}
