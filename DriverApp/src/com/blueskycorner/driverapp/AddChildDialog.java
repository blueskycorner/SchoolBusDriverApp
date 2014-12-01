package com.blueskycorner.driverapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class AddChildDialog extends Dialog implements android.view.View.OnClickListener, OnItemClickListener
{
	private IDriverAppCommunicator m_comm = null;
	private AutoCompleteTextView m_childName = null;
	private ArrayList<String> m_proposals = null;
	private Button m_okButton = null;
	private Child m_child = null;
	private HashMap<String, Child> m_map = null;
	
	public AddChildDialog(Context context, IDriverAppCommunicator pi_communicator) 
	{
		super(context);
		m_comm = pi_communicator;
		setContentView(R.layout.dialog_add_child);
		setTitle(context.getResources().getText(R.string.add_child));
		setCancelable(false);
		
		m_childName = (AutoCompleteTextView) findViewById(R.id.editTextAddChild);
		m_childName.setOnItemClickListener(this);
		m_okButton = (Button) findViewById(R.id.buttonAddChildConfirmed);
		m_okButton.setEnabled(false);
		m_okButton.setOnClickListener(this);
		ArrayList<Child> childs = DataManager.GetInstance().GetChilds();
		
		m_proposals = new ArrayList<String>();
		m_map = new HashMap<String, Child>();
		for (Child child : childs) 
		{
			String s = child.m_lastName + " " + child.m_firstName;
			m_proposals.add(s);
			m_map.put(s, child);
		}

		ArrayAdapter<String> a = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, m_proposals);
		m_childName.setAdapter(a);
		m_childName.setThreshold(2);
	}

	@Override
	public void onClick(View v) 
	{
		if (v.getId() == R.id.buttonAddChildConfirmed)
		{
			m_comm.ChildAdded(m_child);
		}
		this.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		String s = (String) arg0.getItemAtPosition(arg2);
		m_child = m_map.get(s);
		if (m_child != null)
		{
			m_okButton.setEnabled(true);
		}
	}
	
}
