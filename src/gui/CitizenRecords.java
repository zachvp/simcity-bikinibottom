package gui;

import java.util.Vector;

import kelp.Kelp;
import kelp.KelpClass;
import transportation.RealPassengerRole;
import transportation.gui.PassengerGuiClass;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.XYPos;
import agent.PersonAgent;
import agent.PersonAgent.HungerLevel;

/**
 * A class to hold all of the PersonAgent information
 * Where the PersonAgents are instantiated and stored.
 * @author Victoria Dea
 *
 */
public class CitizenRecords {
	private MainFrame main;
	private Vector<PersonAgent> citizens = new Vector<PersonAgent>();
	private InfoList personInfoList;
	private InfoPanel infoPanel;

	private PersonAgent person = new PersonAgent("Steve");


	public CitizenRecords(MainFrame m){
		main = m;
		personInfoList = main.getPersonInfoList();
		addCitizen(person);
		//person.setHungerLevel(HungerLevel.STARVING,true);
		Kelp kelp = KelpClass.getKelpInstance();
		person.addRole(new RealPassengerRole(person, 
				kelp.placesNearMe(new XYPos(0, 0), 
						LocationTypeEnum.Apartment).get(0),
				new PassengerGuiClass()));
		person.startThread();
		/*
		person.goToLoc(kelp.placesNearMe(kelp.placesNearMe(new XYPos(0, 0), 
			LocationTypeEnum.Apartment).get(0), LocationTypeEnum.Restaurant).get(0));
			*/
		person.goToLoc(person.chooseHouse());
	}

	/**
	 * Adds a new person to the records
	 * @param person A PersonAgent
	 */
	public void addCitizen(PersonAgent person){
		citizens.add(person);
		personInfoList.addToList(person.getName());
	}
	public void addCitizen(String name, String job, String home, String status,
			boolean hasCar) {
		PersonAgent newPerson  = new PersonAgent(name);

		//TODO add all attributes to person

		citizens.add(newPerson);
		personInfoList.addToList(newPerson.getName());
		//start Thread


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
	public Vector<PersonAgent> getCitizenList() {
		return citizens;
	}
	/**
	 * Sets the reference to the InfoPanel
	 * @param p the InfoPanel on the MainFrame
	 */
	public void setInfoPanel(InfoPanel p) {
		infoPanel = p;
	}
}
