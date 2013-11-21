package gui;

import java.util.Vector;

import agent.PersonAgent;

/**
 * A class to hold all of the PersonAgent information
 * @author Victoria Dea
 *
 */
public class CitizenRecords {
	 private MainFrame main;
	 private Vector<PersonAgent> citizens = new Vector<PersonAgent>();
	 private InfoList personInfoList;
	 
	private PersonAgent person = new PersonAgent("Steve");
	
	 
	 public CitizenRecords(MainFrame m){
		 main = m;
		 personInfoList = main.getPersonInfoList();
		 addCitizen(person);
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
		 
		 citizens.add(person);
		 personInfoList.addToList(newPerson.getName());
		 //start Thread

			
		}
	 
	 public PersonAgent findPerson(String name){
		 PersonAgent person = null;
		 for (PersonAgent p: citizens){
			 if(p.getName() == (name)){
				 person = p;
			 }
		 }
		 return person;
	 }

	public void setPersonInfoList(InfoList personList) {
		personInfoList = personList;
		
	}

	
}
