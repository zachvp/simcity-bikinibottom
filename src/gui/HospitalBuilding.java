package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;

public class HospitalBuilding extends Building{	
	
	CitizenRecords records;
	ArrayList<Building> buildings;
	JPanel panel = new JPanel();
	PersonCreationPanel personPanel = new PersonCreationPanel();
	ScenarioPanel scenarioPanel = new ScenarioPanel();

	public HospitalBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		//UIManager.put("TabbedPane.selected", Color.white);
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setOpaque(false);
		tabbedPane.addTab("Create a Person", personPanel);
		tabbedPane.addTab("Scenarios", scenarioPanel);
		
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		panel.add(tabbedPane, BorderLayout.CENTER);
	}

	@Override
	public XYPos entrancePos() {
		return (new XYPos((int)(width/2),(int)height));
	}

	@Override
	public Role getGreeter() {
		return null;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Hospital;
	}

	@Override
	public Role getCustomerRole(Person person) {
		return null;
	}

	@Override
	public JPanel getAnimationPanel() {
		return panel;
	}

	@Override
	public JPanel getInfoPanel() {
		return new JPanel();
	}

	public void setRecords(CitizenRecords citizenRecords) {
		records = citizenRecords;
		personPanel.setRecords(records);
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

	@Override
	public JPanel getStaffPanel() {
		return new JPanel();
	}
	

}
