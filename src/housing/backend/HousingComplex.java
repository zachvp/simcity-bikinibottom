package housing.backend;

import housing.gui.HousingComplexGui;
import housing.gui.MaintenanceWorkerRoleGui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

/**
 * HousingComplex is slotted into a ResidentialBuilding. It is the equivalent of
 * one building unit. It has UNIT_COUNT subdivisions that each contain a smaller residential unit.
 *  
 * @author Zach VP
 */

public class HousingComplex {
	/* --- Data --- */
	
	CityLocation building;
	
	HousingComplexGui gui;
	
	// the "boss" or greeter for this building and the on-call Mr. Fix-it
	private PayRecipientRole landlord;
	private MaintenanceWorkerRole worker;
	
	// used for producing jobs and residential roads in the complex
	public Map<Person, Role> population = new HashMap<Person, Role>();
	
	public HousingComplex(ResidentialBuilding building) {
		
		// set up pointer to the building the complex is in
		this.building = building;
		
		// instantiate the gui class for the complex
		this.gui = new HousingComplexGui(this);
		
		if(!Constants.DEBUG) {
			// worker for this building
			this.worker = new MaintenanceWorkerRole(null, building);
			
			// manager for this building 
			this.landlord = new PayRecipientRole(null, building);
			
			this.worker.setComplex(this);
			
			// put the constant roles in the building map
			this.population.put(null, landlord);
			this.population.put(null, worker);
		}
		else {
			try {
				addRole("payrecipient");
				addRole("worker");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* --- Test functions --- */
	public void addRole(String roleType) throws Exception {
		PersonAgent person = new PersonAgent(roleType);
		
		roleType.toLowerCase();
	
		switch(roleType) {
			case "worker" : {
				worker = new MaintenanceWorkerRole(person, building);
				person.addRole(worker);
				worker.activate();
				break;
			}
			case "payrecipient" : {
				landlord = new PayRecipientRole(person, building);
				person.addRole(landlord);
				landlord.activate();
				break;
			}
			default : {
				throw new Exception("Improper role type passed in parameter.");
			}
		}
		person.startThread();
	}
	
	/* --- Utility functions --- */
	
	public void addResident(ResidentRole resident){
		this.population.put(null, resident);
	}
	
	/* --- Getters --- */
	public CityLocation getBuilding() {
		return building;
	}
	
	public Map<Person, Role> getPopulation() {
		return population;
	}
	
	public MaintenanceWorkerRole getWorker() {
		return worker;
	}

	public PayRecipientRole getPayRecipient() {
		return landlord;
	}
	
	public JPanel getGui() {
		return gui;
	}

	public void removeGuiFromDwelling(MaintenanceWorkerRoleGui worker, int unit) {
		gui.removeGuiFromDwelling(worker, unit);
	}

	public void addGuiToDwelling(MaintenanceWorkerRoleGui worker, int unit) {
		gui.addGuiToDwelling(worker, unit);
	}
}
