package gui;

import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;

public interface BuildingRecords {

		
		public LocationTypeEnum getType();

		public Role addPerson(String role, String name);
		
}
