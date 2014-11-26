package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.blueskycorner.system.JSONParser;

public class BackEndManager 
{
	public static final String deviceInfoURL = "http://www.";
	public static final String tableVersionURL = "http://testguest.hostei.com/DriverAppVersionApi.php";
	public static final String getTableURL = "http://testguest.hostei.com/DriverAppTableApi.php";
	public static final String KEY_RESULT = "result";
	private static final String VERSION = "version";
	public static final String DEVICE_ID = "deviceid";
	public static final String TABLE_ID = "tableid";
	
	private static final String KEY_TRIP_CHILD_ASSO_TRIP_ID = "tid";
	private static final String KEY_TRIP_CHILD_ASSO_CHILD_ID = "cid";
	private static final String KEY_TRIP_CHILD_ASSO_PICKUP_TIME_HOUR = "h";
	private static final String KEY_TRIP_CHILD_ASSO_PICKUP_TIME_MINUTE = "m";
	private static final String KEY_TRIP_CHILD_ASSO_ADDRESS_ID = "ad";
	
	private static final String KEY_SCHOOL_SCHOOL_ID = "id";
	private static final String KEY_SCHOOL_NAME = "name";
	
	private static final String KEY_CHILD_ID = "id";
	private static final String KEY_CHILD_FIRST_NAME = "fn";
	private static final String KEY_CHILD_LAST_NAME = "ln";
	private static final String KEY_CHILD_ADDRESS1 = "ad1";
	private static final String KEY_CHILD_ADDRESS2 = "ad2";
	private static final String KEY_CHILD_LANGUAGE_ID = "lid";
	private static final String KEY_CHILD_CREATION_DATE = "cd";
	private static final String KEY_CHILD_ADDRESS_DATE = "ad";
	private static final String KEY_CHILD_MONDAY_INFO = "mon";
	private static final String KEY_CHILD_TUESDAY_INFO = "tue";
	private static final String KEY_CHILD_WEDNESDAY_INFO = "wed";
	private static final String KEY_CHILD_THURSDAY_INFO = "thu";
	private static final String KEY_CHILD_FRIDAY_INFO = "fri";
	
	private static final String KEY_TRIP_ID = "id";
	private static final String KEY_TRIP_SCHOOL_ID = "sid";
	private static final String KEY_TRIP_DESTINATION_ID = "did";
	private static final String KEY_TRIP_DAY_ID = "day_id";
	private static final String KEY_TRIP_HOUR = "h";
	private static final String KEY_TRIP_MINUTE = "m";
	private static final String KEY_TRIP_RETURN = "r";
	
	private static final String KEY_TRIP_DESTINATION_ID1 = "id";
	private static final String KEY_TRIP_DESTINATION = "dest";
	
	private ArrayList<IBackEndManagerListener> m_listeners;
	private DeviceInfo m_deviceInfo;
	
	public BackEndManager()
	{
		m_listeners = new ArrayList<IBackEndManagerListener>();
		m_deviceInfo = new DeviceInfo();
	}

    public void addListener(IBackEndManagerListener listener)
    {
	    if (listener != null)
	    {
	    	m_listeners.add(listener);
    	}
    }
   
    public void removeListener(IBackEndManagerListener listener)
    {
	    if (listener != null)
	    {
	    	m_listeners.remove(listener);
	    }
    }
    
    public DeviceInfo getDeviceInfo()
    {
    	return m_deviceInfo;	
    }
    
	private class GetDeviceInfoTask extends AsyncTask<String, Void, JSONObject>
	{
		@Override
		protected JSONObject doInBackground(String... params) 
		{
			JSONObject json = null;
			try 
			{
				List<NameValuePair> paramsJson = new ArrayList<NameValuePair>();
		        paramsJson.add(new BasicNameValuePair("simserialnumber", params[0]));
		        JSONParser jsonParser = new JSONParser();
		        java.lang.System.out.println("Calling back end server : getDevideInfo");
//		        json = jsonParser.getJSONFromUrl(deviceInfoURL, paramsJson);
		        json = GetFakeDeviceInfo();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) 
		{
			Boolean bResult = false;
			
			super.onPostExecute(json);
			try
			{
	            if (json != null)
	            {	            	
	                bResult = json.getBoolean(KEY_RESULT); 
	                // Device info well got
	                if(bResult)
	                {
	                	java.lang.System.out.println("Calling back end server : get device info successful");
//	                	m_deviceInfo.m_id = json.getInt(DeviceInfo.DEVICE_ID);
//	                	m_deviceInfo.m_gateway = json.getString(DeviceInfo.DEVICE_GATEWAY);
	                }
	                // User is a not a guest
	                else
	                {
	                	java.lang.System.out.println("Calling back end server : get device info failed");
	                }
	            }
			}
			catch (Exception e)
			{
				e.printStackTrace();
				bResult = false;
			}
			finally
			{
				for (IBackEndManagerListener o : m_listeners)
			    {
				    o.OnDeviceInfoGrabed(bResult);
			    }
			}
		}
	}
	
