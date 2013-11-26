package bank.gui;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;


public class BankRecords {

	AnimationPanel animationPanel;//TODO
	BankBuilding bankBuilding;
	
	private PersonAgent TellerPerson = new PersonAgent("teller");
	
	private static final CityBuilding cityBuilding = null;
	public LocationTypeEnum getType() {
		
		return LocationTypeEnum.Bank;
	}

	public Role addPerson(String role, String name) {
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
}