package com.blueskycorner.driverapp;

public enum E_LANGUAGE 
{
	ENGLISH(0),
	FRENCH(1);
	
	private final int id;
	E_LANGUAGE(int id) { this.id = id; }
	public int getValue() { return id; }

	public static E_LANGUAGE FromInt(int pi_value)
	{
		E_LANGUAGE s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = ENGLISH;
				break;
			}
			case 1:
			{
				s = FRENCH;
				break;
			}
		}
		return s;
	}
}
