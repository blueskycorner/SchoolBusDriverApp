package com.blueskycorner.driverapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class ChildFragment extends DriverAppFragment implements OnClickListener
{
	public static final String NAME = "CHILD_FRAGMENT";
	private TextView m_name = null;
	private TextView m_address = null;
	private TextView m_pickupTime = null;
	private Button m_buttonStart = null;
	private Button m_buttonFinish = null;
	private Button m_buttonSkip = null;
	private Button m_buttonBack = null;
	private boolean m_isActivated = false;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		m_fragmentName = NAME;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.child_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
		m_name = (TextView) getActivity().findViewById(R.id.textViewName);
		m_address = (TextView) getActivity().findViewById(R.id.textViewAddress);
		m_pickupTime  = (TextView) getActivity().findViewById(R.id.textViewPickupTime);
		m_buttonStart = (Button) getActivity().findViewById(R.id.buttonStart);
		m_buttonFinish = (Button) getActivity().findViewById(R.id.buttonFinish);
		m_buttonSkip = (Button) getActivity().findViewById(R.id.buttonSkip);
		m_buttonBack = (Button) getActivity().findViewById(R.id.buttonBack);
		
		m_buttonStart.setOnClickListener(this);
		m_buttonFinish.setOnClickListener(this);
		m_buttonSkip.setOnClickListener(this);
		m_buttonBack.setOnClickListener(this);
		
		if (GetCurrentChild().m_state == E_CHILD_STATE.STATE_ON_THE_WAY_STARTED)
		{
			m_isActivated = true;
		}
		else
		{
			m_isActivated = false;
		}
		InitButtons(m_isActivated);
		
		if (GetCurrentChild() != null)
		{
			m_name.setText(GetCurrentChild().m_firstName + " " + GetCurrentChild().m_lastName);
			m_address.setText(GetCurrentChild().GetAddress());
			String s = GetCurrentChild().GetPickupTime();
			if (s.endsWith("") == false)
			{
				m_pickupTime.setText(GetPickupText() + " @ " + s);
			}
			else
			{
				m_pickupTime.setText(GetPickupText() + " @ " + getActivity().getResources().getText(R.string.not_available));
			}
		}
	}

	private void InitButtons(boolean pi_isActivated) 
	{
		m_buttonBack.setEnabled(!pi_isActivated);
		m_buttonFinish.setEnabled(pi_isActivated);
		
		if (pi_isActivated == true)
		{
			m_buttonStart.setText(getActivity().getResources().getString(R.string.on_the_way));
		}
		else
		{
			m_buttonStart.setText(getActivity().getResources().getString(R.string.start));
		}

	}
	
	@Override
	public void SetTrip(Trip pi_trip) 
	{
		super.SetTrip(pi_trip);
	}
	
	private String GetPickupText() 
	{
		String s;
		if (m_trip.m_isReturn == false)
		{
			s = getActivity().getResources().getString(R.string.pickup);
		}
		else
		{
			s = getActivity().getResources().getString(R.string.dropoff);
		}
		
		return s;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.buttonFinish:
			{
				GetCurrentChild().m_state = E_CHILD_STATE.STATE_ON_THE_WAY_FINISHED;
				m_comm.childStateUpdated(GetCurrentChild());
				break;
			}
			case R.id.buttonSkip:
			{
				GetCurrentChild().m_state = E_CHILD_STATE.STATE_ON_THE_WAY_CANCELED;
				m_comm.childStateUpdated(GetCurrentChild());
				break;
			}
			case R.id.buttonBack:
			{
				m_comm.childStateUpdated(GetCurrentChild());
				break;
			}
			case R.id.buttonStart:
			{
				m_isActivated = !m_isActivated;
				
				if (m_isActivated == true)
				{
					GetCurrentChild().m_state = E_CHILD_STATE.STATE_ON_THE_WAY_STARTED;
				}
				else
				{
					GetCurrentChild().m_state = E_CHILD_STATE.STATE_WAITING;
				}
				InitButtons(m_isActivated);
				m_comm.childStateUpdated(GetCurrentChild());
				break;
			}
		}
	}

	@Override
	public void BackPressed() 
	{
		if (m_buttonBack.isEnabled())
		{
			m_comm.childStateUpdated(GetCurrentChild());
		}
		else
		{
			Toast.makeText(getActivity(), "Please update child state", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public ViewGroup GetViewGroup()
	{
		ViewGroup l = (ViewGroup) getActivity().findViewById(R.id.child_fragment);
		return l;
	}

	@Override
	public void RefreshState(Child pi_child) 
	{
		if (pi_child == GetCurrentChild())
		{
			m_comm.childStateUpdated(GetCurrentChild());
		}
	}

	private Child GetCurrentChild()
	{
		return m_trip.GetCurrentChild();
	}

	@Override
	public void UpdateUI() 
	{
		
	}
}
