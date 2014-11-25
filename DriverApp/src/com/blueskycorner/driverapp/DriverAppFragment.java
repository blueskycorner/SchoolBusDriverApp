package com.blueskycorner.driverapp;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public abstract class DriverAppFragment extends Fragment
{
	protected String m_fragmentName = null;
	
	public abstract void BackPressed();

	public abstract ViewGroup GetViewGroup();
	
	public abstract void RefreshState(Child pi_child);
	
	public String GetName()
	{
		return m_fragmentName;
	}
	
	public void SetEnabled(boolean b)
	{
		ViewGroup l = GetViewGroup();
		enableDisableView(l,b);
	}
	
	private void enableDisableView(View view, boolean enabled) {
	    view.setEnabled(enabled);

	    if ( view instanceof ViewGroup ) {
	        ViewGroup group = (ViewGroup)view;

	        for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
	            enableDisableView(group.getChildAt(idx), enabled);
	        }
	    }
	}
}
