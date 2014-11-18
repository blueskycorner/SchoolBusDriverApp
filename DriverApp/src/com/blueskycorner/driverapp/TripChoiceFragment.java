package com.blueskycorner.driverapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

public class TripChoiceFragment extends DriverAppFragment implements OnClickListener, OnItemClickListener
{
	public static final String NAME = "TRIP_CHOICE_FRAGMENT";
	private static final String TRIP_INDEX = "TRIP_INDEX";
	private static final String SCHOOL_ID = "SCHOOL_ID";
	private static final String TRIP_ID = "TRIP_ID";

	private Button m_buttonSchool = null;
	private Button m_buttonTrip = null;
	private Button m_buttonStartTrip = null;
	private ListView m_childList = null;
	
	private School m_school = null;
	private Trip m_trip = null;

	private IDriverAppCommunicator m_communicator = null;
	private Activity m_activity = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.trip_choice_framgent, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		int schoolId = DriverAppParamHelper.GetLastSchoolId(getActivity());
		int tripId = DriverAppParamHelper.GetLastTripId(getActivity());
		
		if (schoolId != -1)
		{
			m_school = DataManager.GetInstance().getSchool(schoolId);
		}
		
		if (tripId != -1)
		{
			m_trip = DataManager.GetInstance().getTrip(tripId);
			ArrayList<Child> list = DataManager.GetInstance().GetChilds(m_trip.m_id);
			m_trip.Init(list);
		}
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
		
		m_buttonSchool = (Button)getActivity().findViewById(R.id.buttonSchool);	
		m_buttonSchool.setOnClickListener(this);
		SetSchoolButtonText(m_school);
		
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
			tripName = pi_trip.m_destination + " @ " + pi_trip.GetTime();
		}
		else
		{
			tripName = getActivity().getResources().getString(R.string.choose_one);
		}
		String text = getString(R.string.trip) + " :  " + tripName;
		m_buttonTrip.setText(text);
	}

	private void SetSchoolButtonText(School pi_school) 
	{
		String schoolName = "";
		if (pi_school != null)
		{
			schoolName = pi_school.m_name;
		}
		else
		{
			schoolName = getActivity().getResources().getString(R.string.choose_one);
		}
		String text = getString(R.string.school) + " :  " + schoolName;
		m_buttonSchool.setText(text);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.buttonSchool:
			{
				final ArrayList<School> schools = DataManager.GetInstance().GetSchools();
				String[] schoolNames = GetSchoolNames(schools);
				final int currentIndex = GetCurrentSchoolIndex(schools);
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		    	builder.setTitle(R.string.school);
		    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		     	{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				});
		     	builder.setSingleChoiceItems(schoolNames,currentIndex, new DialogInterface.OnClickListener() 
		     	{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						m_school = schools.get(which);
						SetSchoolButtonText(m_school);
						if (which != currentIndex)
						{
							m_trip = null;
							SetTripButtonText(m_trip);
							((ChildListAdapter)m_childList.getAdapter()).SetTrip(null);
						}
						m_childList.invalidateViews();
						DriverAppParamHelper.SetLastSchoolId(getActivity(),m_school.m_id);
						dialog.dismiss();
					}
				});
		    	builder.show();
				break;
			}
			case R.id.buttonTrip:
			{
				if (m_school != null)
				{
					final ArrayList<Trip> trips = DataManager.GetInstance().GetTrips(m_school.m_id, true, true);
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
							DriverAppParamHelper.SetLastTripId(getActivity(),m_trip.m_id);
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
				if (m_school == null)
				{
					ChooseSchoolFirst();
				} 
				else if (m_trip == null)
				{
					ChooseTripFirst();
				}
				else
				{
					m_communicator.TripStarted(m_trip);
				}
				
				break;
			}
		}
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
		if (m_school != null)
		{
			tripNames = new String[pi_trips.size()];
			for (int i=0; i<pi_trips.size(); i++)
			{
				tripNames[i] = pi_trips.get(i).m_destination;
			}
		}
		return tripNames;
	}

	private int GetCurrentSchoolIndex(ArrayList<School> pi_schools)
	{
		int index = -1;
		if (m_school != null)
		{
			for (int i=0; i<pi_schools.size(); i++) 
			{
				if (pi_schools.get(i).m_id == m_school.m_id)
				{
					index = i;
				}
			}
		}
		return index;
	}

	private String[] GetSchoolNames(ArrayList<School> pi_schools) 
	{
		String[] schoolNames = new String[pi_schools.size()];
		for (int i=0; i<pi_schools.size(); i++)
		{
			schoolNames[i] = pi_schools.get(i).m_name;
		}
		return schoolNames;
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
	public void onAttach(Activity activity) 
	{
		m_activity = activity;
		m_communicator  = (IDriverAppCommunicator) activity;
		super.onAttach(activity);
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
