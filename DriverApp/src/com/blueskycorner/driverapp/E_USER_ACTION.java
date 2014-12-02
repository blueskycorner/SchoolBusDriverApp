package com.blueskycorner.driverapp;

public enum E_USER_ACTION 
{
	ACTION_ACK(0);
	
	private final int id;
	E_USER_ACTION(int id) { this.id = id; }
	public int getValue() { return id; }
	public static E_USER_ACTION FromInt(int pi_value)
	{
		E_USER_ACTION s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = ACTION_ACK;
				break;
			}
		}
		return s;
	}

}
