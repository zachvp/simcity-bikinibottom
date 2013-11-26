package classifieds;

import gui.Building;
import housing.ResidentRole;
import housing.interfaces.Dwelling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.ws.Response;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.WorkRole;

public class ClassifiedsClass implements Classifieds {
	
	private ClassifiedsClass() {}
	
	static ClassifiedsClass instance = null;
	private List<ClassifiedsChangedListener> listeners
			= new ArrayList<ClassifiedsChangedListener>();
	
	static public ClassifiedsClass getClassifiedsInstance() {
		if (instance == null) {
			instance = new ClassifiedsClass();
		}
		
		return instance;
	}
	
	//List of all the work roles in existence
	List<WorkRole> roles = 
			Collections.synchronizedList(new ArrayList<WorkRole>());
	
	Set<CityLocation> workplaces = 
			Collections.synchronizedSet(new HashSet<CityLocation>());
	
	//List of all the dwellings in existence
	List<Dwelling> dwellings = new ArrayList<Dwelling>();
	
	@Override
	public List<WorkRole> getJobsForBuilding(CityBuilding building,
			boolean returnOnlyOpenPositions) {
		List<WorkRole> response = new ArrayList<WorkRole>();
		
		synchronized (roles) {
			for (WorkRole role : roles) {
				if (role.getPerson() != null && returnOnlyOpenPositions)
					continue;
				if (building == null) {
					response.add(role);
				} else if (role.getLocation() == building) {
					response.add(role);
				}
			}
		}
		return response;
	}

	@Override
	public List<Dwelling> getRooms(boolean returnOnlyOpenRooms) {
		List<Dwelling> response = new ArrayList<Dwelling>();
		
		for (Dwelling dwelling : dwellings) {
			ResidentRole role = (ResidentRole) (dwelling.getResident());
			if (role.getPerson() != null
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
		workplaces.add(role.getLocation());
		notifyListeners();
	}

	@Override
	public void addDwelling(Dwelling dwelling) {
		dwellings.add(dwelling);
		notifyListeners();
	}
	
	@Override
	public void addListener(ClassifiedsChangedListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void notifyListeners() {
		for (ClassifiedsChangedListener listener : listeners) {
			listener.classifiedsUpdated();
		}
	}

	@Override
	public Set<CityLocation> getWorkplaces() {
		synchronized (workplaces) {
			return new HashSet<CityLocation>(workplaces);
		}
	}

}
