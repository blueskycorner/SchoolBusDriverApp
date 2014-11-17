package com.blueskycorner.driverapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;

public class DataManager
{
	public static final int TABLE_ACCOUNT = 3;
	static private DataManager m_dataManager = new DataManager();
	private ReentrantLock m_lock = null;
	
	private School[] m_schoolList = null;
	private Trip[] m_trips = null;
	
	private Context m_context = null;
	private SchoolDAO m_schoolDAO = null;
	
	/* Static 'instance' method */
    public static DataManager GetInstance() 
    {
	   return m_dataManager;
    }
    
    private DataManager()
    {
    	m_lock = new ReentrantLock();
    }
    
    public void SetContext(Context pi_context)
    {
 	   m_context = pi_context;
 	   Init();
    }

	private void Init() 
	{
		m_schoolDAO = new SchoolDAO(m_context);
		m_schoolDAO.open();
		
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

	public ArrayList<School> GetSchools() 
	{
		return m_schoolDAO.GetSchool();
	}

	public School getSchool(int pi_schoolId) 
	{
		return m_schoolDAO.GetSchool().get(pi_schoolId);
	}

	public Trip getTrip(int pi_tripId) 
	{
		return m_trips[pi_tripId];
	}

	public void Update(Context pi_context, boolean pi_bForceUpdate) 
	{
		DataBaseUpdateThread t = new DataBaseUpdateThread(pi_context, pi_bForceUpdate);
		t.start();
	}

	private void SynchDB(Context pi_context) 
	{
		DriverAppParamHelper.SetLastDBUpdateTime(pi_context, System.currentTimeMillis());
	}
	
	private class DataBaseUpdateThread extends Thread
	{
		Context m_context = null;
		Boolean m_bForceUpdate = false;
		public DataBaseUpdateThread(Context pi_context, boolean pi_bForceUpdate) 
		{
			m_context = pi_context;
			m_bForceUpdate = pi_bForceUpdate;
		}

		@Override
		public void run() 
		{
			m_lock.lock();
			try
			{
				long lastUpdate = DriverAppParamHelper.GetLastDBUpdateTime(m_context);
				int period = DriverAppParamHelper.GetAutoUpdatePeriod(m_context);
				if ( (lastUpdate + period > System.currentTimeMillis()) || (m_bForceUpdate == true) )
				{
					SynchDB(m_context);
				}
			}
			catch (Exception e)
			{
				
			}
			finally
			{
				m_lock.unlock();
			}

		}
	}

	public int[] GetLocalTableVersion() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean Update(TableContainer c) 
	{
		// TODO Auto-generated method stub
		return true;
	}
}
