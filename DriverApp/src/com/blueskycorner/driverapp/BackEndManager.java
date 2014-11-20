package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.blueskycorner.system.JSONParser;

public class BackEndManager 
{
	public static final String deviceInfoURL = "http://www.";
	public static final String tableVersionURL = "http://www.";
	public static final String getTableURL = "http://www.";
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
//					json = jsonParser.getJSONFromUrl(tableVersionURL, paramsJson);
					json = GetFakeTableVersion(i.getValue());
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
						
						DataManager.GetInstance().DropTable(j);
						DataManager.GetInstance().UpdateTableVersion(j,remoteTableVersion.get(j.getValue()));
//						jsonObj = jsonParser.getJSONFromUrl(getTableURL, paramsJson);
						jsonObj = GetFakeTable(j.getValue());
					}
					jsonList.add(jsonObj);
				}
				
				for (E_TABLE_ID j : E_TABLE_ID.values())
				{
					if (j.getValue() >= indexStart)
					{
						CreateObject(j, jsonList.get(j.getValue()));
					}
				}
				bResult = true;
				DataManager.GetInstance().SetTransactionSuccessful();
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
	
	public void CreateObject(E_TABLE_ID j, JSONObject pi_jsonObject) 
	{
		switch (j.getValue())
		{
			case 0:
			{
				CreateSchoolObjects(pi_jsonObject);
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

	private void CreateTripDestinationObjects(JSONObject pi_jsonObject) {
		// TODO Auto-generated method stub
		
	}

	public void CreateTripChildObjects(JSONObject jsonObject)
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Child> CreateChildObjects(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Trip> CreateTripObjects(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public void CreateSchoolObjects(JSONObject jsonObject) 
	{
		try {
			JSONArray schools = jsonObject.getJSONArray(SchoolDAO.TABLE);
			for (int i=0; i<schools.length(); i++) 
			{
				JSONObject o = (JSONObject) schools.get(i);
				int id = o.getInt(KEY_SCHOOL_SCHOOL_ID);
				String name = o.getString(KEY_SCHOOL_NAME);
//				DataManager.GetInstance().InsertSchool(id, name);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				sJson = "{\"result\":true,\"school\":[{\"id\":\"0\",\"name\":\"LFM\"},{\"id\":\"1\",\"name\":\"GESM\"}]}";
				break;
			}
			case 1:
			{
				sJson = "{\"result\":true,\"school\":[{\"id\":\"0\",\"name\":\"LFM\"},{\"id\":\"1\",\"name\":\"GESM\"}]}";
				break;
			}
			case 2:
			{
				sJson = "{\"result\":true,\"school\":[{\"id\":\"0\",\"name\":\"LFM\"},{\"id\":\"1\",\"name\":\"GESM\"}]}";
				break;
			}
			case 3:
			{
				sJson = "{\"result\":true,\"school\":[{\"id\":\"0\",\"name\":\"LFM\"},{\"id\":\"1\",\"name\":\"GESM\"}]}";
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
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public JSONObject GetFakeTableVersion(int value) 
	{
		String sJson = "{\"result\":true,\"version\":\"1\"}";
		JSONObject json = null;
		try {
			json = new JSONObject(sJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	private JSONObject GetFakeDeviceInfo() 
	{
		String sJson = "{\"result\":true,\"id\":\"43\",\"gateway\":\"09267420123\"}";
		JSONObject json = null;
		try {
			json = new JSONObject(sJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
