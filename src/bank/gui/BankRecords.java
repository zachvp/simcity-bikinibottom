package bank.gui;

import gui.BuildingRecords;
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;


public class BankRecords implements BuildingRecords {

	AnimationPanel animationPanel;//TODO
	BankBuilding bankBuilding;
	
	private PersonAgent TellerPerson = new PersonAgent("teller");
	
	private static final CityBuilding cityBuilding = null;
	@Override
	public LocationTypeEnum getType() {
		
		return LocationTypeEnum.Bank;
	}

	@Override
	public Role addPerson(String role, String name) {
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
}