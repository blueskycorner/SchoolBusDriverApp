package com.blueskycorner.driverapp;

public interface ISynchronizerListener 
{
	void OnStepChanged(E_INIT_STEP pi_step);

	void OnDataSynchronised();
}
