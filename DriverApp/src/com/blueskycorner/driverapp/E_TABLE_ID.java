package com.blueskycorner.driverapp;

public enum E_TABLE_ID 
{
	TABLE_SCHOOL(0),
	TABLE_DESTINATION(1),
	TABLE_TRIP(2),
	TABLE_CHILD(3),
	TABLE_TRIP_CHILD_ASSOCIATION(4);
	
	private final int id;
	E_TABLE_ID(int id) { this.id = id; }
	public int getValue() { return id; }
	
	@Override
	public String toString()
	{
		String s = "";
		switch (this) 
		{
			case TABLE_SCHOOL:
			{
				s = SchoolDAO.TABLE;
				break;
			}
			case TABLE_DESTINATION:
			{
				s = TripDestinationDAO.TABLE;
				break;
			}
			case TABLE_TRIP:
			{
				s = TripDAO.TABLE;
				break;
			}
			case TABLE_CHILD:
			{
				s = ChildDAO.TABLE;
				break;
			}
			case TABLE_TRIP_CHILD_ASSOCIATION:
			{
				s = TripChildAssociationDAO.TABLE;
				break;
			}
		}
		return s;
	}

}
