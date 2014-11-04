package com.blueskycorner.driverapp;

public interface IDriverAppCommunicator 
{
	void TripStarted(Trip pi_trip);
	
	void ChildSelected(Child pi_child);

	void childStateUpdated(Child pi_child);
	
	void TripFinished();
}
