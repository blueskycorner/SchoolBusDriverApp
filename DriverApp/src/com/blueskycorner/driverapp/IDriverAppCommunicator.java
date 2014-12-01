package com.blueskycorner.driverapp;

public interface IDriverAppCommunicator 
{
	void TripSelected(Trip pi_trip);
	
	void TripStarted();
	
	void ChildSelected(Child pi_child);

	void childStateUpdated(Child pi_child);
	
	void TripFinished();

	void ChildAdded(Child m_child);
}
