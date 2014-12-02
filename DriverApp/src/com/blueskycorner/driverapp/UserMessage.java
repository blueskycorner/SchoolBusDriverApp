package com.blueskycorner.driverapp;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMessage implements Parcelable
{
	public E_SENDER m_sender = E_SENDER.SENDER_PARENT;
	public String m_message = "";
	public E_USER_ACTION m_action = E_USER_ACTION.ACTION_ACK;

	public UserMessage() 
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
		dest.writeInt(m_sender.getValue());
		dest.writeString(m_message);
		dest.writeInt(m_action.getValue());
	}
	
	/**Ctor from Parcel, reads back fields IN THE ORDER they were written */
	public UserMessage(Parcel pc)
	{
		m_sender = E_SENDER.FromInt(pc.readInt());
		m_message =  pc.readString();
		m_action = E_USER_ACTION.FromInt(pc.readInt());
	}

	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<UserMessage> CREATOR = 
			new Parcelable.Creator<UserMessage>() 
			{
				public UserMessage createFromParcel(Parcel pc) 
				{return new UserMessage(pc);}
				
				public UserMessage[] newArray(int size) 
				{return new UserMessage[size];}
	};
}
