package classifieds;

import housing.interfaces.Dwelling;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Response;


import CommonSimpleClasses.CityBuilding;
import agent.Role;
import agent.WorkRole;

public class ClassifiedsClass implements Classifieds {
	
	private ClassifiedsClass() {}
	
	static ClassifiedsClass instance = null;
	
	static public ClassifiedsClass getClassifiedsInstance() {
		if (instance == null) {
			instance = new ClassifiedsClass();
		}
		
		return instance;
	}
	
	//List of all the work roles in existence
	List<WorkRole> roles = new ArrayList<WorkRole>();
	
	//List of all the dwellings in existence
	List<Dwelling> dwellings = new ArrayList<Dwelling>();
	
	@Override
	public List<WorkRole> getJobsForBuilding(CityBuilding building,
			boolean returnOnlyOpenPositions) {
		if(building == null){
			return roles;
		}
		else{
		List<WorkRole> response = new ArrayList<WorkRole>();
		for (WorkRole role : roles) {
			if (role.getPerson() != null &&
					returnOnlyOpenPositions)
				continue;
			if (building == null) {
				response.add(role);
			} else if (role.getLocation() == building) {
				response.add(role);
			}
		}
		return response;
		}
	}

	@Override
	public List<Dwelling> getRooms(boolean returnOnlyOpenRooms) {
		List<Dwelling> response = new ArrayList<Dwelling>();
		
		for (Dwelling dwelling : dwellings) {
			if (dwelling.getResident() != null
					&& returnOnlyOpenRooms) {
				continue;
			}
			response.add(dwelling);
		}
		
		return response;
	}

	@Override
	public void addWorkRole(WorkRole role) {
		roles.add(role);
	}

	@Override
	public void addDwelling(Dwelling dwelling) {
		dwellings.add(dwelling);
	}

}
