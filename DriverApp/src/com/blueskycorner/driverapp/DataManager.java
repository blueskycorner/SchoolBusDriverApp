package com.blueskycorner.driverapp;

import java.util.ArrayList;

public class DataManager
{
	static private DataManager m_dataManager = new DataManager();
	
	private School[] m_schoolList = null;
	private Trip[] m_trips = null;
	
	/* Static 'instance' method */
    public static DataManager GetInstance() 
    {
	   return m_dataManager;
    }
    
    private DataManager()
    {

    }

	public void Init() {
		m_schoolList = new School[2];
    	m_trips = new Trip[4];
		
		School lfm = new School();
		School gesm = new School();
		lfm.m_name = "LFM";
		lfm.m_id = 0; 
		gesm.m_name = "GESM";
		gesm.m_id = 1;
		
		ArrayList<Trip> lfmTrips = new ArrayList<Trip>();
		Trip lfmTrip1 = new Trip();
		lfmTrip1.m_name = "Alabang";
		lfmTrip1.m_id = 0;
		ArrayList<Child> childs1 = new ArrayList<Child>();
		Child c = new Child("Alexandre", "EHLERS", "21 Jump Street\nDasmarinas Village, MAKATI");
		c.m_id = 1;
		childs1.add(c);childs1.add(new Child("Julie", "DAUPLE", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add( new Child("Thomas", "LOVET", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Michael", "JORDAN", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Tim", "COOK", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Chantal", "LAUBIE", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Andrew", "JAMES", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Tom", "SELECT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Wilfrid", "CROISARD", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Alexandre", "STAUB", "21 Jump Street\nDasmarinas Village, MAKATI"));
		lfmTrip1.m_childs = childs1;
		Trip lfmTrip2 = new Trip();
		lfmTrip2.m_name = "Makati";
		lfmTrip2.m_id = 1;
		ArrayList<Child> childs2 = new ArrayList<Child>();
		childs2.add(new Child("Basile", "BIHEL", "21 Jump Street\nDasmarinas Village, MAKATI"));childs2.add(new Child("Jules", "DRACOT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs2.add(new Child("Charles", "PAUTIER", "21 Jump Street\nDasmarinas Village, MAKATI"));
		lfmTrip2.m_childs = childs2;
		lfmTrips.add(lfmTrip1);
		lfmTrips.add(lfmTrip2);
		
		ArrayList<Trip> gesmTrips = new ArrayList<Trip>();
		Trip gesmTrip1 = new Trip();
		gesmTrip1.m_name = "Paranaque";
		gesmTrip1.m_id = 2;
		ArrayList<Child> childs3 = new ArrayList<Child>();
		childs3.add(new Child("Steve", "MARSHAL", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Kobe", "BRYANT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Charles", "OAKLEY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Nina", "RING", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Mabel", "DE CASTRO", "21 Jump Street\nDasmarinas Village, MAKATI"));
		gesmTrip1.m_childs = childs3;
		Trip gesmTrip2 = new Trip();
		gesmTrip2.m_name = "Salcedo";
		gesmTrip2.m_id = 3;
		ArrayList<Child> childs4 = new ArrayList<Child>();
		childs4.add(new Child("Robert", "De Niro", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Sam", "LOURY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Stéphanie", "DAMIOT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Malcom", "INSTEAD", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Arnold", "SHWARZY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Johnny", "DEPP", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Deena", "BLINK", "21 Jump Street\nDasmarinas Village, MAKATI"));
		gesmTrip2.m_childs = childs4;
		gesmTrips.add(gesmTrip1);
		gesmTrips.add(gesmTrip2);
		
		lfm.m_trips = lfmTrips;
		gesm.m_trips = gesmTrips;
		
		m_schoolList[0] = lfm;
		m_schoolList[1] = gesm;
		
		m_trips[0] = lfmTrip1;
		m_trips[1] = lfmTrip2;
		m_trips[2] = gesmTrip1;
		m_trips[3] = gesmTrip2;
	}

	public School[] GetSchools() 
	{
		return m_schoolList;
	}

	public School getSchool(int pi_schoolId) 
	{
		return m_schoolList[pi_schoolId];
	}

	public Trip getTrip(int pi_tripId) 
	{
		return m_trips[pi_tripId];
	}
}
