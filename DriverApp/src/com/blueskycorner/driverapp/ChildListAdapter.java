package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Path.FillType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ChildListAdapter extends BaseAdapter 
{
	private Trip m_trip = null;
	private Context m_context = null;
	
	public ChildListAdapter(Context pi_context)
	{
		m_context = pi_context;
	}
	
	public void SetTrip(Trip pi_trip)
	{
		m_trip = pi_trip;
	}
	
	@Override
	public int getCount()
	{
		int count = 0;
		
		if (m_trip != null)
		{
			if (m_trip.m_childs != null)
			{
				count = m_trip.m_childs.size();
			}
		}
		
		return count;
	}

	@Override
	public Object getItem(int arg0) 
	{
		Child child = null;
		
		if (m_trip != null)
		{
			child = m_trip.m_childs.get(arg0);
		}
		return child;
	}

	@Override
	public long getItemId(int arg0) 
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) 
	{
		// Get a layout inflater
		LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View listView;

		if (arg1 == null) {

			listView = new View(m_context);
		} 
		else 
		{
			listView = (View) arg1;
		}

		// get layout from result.xml
		listView = inflater.inflate(R.layout.child_row, arg2, false);
		TextView firstName = (TextView) listView.findViewById(R.id.textViewFirstName);
		TextView lastName = (TextView) listView.findViewById(R.id.textViewLastName);
		CheckBox present = (CheckBox) listView.findViewById(R.id.checkBoxPresent);
		
		firstName.setText(m_trip.m_childs.get(arg0).m_firstName);
		lastName.setText(m_trip.m_childs.get(arg0).m_lastName);
		present.setChecked(m_trip.m_childs.get(arg0).m_isPresent);
		
		if (m_trip.m_isReturn == true)
		{
			present.setVisibility(View.VISIBLE);
		}
		else
		{
			present.setVisibility(View.INVISIBLE);
		}

		return listView;
	}

	
}
