package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TripChoiceFragment extends DriverAppFragment implements OnClickListener, OnItemClickListener
{
	public static final String NAME = "TRIP_CHOICE_FRAGMENT";

	private Button m_buttonTrip = null;
	private Button m_buttonStartTrip = null;
	private ListView m_childList = null;
		
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.trip_choice_framgent, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		m_fragmentName = NAME;
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
				
		m_buttonTrip = (Button)getActivity().findViewById(R.id.buttonTrip);	
		m_buttonTrip.setOnClickListener(this);
		SetTripButtonText(m_trip);
		
		m_buttonStartTrip = (Button)getActivity().findViewById(R.id.buttonStartTrip);
		m_buttonStartTrip.setOnClickListener(this);
		
		m_childList = (ListView)getActivity().findViewById(R.id.listViewKids);
		ChildListAdapter adapter = new ChildListAdapter(getActivity());
		if (m_trip != null)
		{
			adapter.SetTrip(m_trip);
		}
		
		m_childList.setAdapter(adapter);
		m_childList.setOnItemClickListener(this);
	}

	private void SetTripButtonText(Trip pi_trip) 
	{	
		String tripName = "";
		if (pi_trip != null)
		{
			tripName = pi_trip.ToString();
		}
		else
		{
			tripName = getActivity().getResources().getString(R.string.choose_one);
		}
		String text = getString(R.string.trip) + " :  " + tripName;
		m_buttonTrip.setText(text);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.buttonTrip:
			{
				if (GetSchoolId() != DriverAppParamHelper.NO_SCHOOL_ID)
				{
					final ArrayList<Trip> trips = DataManager.GetInstance().GetTrips(GetSchoolId(), true, true);
					
					Trip specialTripHome = DataManager.GetSpecialHomeTrip(getActivity());
					Trip specialTripSchool = DataManager.GetSpecialSchoolTrip(getActivity());
					
					trips.add(specialTripHome);
					trips.add(specialTripSchool);
					
					String[] tripNames = GetTripNames(trips);
					int currentIndex = GetCurrentTripIndex(trips);
					AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			    	builder.setTitle(R.string.trip);
			    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
			     	{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.dismiss();
						}
					});
			     	builder.setSingleChoiceItems(tripNames,currentIndex, new DialogInterface.OnClickListener() 
			     	{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							m_trip = trips.get(which);
							SetTripButtonText(m_trip);
							ArrayList<Child> list = DataManager.GetInstance().GetChilds(m_trip.m_id);
							m_trip.Init(list);
							((ChildListAdapter)m_childList.getAdapter()).SetTrip(m_trip);
							m_childList.invalidateViews();
							DriverAppParamHelper.GetInstance().SetLastTripId(m_trip.m_id);
							m_comm.TripSelected(m_trip);
							dialog.dismiss();
						}
					});
			    	builder.show();
				}
				else
				{
					ChooseSchoolFirst();
				}
				break;
			}
			case R.id.buttonStartTrip:
			{
				if (GetSchoolId() == DriverAppParamHelper.NO_SCHOOL_ID)
				{
					ChooseSchoolFirst();
				} 
				else if (m_trip == null)
				{
					ChooseTripFirst();
				}
				else if (m_trip.GetRemainChildCount() == 0)
				{
					NoChildsInThisTrip();
				}
				else
				{
					m_comm.TripStarted();
				}
				
				break;
			}
		}
	}

	private void NoChildsInThisTrip() 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.no_childs_in_this_trip);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private int GetSchoolId() 
	{
		return DriverAppParamHelper.GetInstance().GetLastSchoolId();
	}

	private void ChooseTripFirst() 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.choose_a_trip_first);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void ChooseSchoolFirst() 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.choose_a_school_first);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private int GetCurrentTripIndex(ArrayList<Trip> pi_trips) 
	{
		int index = -1;
		if (m_trip != null)
		{
			for (int i=0; i<pi_trips.size(); i++) 
			{
				if (pi_trips.get(i).m_id == m_trip.m_id)
				{
					index = i;
				}
			}
		}
		return index;
	}

	private String[] GetTripNames(ArrayList<Trip> pi_trips) 
	{
		String[] tripNames = null;
		if (GetSchoolId() != DriverAppParamHelper.NO_SCHOOL_ID)
		{
			tripNames = new String[pi_trips.size()];
			for (int i=0; i<pi_trips.size(); i++)
			{
				tripNames[i] = pi_trips.get(i).ToString();
			}
		}
		return tripNames;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		if (m_trip.m_isReturn == true)
		{
			boolean b = m_trip.m_childs.get(arg2).m_isPresent;
			m_trip.m_childs.get(arg2).m_isPresent = !b;
			m_childList.invalidateViews();
		}
	}

	@Override
	public void BackPressed() 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    	builder.setTitle(R.string.warning);
    	builder.setMessage(R.string.confirmation_quit);
    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
     	{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
     	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
     	{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				m_activity.finish();
			}
		});
    	builder.show();

	}

	@Override
	public ViewGroup GetViewGroup()
	{
		ViewGroup l = (ViewGroup) getActivity().findViewById(R.id.trip_choice_fragment);
		return l;
	}

	@Override
	public void RefreshState(Child pi_child) 
	{
		
	}
}
