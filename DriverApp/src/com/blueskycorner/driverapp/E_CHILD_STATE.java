package com.blueskycorner.driverapp;

public enum E_CHILD_STATE 
{
	STATE_ON_THE_WAY(0),
	FINISH(1),
	SKIPPED(2),
	WAITING(3),
	MISSING(4);
	
	private final int id;
	E_CHILD_STATE(int id) { this.id = id; }
	public int getValue() { return id; }

}