	public void launchDeviceInfoGrabing(String pi_simserialnumber)
	{
		try 
		{
			GetDeviceInfoTask task = new GetDeviceInfoTask();
			task.execute(pi_simserialnumber);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
		
	private class DbModificationGrabing extends AsyncTask<Integer, Void, Boolean >
	{
		@Override
		protected Boolean doInBackground(Integer... params) 
		{
			Boolean bResult = false;
			JSONObject json = null;
			ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
			
			try 
			{
				DataManager.GetInstance().BeginTransaction();
				
				JSONParser jsonParser = new JSONParser();
				ArrayList<Integer> localTableVersion = DataManager.GetInstance().GetLocalTableVersion();
				ArrayList<Integer> remoteTableVersion = new ArrayList<Integer>();
				for (E_TABLE_ID i : E_TABLE_ID.values()) 
				{
					List<NameValuePair> paramsJson = new ArrayList<NameValuePair>();
					paramsJson.add(new BasicNameValuePair(DEVICE_ID, Integer.toString(params[0])));
					paramsJson.add(new BasicNameValuePair(TABLE_ID, Integer.toString(i.getValue())));
					java.lang.System.out.println("Calling back end server : updating");
					json = jsonParser.getJSONFromUrl(tableVersionURL, paramsJson);
//					json = GetFakeTableVersion(i.getValue());
					if (json.getBoolean(KEY_RESULT) == true)
					{
						remoteTableVersion.add(json.getInt(VERSION));
					}
				}
				
				int indexStart = 0;
				Boolean bContinue = true;
				while ( (indexStart < E_TABLE_ID.values().length) && (bContinue == true) )
				{
					if (remoteTableVersion.get(indexStart) != localTableVersion.get(indexStart))
					{
						bContinue = false;
					}
					else
					{
						indexStart ++;
					}
				}
				
				for (E_TABLE_ID j : E_TABLE_ID.values())
				{
					JSONObject jsonObj = null;
					if (j.getValue() >= indexStart)
					{
						List<NameValuePair> paramsJson = new ArrayList<NameValuePair>();
						paramsJson.add(new BasicNameValuePair(DEVICE_ID, Integer.toString(params[0])));
						paramsJson.add(new BasicNameValuePair(TABLE_ID, Integer.toString(j.getValue())));
						
						jsonObj = jsonParser.getJSONFromUrl(getTableURL, paramsJson);
//						jsonObj = GetFakeTable(j.getValue());
					}
					jsonList.add(jsonObj);
				}
				
				for (E_TABLE_ID j : E_TABLE_ID.values())
				{
					if (j.getValue() >= indexStart)
					{
						DataManager.GetInstance().DeleteTable(j);
						CreateObject(j, jsonList.get(j.getValue()));
						DataManager.GetInstance().UpdateTableVersion(j,remoteTableVersion.get(j.getValue()));
					}
				}
				DataManager.GetInstance().SetTransactionSuccessful();
				bResult = true;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				DataManager.GetInstance().EndTransaction();
			}
			
			return bResult;
		}

		@Override
		protected void onPostExecute(Boolean pi_bResult) 
		{
			super.onPostExecute(pi_bResult);
	        // check for  response
        	// If server well reached
			try
			{
                if (pi_bResult == true)
                {
                	java.lang.System.out.println("Calling back end server : updating db successful");
                }
                // User not well updated
                else
                {
                	java.lang.System.out.println("Calling back end server : updating fail");
                }     
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				for (IBackEndManagerListener o : m_listeners)
			    {
				    o.OnDbModificationGrabed(pi_bResult);
			    }
			}
		}
	}

	public void launchDbModificationGrabing(int pi_deviceId)
	{
		DbModificationGrabing task = new DbModificationGrabing();
		task.execute(pi_deviceId);
	}
	
	public void CreateObject(E_TABLE_ID j, JSONObject pi_jsonObject) throws Exception 
	{
		try
		{
			switch (j.getValue())
			{
				case 0:
				{
					CreateSchoolObjects(pi_jsonObject);
					DriverAppParamHelper.GetInstance().SetLastSchoolId(DriverAppParamHelper.NO_SCHOOL_ID);
					break;
				}
				case 1:
				{
					CreateTripDestinationObjects(pi_jsonObject);
					break;
				}
				case 2:
				{
					CreateTripObjects(pi_jsonObject);
					DriverAppParamHelper.GetInstance().SetLastTripId(DriverAppParamHelper.NO_TRIP_ID);
					break;
				}
				case 3:
				{
					CreateChildObjects(pi_jsonObject);
					break;
				}
				case 4:
				{
					CreateTripChildObjects(pi_jsonObject);
					break;
				}
			}
		}
		catch (Exception e)
		{
			Log.e("BackEnManager", "CreateObject");
			throw e;
		}
	}

	private void CreateTripDestinationObjects(JSONObject pi_jsonObject) throws Exception 
	{
		try {
			JSONArray schools = pi_jsonObject.getJSONArray(TripDestinationDAO.TABLE);
			for (int i=0; i<schools.length(); i++) 
			{
				JSONObject o = (JSONObject) schools.get(i);
				int id = o.getInt(KEY_TRIP_DESTINATION_ID1);
				String destination = o.getString(KEY_TRIP_DESTINATION);
				DataManager.GetInstance().InsertTripDestination(id, destination);
			}
		} 
		catch (Exception e) 
		{
			Log.e("BackEnManager", "CreateTripDestinationObjects");
			throw e;
		}
	}

	public void CreateTripChildObjects(JSONObject jsonObject) throws Exception
	{
		try {
			JSONArray schools = jsonObject.getJSONArray(TripChildAssociationDAO.TABLE);
			for (int i=0; i<schools.length(); i++) 
			{
				JSONObject o = (JSONObject) schools.get(i);
				int tripId = o.getInt(KEY_TRIP_CHILD_ASSO_TRIP_ID);
				int childId = o.getInt(KEY_TRIP_CHILD_ASSO_CHILD_ID);
				int pickupTimeHour = o.getInt(KEY_TRIP_CHILD_ASSO_PICKUP_TIME_HOUR);
				int pickupTimeMinute = o.getInt(KEY_TRIP_CHILD_ASSO_PICKUP_TIME_MINUTE);
				int addressId = o.getInt(KEY_TRIP_CHILD_ASSO_ADDRESS_ID);
				DataManager.GetInstance().InsertTripChildAssociation(tripId, childId, pickupTimeHour, pickupTimeMinute, addressId);
			}
		} catch (Exception e) 
		{
			Log.e("BackEnManager", "CreateTripChildObjects");
			throw e;
		}
	}

	public void CreateChildObjects(JSONObject jsonObject) throws Exception 
	{
		try {
			JSONArray childs = jsonObject.getJSONArray(ChildDAO.TABLE);
			for (int i=0; i<childs.length(); i++) 
			{
				JSONObject o = (JSONObject) childs.get(i);
				int childId = o.getInt(KEY_CHILD_ID);
				String firstName = o.getString(KEY_CHILD_FIRST_NAME);
				String lastName = o.getString(KEY_CHILD_LAST_NAME);
				String address1 = o.getString(KEY_CHILD_ADDRESS1);
				String address2 = o.getString(KEY_CHILD_ADDRESS2);
				int languageId = o.getInt(KEY_CHILD_LANGUAGE_ID);
				String creationDate = o.getString(KEY_CHILD_CREATION_DATE);
				String modificationDate = o.getString(KEY_CHILD_ADDRESS_DATE);
				String mondayInfo = o.getString(KEY_CHILD_MONDAY_INFO);
				String tuesdayInfo = o.getString(KEY_CHILD_TUESDAY_INFO);
				String wednesdayInfo = o.getString(KEY_CHILD_WEDNESDAY_INFO);
				String thursdayInfo = o.getString(KEY_CHILD_THURSDAY_INFO);
				String fridayInfo = o.getString(KEY_CHILD_FRIDAY_INFO);
				DataManager.GetInstance().InsertChild(childId, firstName, lastName, address1, address2, languageId, creationDate, modificationDate, mondayInfo, tuesdayInfo, wednesdayInfo, thursdayInfo, fridayInfo);
			}
		} 
		catch (Exception e) 
		{
			Log.e("BackEnManager", "CreateChildObjects");
			throw e;
		}
	}

	public void CreateTripObjects(JSONObject jsonObject) throws Exception 
	{
		try {
			JSONArray trip = jsonObject.getJSONArray(TripDAO.TABLE);
			for (int i=0; i<trip.length(); i++) 
			{
				JSONObject o = (JSONObject) trip.get(i);
				int id = o.getInt(KEY_TRIP_ID);
				int schoolId = o.getInt(KEY_TRIP_SCHOOL_ID);
				int destinationId = o.getInt(KEY_TRIP_DESTINATION_ID);
				E_DAY day = E_DAY.FromInt(o.getInt(KEY_TRIP_DAY_ID));
				int hour = o.getInt(KEY_TRIP_HOUR);
				int minute = o.getInt(KEY_TRIP_MINUTE);
				boolean bReturn = o.getBoolean(KEY_TRIP_RETURN);
				DataManager.GetInstance().InsertTrip(id, schoolId, destinationId, day, hour, minute, bReturn);
			}
		} catch (Exception e) 
		{
			Log.e("BackEnManager", "CreateTripObjects");
			throw e;
		}
	}

	public void CreateSchoolObjects(JSONObject jsonObject) throws Exception 
	{
		try {
			JSONArray schools = jsonObject.getJSONArray(SchoolDAO.TABLE);
			for (int i=0; i<schools.length(); i++) 
			{
				JSONObject o = (JSONObject) schools.get(i);
				int id = o.getInt(KEY_SCHOOL_SCHOOL_ID);
				String name = o.getString(KEY_SCHOOL_NAME);
				DataManager.GetInstance().InsertSchool(id, name);
			}
//			ArrayList<Integer> l = new ArrayList<Integer>();
//			int i = l.get(5);
		} 
		catch (Exception e) 
		{
			Log.e("BackEnManager", "CreateSchoolObjects");
			throw e;
		}
	}

	public JSONObject GetFakeTable(int value) 
	{
		String sJson = null;
		JSONObject json = null;
		switch (value)
		{
			case 0:
			{
				sJson = "{\"result\":true,\"school\":[{\"id\":\"0\",\"name\":\"LFM\"},{\"id\":\"1\",\"name\":\"Chinese School\"}]}";
				break;
			}
			case 1:
			{
				sJson = "{\"result\":true,\"trip_destination\":[{\"id\":\"0\",\"dest\":\"Makati\"},{\"id\":\"1\",\"dest\":\"Alabang up\"}]}";
				break;
			}
			case 2:
			{
				sJson = "{\"result\":true,\"trip\":[{\"id\":\"0\",\"sid\":\"0\",\"did\":\"0\",\"day_id\":\"1\",\"h\":\"13\",\"m\":\"30\",\"r\":true},{\"id\":\"1\",\"sid\":\"0\",\"did\":\"0\",\"day_id\":\"1\",\"h\":\"6\",\"m\":\"00\",\"r\":false}]}";
				break;
			}
			case 3:
			{
				sJson = "{\"result\":true,\"child\":[{\"id\":\"0\",\"fn\":\"john\",\"ln\":\"travolta\",\"ad1\":\"22 Jump street,MAKATI\",\"ad2\":\"1813 Santan street\",\"lid\":\"0\",\"cd\":\"2014:09:01\",\"ad\":\"2014:10:21\",\"mon\":\"foot until 3:30\",\"tue\":\"...\",\"wed\":\"...\",\"thu\":\"...\",\"fri\":\"...\"},{\"id\":\"1\",\"fn\":\"steve\",\"ln\":\"jobs\",\"ad1\":\"42 Kalamansi street\",\"ad2\":\"1795 Accacia street\",\"lid\":\"0\",\"cd\":\"2014:09:01\",\"ad\":\"2014:10:21\",\"mon\":\"hip-hop until 3:30\",\"tue\":\"...\",\"wed\":\"...\",\"thu\":\"...\",\"fri\":\"...\"}]}";
				break;
			}
			case 4:
			{
				sJson = "{\"result\":true,\"trip_child_association\":[{\"tid\":\"0\",\"cid\":\"0\",\"h\":\"13\",\"m\":\"50\",\"ad\":\"0\"},{\"tid\":\"0\",\"cid\":\"1\",\"h\":\"14\",\"m\":\"10\",\"ad\":\"1\"}]}";
				break;
			}
		}
		
		try 
		{
			json = new JSONObject(sJson);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return json;
	}

	public JSONObject GetFakeTableVersion(int value) 
	{
		String sJson = null;
		switch (value) 
		{
			case 0:
			{
				sJson = "{\"result\":true,\"version\":\"5\"}";
				break;
			}
			case 1:
			{
				sJson = "{\"result\":true,\"version\":\"1\"}";
				break;
			}
			case 2:
			{
				sJson = "{\"result\":true,\"version\":\"1\"}";
				break;
			}
			case 3:
			{
				sJson = "{\"result\":true,\"version\":\"5\"}";
				break;
			}
			case 4:
			{
				sJson = "{\"result\":true,\"version\":\"1\"}";
				break;
			}
		}
		
		JSONObject json = null;
		try 
		{
			json = new JSONObject(sJson);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return json;
	}

	private JSONObject GetFakeDeviceInfo() 
	{
		String sJson = "{\"result\":true,\"id\":\"43\",\"gateway\":\"09267420123\"}";
		JSONObject json = null;
		try 
		{
			json = new JSONObject(sJson);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return json;
	}

}
