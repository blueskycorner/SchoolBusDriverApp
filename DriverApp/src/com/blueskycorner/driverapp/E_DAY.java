package com.blueskycorner.driverapp;

public enum E_DAY 
{
	MONDAY(0),
	TUESDAY(1),
	WEDNESDAY(2),
	THURSDAY(3),
	FRIDAY(4),
	SATURDAY(5),
	SUNDAY(6);
	
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
			case 5:
			{
				s = SATURDAY;
				break;
			}
			case 6:
			{
				s = SUNDAY;
				break;
			}
		}
		return s;
	}

	public static E_DAY FromCalendarInt(int pi_value)
	{
		E_DAY s = null;
		switch (pi_value) 
		{
			case 1:
			{
				s = SUNDAY;
				break;
			}
			case 2:
			{
				s = MONDAY;
				break;
			}
			case 3:
			{
				s = TUESDAY;
				break;
			}
			case 4:
			{
				s = WEDNESDAY;
				break;
			}
			case 5:
			{
				s = THURSDAY;
				break;
			}
			case 6:
			{
				s = FRIDAY;
				break;
			}
			case 7:
			{
				s = SATURDAY;
				break;
			}
		}
		return s;
	}

}
