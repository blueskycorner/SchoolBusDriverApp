package com.blueskycorner.driverapp;

import java.util.ArrayList;

public class TableContainer 
{
	public int[] m_version;
	public ArrayList<School> m_school;
	public ArrayList<Trip> m_trip;
	public ArrayList<Child> m_child;
	
	public TableContainer(int[] pi_versionList,
						  ArrayList<School> pi_schoolList, 
						  ArrayList<Trip> pi_tripList, 
						  ArrayList<Child> pi_childList)
	{
		m_version = pi_versionList;
		m_school = pi_schoolList;
		m_trip = pi_tripList;
		m_child = pi_childList;
	}
}
