package com.blueskycorner.driverapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverAppSmsMessage implements Parcelable
{
	static final int SEPARATOR_INT = '|';
	
	private static final int TYPE_FIELD_NUMBER = 0;
	private static final int CHILD_FIELD_NUMBER = 1;
	
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
		return GetField(TYPE_FIELD_NUMBER);
	}
	
	private int GetField(int pi_field)
	{
		int type = -1;
		String s = GetStringField(pi_field);
		if (s != null)
		{			
			type = Integer.parseInt(s);
		}
		return type;
	}

	private String GetStringField(int typeFieldNumber) 
	{
		String s = null;
		int first = -1;
		if (typeFieldNumber != 0)
		{
			first = GetSeparatorIndex(typeFieldNumber);
		}
		
		int second = GetSeparatorIndex(typeFieldNumber+1);
		
		if (first < second)
		{			
			s = m_body.substring(first+1, second);
		}
		
		return s;
	}

	private int GetSeparatorIndex(int typeFieldNumber) 
	{
		int start = -1;
		for (int i=0; i<typeFieldNumber; i++)
		{
			start = m_body.indexOf(SEPARATOR_INT, start+1);
		}
		return start;
	}

	public int GetChildId() 
	{
		return GetField(CHILD_FIELD_NUMBER);	
	}

}
