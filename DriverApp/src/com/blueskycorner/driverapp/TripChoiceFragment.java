package com.blueskycorner.driverapp;

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

public class TripChoiceFragment extends DriverAppFragment implements OnClickListener, OnItemClickListener, OnCheckedChangeListener
{
	public static final String NAME = "TRIP_CHOICE_FRAGMENT";
	private static final String SCHOOL_INDEX = "SCHOOL_INDEX";
	private static final String TRIP_INDEX = "TRIP_INDEX";
	private static final String SCHOOL_ID = "SCHOOL_ID";
	private static final String TRIP_ID = "TRIP_ID";

	private Button m_buttonSchool = null;
	private Button m_buttonTrip = null;
	private Button m_buttonStartTrip = null;
	private ToggleButton m_buttonReturn = null;
	private ListView m_childList = null;
	
	private School m_school = null;
	private Trip m_trip = null;

	private int m_schoolIndex = -1;
	private int m_tripIndex = -1;
	private DriverAppCommunicator m_communicator = null;
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
		
		if (savedInstanceState != null)
		{
			m_schoolIndex = savedInstanceState.getInt(SCHOOL_INDEX);
			m_tripIndex = savedInstanceState.getInt(TRIP_INDEX);
		}
		
		int schoolId = DriverAppParamHelper.GetLastSchoolId(getActivity());
		int tripId = DriverAppParamHelper.GetLastTripId(getActivity());
		
		if (schoolId != -1)
		{
			m_school = DataManager.GetInstance().getSchool(schoolId);
		}
		
		if (tripId != -1)
		{
			m_trip = DataManager.GetInstance().getTrip(tripId);
			m_trip.Init();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		outState.putInt(SCHOOL_INDEX, m_schoolIndex);
		outState.putInt(TRIP_INDEX, m_tripIndex);
		
		if (m_school != null)
		{
			outState.putInt(SCHOOL_ID, m_school.m_id);
		}
		else
		{
			outState.putInt(SCHOOL_ID, -1);
		}
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
		
		m_buttonReturn = (ToggleButton)getActivity().findViewById(R.id.toggleButtonReturn);
		m_buttonReturn.setOnCheckedChangeListener(this);
		
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
			tripName = pi_trip.m_name;
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
				String[] schoolNames = GetSchoolNames();
				int currentIndex = GetCurrentSchoolIndex();
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
						m_school = TripChoiceFragment.this.GetSchool(which);
						SetSchoolButtonText(m_school);
						((ChildListAdapter)m_childList.getAdapter()).SetTrip(null);
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
					String[] tripNames = GetTripNames();
					int currentIndex = GetCurrentTripIndex();
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
							m_trip = TripChoiceFragment.this.GetTrip(which);
							m_trip.Init();
							SetTripButtonText(m_trip);
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

	protected Trip GetTrip(int pi_index) 
	{
		Trip trip = null;
		if (m_school != null)
		{
			trip = m_school.m_trips.get(pi_index);
			m_tripIndex = pi_index;
		}
		return trip;
	}

	private int GetCurrentTripIndex() 
	{
		return m_tripIndex;
	}

	private String[] GetTripNames() 
	{
		String[] tripNames = null;
		if (m_school != null)
		{
			tripNames = new String[m_school.m_trips.size()];
			for (int i=0; i<m_school.m_trips.size(); i++)
			{
				tripNames[i] = m_school.m_trips.get(i).m_name;
			}
		}
		return tripNames;
	}

	private int GetCurrentSchoolIndex()
	{
		return m_schoolIndex;
	}

	protected School GetSchool(int pi_index) 
	{
		if (m_schoolIndex != pi_index)
		{
			m_schoolIndex = pi_index;
			m_trip = null;
			m_tripIndex = -1;
			SetTripButtonText(m_trip);
		}
		return DataManager.GetInstance().GetSchools()[pi_index];
	}

	private String[] GetSchoolNames() 
	{
		School[] schools = DataManager.GetInstance().GetSchools();
		
		String[] schoolNames = new String[schools.length];
		for (int i=0; i<schools.length; i++)
		{
			schoolNames[i] = schools[i].m_name;
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
		m_communicator  = (DriverAppCommunicator) activity;
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
	{
		m_trip.m_isReturn = isChecked;
		m_childList.invalidateViews();
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
