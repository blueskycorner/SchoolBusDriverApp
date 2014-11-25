package com.blueskycorner.driverapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ChildFragment extends DriverAppFragment implements OnClickListener
{
	public static final String NAME = "CHILD_FRAGMENT";
	private Child m_child;
	private TextView m_name = null;
	private TextView m_address = null;
	private TextView m_pickupTime = null;
	private Button m_buttonStart = null;
	private Button m_buttonFinish = null;
	private Button m_buttonSkip = null;
	private Button m_buttonBack = null;
	private IDriverAppCommunicator m_comm;
	private boolean m_bIsReturn;
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
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		m_comm = (IDriverAppCommunicator) activity;
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
		
		InitButtons();
		
		if (m_child != null)
		{
			m_name.setText(m_child.m_firstName + " " + m_child.m_lastName);
			m_address.setText(m_child.GetAddress());
			m_pickupTime.setText(GetPickupText() + " @ " + m_child.GetPickupTime());
		}
	}

	private void InitButtons() 
	{
		m_isActivated = false;
		m_buttonStart.setText(getActivity().getResources().getString(R.string.start));
		m_buttonFinish.setEnabled(false);
		m_buttonSkip.setEnabled(true);
		m_buttonBack.setEnabled(true);
	}
	
	private String GetPickupText() 
	{
		String s;
		if (m_bIsReturn == false)
		{
			s = getActivity().getResources().getString(R.string.pickup);
		}
		else
		{
			s = getActivity().getResources().getString(R.string.dropoff);
		}
		
		return s;
	}

	public void SetChild(Child pi_child)
	{
		m_child = pi_child;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.buttonFinish:
			{
				m_child.m_state = E_CHILD_STATE.STATE_ON_THE_WAY_FINISHED;
				m_comm.childStateUpdated(m_child);
				break;
			}
			case R.id.buttonSkip:
			{
				m_child.m_state = E_CHILD_STATE.STATE_ON_THE_WAY_CANCELED;
				m_comm.childStateUpdated(m_child);
				break;
			}
			case R.id.buttonBack:
			{
				m_comm.childStateUpdated(m_child);
				break;
			}
			case R.id.buttonStart:
			{
				m_isActivated = !m_isActivated;
				m_buttonBack.setEnabled(!m_isActivated);
				m_buttonFinish.setEnabled(m_isActivated);
				
				if (m_isActivated == true)
				{
					m_buttonStart.setText(getActivity().getResources().getString(R.string.on_the_way));
					m_child.m_state = E_CHILD_STATE.STATE_ON_THE_WAY_STARTED;
				}
				else
				{
					m_buttonStart.setText(getActivity().getResources().getString(R.string.start));
					m_child.m_state = E_CHILD_STATE.STATE_WAITING;
				}
				m_comm.childStateUpdated(m_child);
				break;
			}
		}
	}

	@Override
	public void BackPressed() 
	{
		if (m_buttonBack.isEnabled())
		{
			m_comm.childStateUpdated(m_child);
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
		if (pi_child == m_child)
		{
			m_comm.childStateUpdated(m_child);
		}
	}

	public void SetReturn(boolean pi_isReturn) 
	{
		m_bIsReturn = pi_isReturn;
	}

	public Child GetChild() 
	{
		return m_child;
	}
}
