package housing.gui;

import housing.backend.MaintenanceWorkerRole;
import housing.backend.PayRecipientRole;
import housing.backend.ResidentRole;
import housing.backend.ResidentialBuilding;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.gui.Gui;
import agent.interfaces.Person;

/**
 * HousingComplex is slotted into a ResidentialBuilding. It is the equivalent of
 * one building unit. It has UNIT_COUNT subdivisions that each contain a smaller residential unit.
 *  
 * @author Zach VP
 */

@SuppressWarnings("serial")
public class HousingComplex extends JPanel {
	/* --- Data --- */
	
	CityLocation building;
	
	// the "boss" or greeter for this building and the on-call Mr. Fix-it
	private PayRecipientRole landlord;
	private MaintenanceWorkerRole worker;
	
	// used for producing jobs and residential roads in the complex
	public Map<Person, Role> population = new HashMap<Person, Role>();
	
	// some configuration constants
	private final int UNIT_COUNT = 1;
	private final int ROWS = 1;
	private final int COLUMNS = 1;
	private final int SPACING = 0;
	
	// layout manager
	private GridLayout complexLayout;

	// stores all of the housing units in the complex
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex(ResidentialBuilding building) {
		
		this.building = building;
		
		// worker for this building
		this.worker = new MaintenanceWorkerRole(null, building);
		
		// manager for this building 
		this.landlord = new PayRecipientRole(null, building);
		
		this.worker.setComplex(this);
		
		// put the constant roles in the building map
		this.population.put(null, landlord);
		this.population.put(null, worker);
		
		
		/** Handle GUI stuff */
		// create layout and set the layout manager
		complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i, building, this);
			this.add(gui);
			housingUnits.add(gui);
		}

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
	
	/** Gui utilities */
	// this is to get a maintenance worker in the right building
	public void addGuiToDwelling(Gui gui, int unitNumber) {
		for(HousingGui unit : housingUnits) {
			if(unit.getIndex() == unitNumber) {
				unit.addGui(gui);
			}
		}
	}
	
	// take the gui out once it's done visiting/fixing
	public void removeGuiFromDwelling(Gui gui, int unitNumber) {
		for(HousingGui unit : housingUnits) {
			if(unit.getIndex() == unitNumber) {
				unit.removeGui(gui);
			}
		}
	}
}
