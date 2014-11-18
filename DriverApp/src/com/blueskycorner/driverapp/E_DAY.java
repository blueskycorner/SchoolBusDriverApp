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
}
