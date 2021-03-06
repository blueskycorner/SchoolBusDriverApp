package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataManager
{
	static private DataManager m_dataManager = new DataManager();
	private ReentrantLock m_lock = null;
	
//	private School[] m_schoolList = null;
//	private Trip[] m_trips = null;
	
	private Context m_context = null;
	protected SQLiteDatabase m_database = null;
	
	private SchoolDAO m_schoolDAO = null;
	private TripDAO m_tripDAO = null;
	private TripChildAssociationDAO m_tripChildAssociationDAO = null;
	private ChildDAO m_childDAO = null;
	private TripDestinationDAO m_tripDestinationDAO = null;
	private VersionDAO m_versionDAO = null;
	private Trip m_currentTrip = null;
	private Child m_currentChild = null;
	
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
		SqlLiteHelper sqliteHelper = new SqlLiteHelper(m_context);
		m_database = sqliteHelper.getWritableDatabase();
		
		m_schoolDAO = new SchoolDAO(sqliteHelper);
		m_schoolDAO.open();
		
		m_tripDAO = new TripDAO(sqliteHelper);
		m_tripDAO.open();
		
		m_tripChildAssociationDAO = new TripChildAssociationDAO(sqliteHelper);
		m_tripChildAssociationDAO.open();
		
		m_childDAO = new ChildDAO(sqliteHelper);
		m_childDAO.open();
		
		m_tripDestinationDAO = new TripDestinationDAO(sqliteHelper);
		m_tripDestinationDAO.open();
		
		m_versionDAO = new VersionDAO(sqliteHelper);
		m_versionDAO.open();
		
//		m_schoolList = new School[2];
//    	m_trips = new Trip[4];
//		
//		School lfm = new School();
//		School gesm = new School();
//		lfm.m_name = "LFM";
//		lfm.m_id = 0; 
//		gesm.m_name = "GESM";
//		gesm.m_id = 1;
//		
//		ArrayList<Trip> lfmTrips = new ArrayList<Trip>();
//		Trip lfmTrip1 = new Trip();
//		lfmTrip1.m_destination = "Alabang";
//		lfmTrip1.m_id = 0;
//		ArrayList<Child> childs1 = new ArrayList<Child>();
//		Child c = new Child("Alexandre", "EHLERS", "21 Jump Street\nDasmarinas Village, MAKATI");
//		c.m_id = 1;
//		childs1.add(c);childs1.add(new Child("Julie", "DAUPLE", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add( new Child("Thomas", "LOVET", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Michael", "JORDAN", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Tim", "COOK", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Chantal", "LAUBIE", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Andrew", "JAMES", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Tom", "SELECT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Wilfrid", "CROISARD", "21 Jump Street\nDasmarinas Village, MAKATI"));childs1.add(new Child("Alexandre", "STAUB", "21 Jump Street\nDasmarinas Village, MAKATI"));
//		lfmTrip1.m_childs = childs1;
//		Trip lfmTrip2 = new Trip();
//		lfmTrip2.m_destination = "Makati";
//		lfmTrip2.m_id = 1;
//		ArrayList<Child> childs2 = new ArrayList<Child>();
//		childs2.add(new Child("Basile", "BIHEL", "21 Jump Street\nDasmarinas Village, MAKATI"));childs2.add(new Child("Jules", "DRACOT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs2.add(new Child("Charles", "PAUTIER", "21 Jump Street\nDasmarinas Village, MAKATI"));
//		lfmTrip2.m_childs = childs2;
//		lfmTrips.add(lfmTrip1);
//		lfmTrips.add(lfmTrip2);
//		
//		ArrayList<Trip> gesmTrips = new ArrayList<Trip>();
//		Trip gesmTrip1 = new Trip();
//		gesmTrip1.m_destination = "Paranaque";
//		gesmTrip1.m_id = 2;
//		ArrayList<Child> childs3 = new ArrayList<Child>();
//		childs3.add(new Child("Steve", "MARSHAL", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Kobe", "BRYANT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Charles", "OAKLEY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Nina", "RING", "21 Jump Street\nDasmarinas Village, MAKATI"));childs3.add(new Child("Mabel", "DE CASTRO", "21 Jump Street\nDasmarinas Village, MAKATI"));
//		gesmTrip1.m_childs = childs3;
//		Trip gesmTrip2 = new Trip();
//		gesmTrip2.m_destination = "Salcedo";
//		gesmTrip2.m_id = 3;
//		ArrayList<Child> childs4 = new ArrayList<Child>();
//		childs4.add(new Child("Robert", "De Niro", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Sam", "LOURY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("St�phanie", "DAMIOT", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Malcom", "INSTEAD", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Arnold", "SHWARZY", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Johnny", "DEPP", "21 Jump Street\nDasmarinas Village, MAKATI"));childs4.add(new Child("Deena", "BLINK", "21 Jump Street\nDasmarinas Village, MAKATI"));
//		gesmTrip2.m_childs = childs4;
//		gesmTrips.add(gesmTrip1);
//		gesmTrips.add(gesmTrip2);
		
//		lfm.m_trips = lfmTrips;
//		gesm.m_trips = gesmTrips;
		
//		m_schoolList[0] = lfm;
//		m_schoolList[1] = gesm;
//		
//		m_trips[0] = lfmTrip1;
//		m_trips[1] = lfmTrip2;
//		m_trips[2] = gesmTrip1;
//		m_trips[3] = gesmTrip2;
	}

	public ArrayList<School> GetSchools() 
	{
		return m_schoolDAO.GetSchools();
	}

	public School getSchool(int pi_schoolId) 
	{
		return m_schoolDAO.GetSchool(pi_schoolId);
	}

	public ArrayList<Trip> GetTrips(int pi_schoolId, boolean pi_dayFilter, boolean pi_timeFilter) 
	{
		
		E_DAY day = null;
		int hour = -1, minute = -1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MINUTE, -30);
		if (DriverAppParamHelper.GetInstance().GetTripFilterByDay() == true)
		{
			int iday = calendar.get(Calendar.DAY_OF_WEEK);
			day = E_DAY.FromCalendarInt(iday);
		}
		if (DriverAppParamHelper.GetInstance().GetTripFilterByTime() == true)
		{
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
		}
		return m_tripDAO.GetTrips(pi_schoolId, day, hour, minute);
	}

	public Trip getTrip(int pi_tripId) 
	{
		Trip trip = null;
		switch (pi_tripId)
		{
			case DriverAppParamHelper.SPECIAL_TRIP_HOME_ID:
			{
				trip = GetSpecialHomeTrip(m_context);
				break;
			}
			case DriverAppParamHelper.SPECIAL_TRIP_SCHOOL_ID:
			{
				trip = GetSpecialSchoolTrip(m_context);
				break;
			}
			default:
			{
				trip = m_tripDAO.GetTrip(pi_tripId);
				break;
			}
		}
		return trip;
	}

	static public Trip GetSpecialSchoolTrip(Context pi_context) 
	{
		Trip trip;
		trip = new Trip();
		trip.m_id = DriverAppParamHelper.SPECIAL_TRIP_SCHOOL_ID;
		trip.m_destination = pi_context.getResources().getString(R.string.special_school_trip_name);
		trip.m_isReturn = false;
		trip.m_childs = new ArrayList<Child>();
		return trip;
	}

	static public Trip GetSpecialHomeTrip(Context pi_context) 
	{
		Trip trip;
		trip = new Trip();
		trip.m_id = DriverAppParamHelper.SPECIAL_TRIP_HOME_ID;
		trip.m_destination = pi_context.getResources().getString(R.string.special_home_trip_name);
		trip.m_isReturn = true;
		trip.m_childs = new ArrayList<Child>();
		return trip;
	}

	public ArrayList<Child> GetChilds(int pi_tripId)
	{
		ArrayList<Child> childs = null;
		if ( (pi_tripId != DriverAppParamHelper.SPECIAL_TRIP_HOME_ID) && (pi_tripId != DriverAppParamHelper.SPECIAL_TRIP_SCHOOL_ID) )
		{
			childs = m_tripChildAssociationDAO.GetChilds(pi_tripId);
			childs = m_childDAO.GetChildInfo(childs);
		}
		else
		{
			childs = new ArrayList<Child>();
		}
		
		return childs;
	}

	public ArrayList<Child> GetChilds() 
	{
		ArrayList<Child> childs = m_childDAO.GetAllChilds();
		
		return childs;
	}
	
	public void InsertVersion(int pi_id, int pi_version) throws Exception 
	{
		m_versionDAO.InsertVersion(pi_id, pi_version);
	}
	
	public void InsertChild(int pi_childId, 
							String pi_firstName, 
							String pi_lastName,
							String pi_birthdate,
							String pi_grade,
							String pi_address1,
							String pi_address2,
							String pi_address3,
							String pi_addressDate1,
							String pi_addressDate2,
							String pi_addressDate3, 
							String pi_diaryInfo,
							String pi_usefulInfo) throws Exception
	{
		m_childDAO.InsertChild(pi_childId, pi_firstName, pi_lastName, pi_birthdate, pi_grade, pi_address1, pi_address2, pi_address3, pi_addressDate1, pi_addressDate2, pi_addressDate3, pi_diaryInfo, pi_usefulInfo);
	}
	
	public void InsertSchool(int pi_schoolId, String pi_name) throws Exception
	{
		m_schoolDAO.InsertSchool(pi_schoolId, pi_name);
	}

	public void InsertTripDestination(int pi_id, String pi_destination) throws Exception 
	{
		m_tripDestinationDAO.InsertDestination(pi_id, pi_destination);
	}

	public void InsertTripChildAssociation(int pi_tripId, int pi_childId, int pi_pickupTimeHour, int pi_pickupTimeMinute, int pi_addressId) throws Exception
	{
		m_tripChildAssociationDAO.InsertTripChildAssociation(pi_tripId, pi_childId, pi_pickupTimeHour, pi_pickupTimeMinute, pi_addressId);
	}

	public void InsertTrip(int pi_id, int pi_schoolId, int pi_destinationId, E_DAY pi_day, int pi_hour, int pi_minute, boolean pi_return) throws Exception
	{
		m_tripDAO.InsertTrip(pi_id, pi_schoolId, pi_destinationId, pi_day, pi_hour, pi_minute, pi_return);
	}

	public void InsertDestination(int pi_id, String pi_destination) throws Exception
	{
		m_tripDestinationDAO.InsertDestination(pi_id, pi_destination);
	}

