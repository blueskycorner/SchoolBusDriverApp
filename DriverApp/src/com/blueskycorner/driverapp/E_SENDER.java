package com.blueskycorner.driverapp;

public enum E_SENDER 
{
	SENDER_PARENT(0),
	SENDER_SCHOOL(1),
	SENDER_DRIVER_COMPANY(2);
	
	private final int id;
	E_SENDER(int id) { this.id = id; }
	public int getValue() { return id; }
	public static E_SENDER FromInt(int pi_value)
	{
		E_SENDER s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = SENDER_PARENT;
				break;
			}
			case 1:
			{
				s = SENDER_SCHOOL;
				break;
			}
			case 2:
			{
				s = SENDER_DRIVER_COMPANY;
				break;
			}
		}
		return s;
	}

}
