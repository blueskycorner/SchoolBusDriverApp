package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
	
	private Context m_context = null;
	private ArrayList<IBackEndManagerListener> m_listeners;
	private DeviceInfo m_deviceInfo;
	
	public BackEndManager(Context pi_context)
	{
		m_context = pi_context;
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
//				JSONParser jsonParser = new JSONParser();
//				int[] localTableVersion = DataManager.GetInstance().GetLocalTableVersion();
//				int[] remoteTableVersion = new int[DataManager.TABLE_ACCOUNT];
//				for (E_TABLE_ID i : E_TABLE_ID.values()) 
//				{
//					List<NameValuePair> paramsJson = new ArrayList<NameValuePair>();
//					paramsJson.add(new BasicNameValuePair(DEVICE_ID, Integer.toString(params[0])));
//					paramsJson.add(new BasicNameValuePair(TABLE_ID, Integer.toString(i.getValue())));
//					java.lang.System.out.println("Calling back end server : updating");
////					json = jsonParser.getJSONFromUrl(tableVersionURL, paramsJson);
//					json = GetFakeTableVersion(i.getValue());
//					if (json.getBoolean(KEY_RESULT) == true)
//					{
//						remoteTableVersion[i.getValue()] = json.getInt(VERSION);
//					}
//				}
//				
//				int indexStart = 0;
//				Boolean bContinue = true;
//				while ( (indexStart < DataManager.TABLE_ACCOUNT) && (bContinue == true) )
//				{
//					if (remoteTableVersion[indexStart] != localTableVersion[indexStart])
//					{
//						bContinue = false;
//					}
//					else
//					{
//						indexStart ++;
//					}
//				}
//				
//				for (E_TABLE_ID j : E_TABLE_ID.values())
//				{
//					JSONObject jsonObj = null;
//					if (j.getValue() >= indexStart)
//					{
//						List<NameValuePair> paramsJson = new ArrayList<NameValuePair>();
//						paramsJson.add(new BasicNameValuePair(DEVICE_ID, Integer.toString(params[0])));
//						paramsJson.add(new BasicNameValuePair(TABLE_ID, Integer.toString(j.getValue())));
//						
////						jsonObj = jsonParser.getJSONFromUrl(getTableURL, paramsJson);
//						jsonObj = GetFakeTable(j.getValue());
//					}
//					jsonList.add(jsonObj);
//				}
//				
//				ArrayList<School> schoolList = CreateSchoolObjects(jsonList.get(E_TABLE_ID.ID_SCHOOL.getValue()));
//				ArrayList<Trip> tripList = CreateTripObjects(jsonList.get(E_TABLE_ID.ID_TRIP.getValue()));
//				ArrayList<Child> childList = CreateChildObjects(jsonList.get(E_TABLE_ID.ID_CHILD.getValue()));
//				tableContainer c = new tableContainer(remoteTableVersion, schoolList, tripList, childList);
//				bResult = DataManager.GetInstance().Update(c);
				bResult = true;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
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
	
	public ArrayList<Child> CreateChildObjects(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Trip> CreateTripObjects(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<School> CreateSchoolObjects(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONObject GetFakeTable(int value) 
	{
		// TODO Auto-generated method stub
		return null;
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
