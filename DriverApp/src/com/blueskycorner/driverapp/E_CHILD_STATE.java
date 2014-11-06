package com.blueskycorner.driverapp;

public enum E_CHILD_STATE 
{
	STATE_ON_THE_WAY(0),
	STATE_FINISH(1),
	STATE_SKIPPED(2),
	STATE_WAITING(3),
	STATE_MISSING(4);
	
	private final int id;
	E_CHILD_STATE(int id) { this.id = id; }
	public int getValue() { return id; }

}
