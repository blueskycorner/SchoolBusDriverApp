package com.blueskycorner.driverapp;

public enum E_CHILD_STATE 
{
	STATE_ON_THE_WAY_STARTED(0),
	STATE_ON_THE_WAY_FINISHED(1),
	STATE_ON_THE_WAY_CANCELED(2),
	STATE_WAITING(3),
	STATE_MISSING(4);
	
	private final int id;
	E_CHILD_STATE(int id) { this.id = id; }
	public int getValue() { return id; }

}
