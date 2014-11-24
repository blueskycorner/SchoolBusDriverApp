package com.blueskycorner.driverapp;

import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class TripFragment extends DriverAppFragment implements OnClickListener
{
	public static final String NAME = "TRIP_FRAGMENT";
	private Trip m_trip = null;
	private LinearLayout m_layout = null;
	private Activity m_activity;
	private HashMap<Integer, Child> m_childMap = null;
	private IDriverAppCommunicator m_comm = null;
	private Button m_buttonEndTrip = null;

	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.trip_fragment, container, false);
		m_layout = (LinearLayout) v.findViewById(R.id.trip_buttons);
		m_buttonEndTrip = (Button) v.findViewById(R.id.buttonEndTrip);
		m_buttonEndTrip.setOnClickListener(this);
		
		UpdateUI();
		
		return v;
	}

	public void UpdateUI() 
	{
		if (m_trip != null)
		{
			int i = 0;
			Boolean bIsReturn = m_trip.m_isReturn;
			m_childMap = new HashMap<Integer, Child>();
			for (Child child : m_trip.m_childs) 
			{
				Button bt = new Button(m_activity);
				bt.setText(child.m_firstName + " " + child.m_lastName);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
				lp.weight = 1;
				bt.setId(i);
				bt.setLayoutParams(lp);
				bt.setOnClickListener(this);
				Drawable img = null;
				
				if ( (bIsReturn == true) && (child.m_isPresent == false) )
				{
					child.m_state = E_CHILD_STATE.STATE_MISSING;
					bt.setEnabled(false);
				}
				
				img = GetDrawable(child);
				bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
				m_layout.addView(bt);
				
				m_childMap.put(i, child);
				i++;
			}
			
			if (m_trip.GetRemainChildCount() == 0)
			{
				Toast.makeText(m_activity, R.string.all_kids_dealed_with, Toast.LENGTH_LONG).show();
				m_buttonEndTrip.setText(m_activity.getResources().getText(R.string.end_trip));
			}
			else
			{

				m_buttonEndTrip.setText(m_activity.getResources().getText(R.string.cancel_trip));
			}
		}
	}

	private Drawable GetDrawable(Child child) 
	{
		Drawable img = null;
		switch (child.m_state)
		{
			case STATE_WAITING:
			{	
				img = m_activity.getResources().getDrawable(R.drawable.waiting);
				break;
			}
			case STATE_ON_THE_WAY_FINISHED:
			{	
				img = m_activity.getResources().getDrawable(R.drawable.happy);
				break;
			}
			case STATE_ON_THE_WAY_CANCELED:
			case STATE_MISSING:
			{	
				img = m_activity.getResources().getDrawable(R.drawable.skip);
				break;
			}
		}
		return img;
	}
	
	public void UpdateTrip(Trip pi_trip)
	{
		m_trip  = pi_trip;
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		m_activity = activity;
		m_comm = (IDriverAppCommunicator) activity;
		
	}

	@Override
	public void onClick(View v) 
	{
		if (v.getId() == R.id.buttonEndTrip)
		{
			if (m_trip.GetRemainChildCount() == 0)
			{
				m_trip.m_isCancel = false;
				m_comm.TripFinished();
			}
			else
			{
				ConfirmTripCancelation();
			}
		}
		else
		{
			Child c = m_childMap.get(v.getId());
			m_comm.ChildSelected(c);
		}
	}

	@Override
	public void BackPressed() 
	{
		ConfirmTripCancelation();
	}

	private void ConfirmTripCancelation() 
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    	builder.setTitle(R.string.warning);
    	builder.setMessage(R.string.confirmation_cancel_trip);
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
				m_trip.m_isCancel = true;
				m_comm.TripFinished();
			}
		});
    	builder.show();
	}

	@Override
	public ViewGroup GetViewGroup()
	{
		ViewGroup l = (ViewGroup) getActivity().findViewById(R.id.trip_fragment);
		return l;
	}

	@Override
	public void RefreshState(Child pi_child) 
	{
		Integer i = getKeyByValue(m_childMap, pi_child);
		Button b = (Button) getActivity().findViewById(i);
		Drawable img = GetDrawable(pi_child);
		b.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
	}
	
	public static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
