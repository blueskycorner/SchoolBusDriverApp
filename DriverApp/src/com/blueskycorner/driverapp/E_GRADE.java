package com.blueskycorner.driverapp;

public enum E_GRADE 
{
	GRADE_PS(0),
	GRADE_MS(1),
	GRADE_GS(2),
	GRADE_CP(3),
	GRADE_CE1(4),
	GRADE_CE2(5),
	GRADE_CM1(6),
	GRADE_CM2(7),
	GRADE_6EME(8),
	GRADE_5EME(9),
	GRADE_4EME(10),
	GRADE_3EME(11),
	GRADE_2ND(12),
	GRADE_1ERE(13),
	GRADE_TERMINAL(14);
	
	private final int id;
	E_GRADE(int id) { this.id = id; }
	public int getValue() { return id; }
	public static E_GRADE FromInt(int pi_value)
	{
		E_GRADE s = null;
		switch (pi_value) 
		{
			case 0:
			{
				s = GRADE_PS;
				break;
			}
			case 1:
			{
				s = GRADE_MS;
				break;
			}
			case 2:
			{
				s = GRADE_GS;
				break;
			}
			case 3:
			{
				s = GRADE_CP;
				break;
			}
			case 4:
			{
				s = GRADE_CE1;
				break;
			}
			case 5:
			{
				s = GRADE_CE2;
				break;
			}
			case 6:
			{
				s = GRADE_CM1;
				break;
			}
			case 7:
			{
				s = GRADE_CM2;
				break;
			}
			case 8:
			{
				s = GRADE_6EME;
				break;
			}
			case 9:
			{
				s = GRADE_5EME;
				break;
			}
			case 10:
			{
				s = GRADE_4EME;
				break;
			}
			case 11:
			{
				s = GRADE_3EME;
				break;
			}
			case 12:
			{
				s = GRADE_2ND;
				break;
			}
			case 13:
			{
				s = GRADE_1ERE;
				break;
			}
			case 14:
			{
				s = GRADE_TERMINAL;
				break;
			}
		}
		return s;
	}

}
