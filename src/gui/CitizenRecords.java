package gui;

import housing.backend.ResidentRole;
import housing.interfaces.Dwelling;

import java.util.ArrayList;
import java.util.List;

import transportation.CornerAgent.MyCorner;
import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.CardinalDirectionEnum;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;
import agent.PersonAgent.HungerLevel;
import agent.interfaces.Person.Wallet.IncomeLevel;

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
	public void addCitizen(String name, WorkRole job, Dwelling home, String status,
			boolean hasCar, String hungerLevel, String restaurant, String foodAtHome) {
		
		//Assigning income level
		IncomeLevel incomeLevel;
		
		switch (status) {
		case "Rich":
			incomeLevel = IncomeLevel.RICH;
			break;
		case "Poor":
			incomeLevel = IncomeLevel.POOR;
			break;
		default:
			incomeLevel = IncomeLevel.MEDIUM;
			break;
		}
		
		//Assigning hunger level
		HungerLevel hunger;
		switch (hungerLevel) {
		case "Starving":
			hunger = HungerLevel.STARVING;
			break;
		case "Hungry":
			hunger = HungerLevel.HUNGRY;
			break;
		default:
		case "Neutral":
			hunger = HungerLevel.NEUTRAL;
			break;
		case "Satisfied":
			hunger = HungerLevel.SATISFIED;
			break;
		case "Full":
			hunger = HungerLevel.FULL;
			break;
		}
		
		//Assigning willing to go to Restaurant or not
		boolean goToRestaurant;
		switch (restaurant) {
		case "Not willing to go":
			goToRestaurant = false;
			break;
		default:
		case "Willing to go":
			goToRestaurant = true;
			break;
		}
		
		
		PersonAgent newPerson  = new PersonAgent
				(name, incomeLevel, hunger, goToRestaurant, (foodAtHome.equals("Has food at home")));
		
		//Assigning job
		if (job != null) {
			newPerson.addRole(job);
			job.setPerson(newPerson);
		}
		
		//Assigning residence
		if (home != null) {
			ResidentRole role = (ResidentRole)(home.getResident());
			newPerson.addRole(role);
			role.setPerson(newPerson);			
		}
		
		//Adding car
		if (hasCar) newPerson.instantiateCar();
		
		//PassegerRole passengerRole = new PassengerRole();
		//TODO add all attributes to person
		//TODO add dwelling!
		
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
