package bank.gui;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import gui.BuildingRecords;


public class BankRecords implements BuildingRecords {

	AnimationPanel animationPanel;//TODO
	
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