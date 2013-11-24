package bank.gui;

import market.gui.MarketControlPanel;
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import gui.BuildingRecords;


public class BankRecords implements BuildingRecords {

	MarketControlPanel marketControlPanel;
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