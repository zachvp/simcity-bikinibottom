package gui;

import java.util.ArrayList;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;

/**
 * A class to hold all of the PersonAgent information
 * Where the PersonAgents are instantiated and stored.
 * @author Victoria Dea
 *
 */
public class CitizenRecords {
	private MainFrame main;
	private ArrayList<PersonAgent> citizens = new ArrayList<PersonAgent>();
	ArrayList<Building> buildings;
	private InfoList personInfoList;
	private InfoPanel infoPanel;

	public CitizenRecords(MainFrame m){
		main = m;
		personInfoList = main.getPersonInfoList();
	}

	/**
	 * Adds a new person to the records
	 * @param person A PersonAgent
	 */
	public void addCitizen(PersonAgent person){
		citizens.add(person);
		personInfoList.addToList(person.getName());
	}
	public void addCitizen(String name, WorkRole job, String home, String status,
			boolean hasCar) {
		PersonAgent newPerson  = new PersonAgent(name);
		//PassegerRole passengerRole = new PassengerRole();
		//TODO add all attributes to person
		//TODO remember that you need to add roles to people AND people to roles
		
		//Add passengerAgent

		citizens.add(newPerson);
		personInfoList.addToList(newPerson.getName());
		newPerson.startThread();

		infoPanel.updatePersonInfoPanel(newPerson);

	}
	
	/**
	 * Displays the PersonAgent's details on the InfoPanel
	 * @param name the name of the PersonAgent
	 */
	public void showInfo(String name) {
		for (PersonAgent p: citizens){
			if(p.getName() == (name)){
				infoPanel.updatePersonInfoPanel(p);
				//System.out.println("showing info for "+p.getName());
			}
		}
	}
	
	
	/** Utilities **/
	
	/**
	 * Returns the list of PersonAgents in the city
	 * @return a Vector list of PersonAgent
	 */
	public ArrayList<PersonAgent> getCitizenList() {
		return citizens;
	}
	/**
	 * Sets the reference to the InfoPanel
	 * @param p the InfoPanel on the MainFrame
	 */
	public void setInfoPanel(InfoPanel p) {
		infoPanel = p;
	}
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	public void setBuildings(ArrayList<Building> b){
		buildings = b;
	}
	public ArrayList<Building> getBuildings(){
		return buildings;
	}
}
