package com.blueskycorner.driverapp;

public enum E_SMS_FROM 
{
	FROM_UNKNOWN(0),
	FROM_PARENT(1),
	FROM_SCHOOL(2),
	FROM_DRIVER_COMPANY(3);
	
	private final int id;
	E_SMS_FROM(int id) { this.id = id; }
	public int getValue() { return id; }
	
	public static E_SMS_FROM FromInt(int pi_value)
	{
		E_SMS_FROM s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = FROM_UNKNOWN;
				break;
			}
			case 1:
			{
				s = FROM_PARENT;
				break;
			}
			case 2:
			{
				s = FROM_SCHOOL;
				break;
			}
			case 3:
			{
				s = FROM_DRIVER_COMPANY;
				break;
			}
		}
		return s;
	}
}
