package gui;

import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;

public class HospitalBuilding extends Building{	
	
	CitizenRecords records;
	PersonCreationPanel panel = new PersonCreationPanel();
	

	public HospitalBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
		return (new XYPos((int)x,(int)y));
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

}
