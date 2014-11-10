package com.blueskycorner.driverapp;

public enum E_SYNCHRONISATION_MODE 
{
	MODE_AUTO(0),
	MODE_MANUALY(1),
	MODE_STARTUP(2);
	
	private final int id;
	E_SYNCHRONISATION_MODE(int id) { this.id = id; }
	public int getValue() { return id; }

}
