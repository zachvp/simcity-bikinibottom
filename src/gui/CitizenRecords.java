package gui;

import java.util.Vector;

import agent.PersonAgent;

/**
 * A class to hold all of the PersonAgent information
 * @author Victoria Dea
 *
 */
public class CitizenRecords {
	 private Vector<PersonAgent> citizens = new Vector<PersonAgent>();
	 private InfoList personInfoList;
	 
	 public CitizenRecords(){
		 
	 }
	 
	 /**
	  * Adds a new person to the records
	  * @param person A PersonAgent
	  */
	 public void addCitizen(PersonAgent person){
		 citizens.add(person);
		 personInfoList.addToList(person.getName());
	 }
	 
	 public PersonAgent findPerson(String name){
		 PersonAgent person = null;
		 for (PersonAgent p: citizens){
			 if(p.getName() == name){
				 person = p;
			 }
		 }
		 return person;
	 }

	public void setPersonInfoList(InfoList personList) {
		personInfoList = personList;
		
	}
}
