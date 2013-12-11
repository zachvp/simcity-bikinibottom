package housing.backend;

import housing.gui.HousingComplexGui;
import housing.gui.MaintenanceWorkerRoleGui;
import housing.interfaces.Dwelling;

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
	private PayRecipientRole payRecipient;
	private MaintenanceWorkerRole worker;
	
	// used for producing jobs and residential roles in the complex
	private Map<Role, Person> population = new HashMap<Role, Person>();
	// maps the housing complex unit to the role
	private Map<Integer, Role> unitMap = new HashMap<Integer, Role>();
	
	public HousingComplex(ResidentialBuilding building) {
		
		// set up pointer to the building the complex is in
		this.building = building;
		
		// instantiate the gui class for the complex
		
		if(Constants.TEST_POPULATE_HOUSING) {
			try {
				addRole("payrecipient");
				addRole("worker");
				this.gui = new HousingComplexGui(this);
				
				// assign all the residents of this comlex to a pay recipient
				for(Dwelling d : gui.getDwellings()) {
					payRecipient.addResident(d);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			// worker for this building
			this.worker = new MaintenanceWorkerRole(null, building);
			
			// manager for this building 
			this.payRecipient = new PayRecipientRole(null, building);
			
			// make sure the worker knows the building he's working in
			this.worker.setComplex(this);
			
			// put the constant roles in the building map
			this.population.put(payRecipient, null);
			this.population.put(worker, null);
			
			this.gui = new HousingComplexGui(this);
		}
	}
	
	/* --- Test functions --- */
	public void addRole(String roleType) throws Exception {
		PersonAgent person = new PersonAgent(roleType);
		
		roleType.toLowerCase();
	
		switch(roleType) {
			case "worker" : {
				this.worker = new MaintenanceWorkerRole(person, building);
				this.worker.setComplex(this);
				this.population.put(worker, person);
				person.addRole(worker);
				worker.activate();
				break;
			}
			case "payrecipient" : {
				payRecipient = new PayRecipientRole(person, building);
				this.population.put(payRecipient, person);
				person.addRole(payRecipient);
				payRecipient.activate();
				break;
			}
			default : {
				throw new Exception("Improper role type passed in parameter.");
			}
		}
		person.startThread();
	}
	
	public void addRoleToUnit(int unitNum, ResidentRole role) {
		unitMap.put(unitNum, role);
	}
	
	/* --- Utility functions --- */
	
	public void addResident(ResidentRole resident){
		this.population.put(resident, null);
	}
	
	/* --- Getters --- */
	public CityLocation getBuilding() {
		return building;
	}
	
	public Map<Role, Person> getPopulation() {
		return population;
	}
	
	public Map<Integer, Role> getUnitMap() {
		return unitMap;
	}
	
	public MaintenanceWorkerRole getWorker() {
		return worker;
	}

	public PayRecipientRole getPayRecipient() {
		return payRecipient;
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
