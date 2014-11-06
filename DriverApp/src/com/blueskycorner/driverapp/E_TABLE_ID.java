package com.blueskycorner.driverapp;

public enum E_TABLE_ID 
{
	ID_SCHOOL(0),
	ID_TRIP(1),
	ID_CHILD(2);
	
	private final int id;
	E_TABLE_ID(int id) { this.id = id; }
	public int getValue() { return id; }
}
