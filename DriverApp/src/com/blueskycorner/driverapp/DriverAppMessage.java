package com.blueskycorner.driverapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverAppMessage implements Parcelable
{
	E_SMS_FROM m_from;
	int m_fromId;
	String m_cellNumer;
	String m_text;
//	E_CRITICITY m_criticity;
	
	public DriverAppMessage()
	{
		
	}
	
	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeInt(m_from.getValue());
		dest.writeInt(m_fromId);
		dest.writeString(m_cellNumer);
		dest.writeString(m_text);
	}
	
	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<DriverAppMessage> CREATOR = 
			new Parcelable.Creator<DriverAppMessage>() 
			{
				public DriverAppMessage createFromParcel(Parcel pc) 
				{return new DriverAppMessage(pc);}
				
				public DriverAppMessage[] newArray(int size) 
				{return new DriverAppMessage[size];}
	};
	
	/**Ctor from Parcel, reads back fields IN THE ORDER they were written */
	public DriverAppMessage(Parcel pc)
	{
		m_from = E_SMS_FROM.FromInt(pc.readInt());
		m_fromId =  pc.readInt();
		m_cellNumer = pc.readString();
		m_text = pc.readString();
	}

}
