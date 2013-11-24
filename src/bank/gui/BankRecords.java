package bank.gui;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import gui.BuildingRecords;


public class BankRecords implements BuildingRecords {

	@Override
	public LocationTypeEnum getType() {
		
		return LocationTypeEnum.Bank;
	}

	@Override
	public Role addPerson(String role, String name) {
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
}