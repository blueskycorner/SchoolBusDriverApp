package com.blueskycorner.driverapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverAppSmsMessage implements Parcelable
{
	public String m_body = "";
	public long m_date = 0;
	
	public DriverAppSmsMessage()
	{
		
	}
	
	public boolean HasToBeShown() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(m_body);
		dest.writeLong(m_date);
	}
	
	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<DriverAppSmsMessage> CREATOR = 
			new Parcelable.Creator<DriverAppSmsMessage>() 
			{
				public DriverAppSmsMessage createFromParcel(Parcel pc) 
				{return new DriverAppSmsMessage(pc);}
				
				public DriverAppSmsMessage[] newArray(int size) 
				{return new DriverAppSmsMessage[size];}
	};
	
	/**Ctor from Parcel, reads back fields IN THE ORDER they were written */
	public DriverAppSmsMessage(Parcel pc)
	{
		m_body = pc.readString();
		m_date =  pc.readInt();
	}

	public CharSequence GetText() 
	{
		String s = "";
		return s;
	}

	public int GetType() 
	{
		// TODO Auto-generated method stub
		return 3;
	}

	public int GetChildId() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
