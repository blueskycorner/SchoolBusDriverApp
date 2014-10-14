package com.blueskycorner.driverapp;

public interface DriverAppCommunicator 
{
	void StartTrip(Trip pi_trip);
	
	void ChildSelected(Child pi_child);

	void childStateUpdated(Child pi_child);
	
	void TripFinished();
}
