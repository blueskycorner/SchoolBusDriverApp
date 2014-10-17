package com.blueskycorner.driverapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ChildFragment extends DriverAppFragment implements OnClickListener, OnCheckedChangeListener
{
	public static final String NAME = "CHILD_FRAGMENT";
	private Child m_child;
	TextView m_name = null;
	TextView m_address = null;
	ToggleButton m_buttonStart = null;
	Button m_buttonFinish = null;
	Button m_buttonSkip = null;
	Button m_buttonBack = null;
	private DriverAppCommunicator m_comm;

	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.child_fragment, container, false);
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		m_comm = (DriverAppCommunicator) activity;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		m_name = (TextView) getActivity().findViewById(R.id.textViewName);
		m_address = (TextView) getActivity().findViewById(R.id.textViewAddress);
		m_buttonStart = (ToggleButton) getActivity().findViewById(R.id.buttonStart);
		m_buttonFinish = (Button) getActivity().findViewById(R.id.buttonFinish);
		m_buttonSkip = (Button) getActivity().findViewById(R.id.buttonSkip);
		m_buttonBack = (Button) getActivity().findViewById(R.id.buttonBack);
		
		m_buttonStart.setOnCheckedChangeListener(this);
		m_buttonFinish.setOnClickListener(this);
		m_buttonSkip.setOnClickListener(this);
		m_buttonBack.setOnClickListener(this);
		
		m_buttonFinish.setEnabled(false);
		
		if (m_child != null)
		{
			m_name.setText(m_child.m_firstName + " " + m_child.m_lastName);
			m_address.setText(m_child.m_address);
		}
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
				m_child.m_state = E_CHILD_STATE.FINISH;
				m_comm.childStateUpdated(m_child);
				break;
			}
			case R.id.buttonSkip:
			{
				m_child.m_state = E_CHILD_STATE.SKIPPED;
				m_comm.childStateUpdated(m_child);
				break;
			}
			case R.id.buttonBack:
			{
				m_comm.childStateUpdated(m_child);
				break;
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		m_buttonBack.setEnabled(!isChecked);
		m_buttonFinish.setEnabled(isChecked);
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
}
