package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;

public class HospitalBuilding extends Building{	
	
	CitizenRecords records;
	ArrayList<Building> buildings;
	PersonCreationPanel panel = new PersonCreationPanel();
	

	public HospitalBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
		return (new XYPos(0,0));
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return LocationTypeEnum.Hospital;
	}

	@Override
	public Role getCustomerRole(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getAnimationPanel() {
		// TODO Auto-generated method stub
		return panel;
	}

	@Override
	public JPanel getInfoPanel() {
		
		return new JPanel();
	}

	public void setRecords(CitizenRecords citizenRecords) {
		records = citizenRecords;
		panel.setRecords(records);
	}
	public void setBuildings(ArrayList<Building> b){
		buildings = b;
		records.setBuildings(b);
	}

	@Override
	public int getOpeningHour() {
		return 0;
	}

	@Override
	public int getOpeningMinute() {
		return 0;
	}

	@Override
	public int getClosingHour() {
		return 23;
	}

	@Override
	public int getClosingMinute() {
		return 59;
	}
	

}
