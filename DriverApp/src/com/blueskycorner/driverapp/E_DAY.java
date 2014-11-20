package com.blueskycorner.driverapp;

public enum E_DAY 
{
	MONDAY(0),
	TUESDAY(1),
	WEDNESDAY(2),
	THURSDAY(3),
	FRIDAY(4);
	
	private final int id;
	E_DAY(int id) { this.id = id; }
	public int getValue() { return id; }
	
	public static E_DAY FromInt(int pi_value)
	{
		E_DAY s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = MONDAY;
				break;
			}
			case 1:
			{
				s = TUESDAY;
				break;
			}
			case 2:
			{
				s = WEDNESDAY;
				break;
			}
			case 3:
			{
				s = THURSDAY;
				break;
			}
			case 4:
			{
				s = FRIDAY;
				break;
			}
		}
		return s;
	}

}