//
//	public void Update(Context pi_context, boolean pi_bForceUpdate) 
//	{
//		DataBaseUpdateThread t = new DataBaseUpdateThread(pi_context, pi_bForceUpdate);
//		t.start();
//	}
//
//	private void SynchDB(Context pi_context) 
//	{
//		DriverAppParamHelper.SetLastDBUpdateTime(pi_context, System.currentTimeMillis());
//	}
//	
//	private class DataBaseUpdateThread extends Thread
//	{
//		Context m_context = null;
//		Boolean m_bForceUpdate = false;
//		public DataBaseUpdateThread(Context pi_context, boolean pi_bForceUpdate) 
//		{
//			m_context = pi_context;
//			m_bForceUpdate = pi_bForceUpdate;
//		}
//
//		@Override
//		public void run() 
//		{
//			m_lock.lock();
//			try
//			{
//				long lastUpdate = DriverAppParamHelper.GetLastDBUpdateTime(m_context);
//				int period = DriverAppParamHelper.GetAutoUpdatePeriod(m_context);
//				if ( (lastUpdate + period > System.currentTimeMillis()) || (m_bForceUpdate == true) )
//				{
//					SynchDB(m_context);
//				}
//			}
//			catch (Exception e)
//			{
//				
//			}
//			finally
//			{
//				m_lock.unlock();
//			}
//
//		}
//	}

	public ArrayList<Integer> GetLocalTableVersion() 
	{
		ArrayList<Integer> tableVersions = new ArrayList<Integer>();
		for (E_TABLE_ID i : E_TABLE_ID.values()) 
		{
			tableVersions.add(m_versionDAO.GetVersion(i.getValue()));
		}
		return tableVersions;
	}

	public void UpdateTableVersion(E_TABLE_ID pi_j, Integer pi_version) 
	{
		m_versionDAO.UpdateVersion(pi_j.getValue(), pi_version);
	}

	public void DeleteTable(E_TABLE_ID j) throws Exception 
	{
		try 
		{
			switch (j) 
			{
				case TABLE_SCHOOL:
				{
					m_schoolDAO.Delete();
					break;
				}
				case TABLE_CHILD:
				{
					m_childDAO.Delete();
					break;
				}
				case TABLE_DESTINATION:
				{
					m_tripDestinationDAO.Delete();
					break;
				}
				case TABLE_TRIP:
				{
					m_tripDAO.Delete();
					break;
				}
				case TABLE_TRIP_CHILD_ASSOCIATION:
				{
					m_tripChildAssociationDAO.Delete();
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	public void BeginTransaction() 
	{
		m_database.beginTransaction();
	}
	
	public void EndTransaction() 
	{
		m_database.endTransaction();
	}

	public void SetTransactionSuccessful() 
	{
		m_database.setTransactionSuccessful();
	}

	public void SetCurrentTrip(Trip pi_trip) 
	{
		m_currentTrip = pi_trip;
	}

	public Trip GetCurrentTrip()
	{
		return m_currentTrip;
	}
}
