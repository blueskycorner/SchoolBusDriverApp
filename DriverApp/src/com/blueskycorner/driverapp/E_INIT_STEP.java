package com.blueskycorner.driverapp;

public enum E_INIT_STEP 
{
	STEP_DEVICE_UPDATE(0),
	STEP_DB_UPDATE(1);
	
	private final int id;
	E_INIT_STEP(int id) { this.id = id; }
	public int getValue() { return id; }
}
