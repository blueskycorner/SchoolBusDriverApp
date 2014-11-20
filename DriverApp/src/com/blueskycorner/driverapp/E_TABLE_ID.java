package com.blueskycorner.driverapp;

public enum E_TABLE_ID 
{
	TABLE_SCHOOL(0),
	TABLE_DESTINATION(1),
	TABLE_TRIP(2),
	TABLE_CHILD(3),
	TABLE_TRIP_CHILD_ASSOCIATION(4);
	
	private final int id;
	E_TABLE_ID(int id) { this.id = id; }
	public int getValue() { return id; }
}
