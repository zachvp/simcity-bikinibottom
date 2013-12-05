package housing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import housing.gui.HousingComplex;
import housing.gui.HousingInfoPanel;

/**
 * ResidentialBuilding is the class that will be slotted into the city map itself.
 * It will then display in the overview city map. Upon clicking on it, the view within
 * the ResidentialBuilding with the detailed housing animations will pop up. 
 * @author Zach VP
 */

public class ResidentialBuilding extends Building {
	// ResidentialBuilding is a CityLocation that will be added to kelp
	
	// location for the "door" to the building
	private XYPos entrancePos;
	
	// this displays after clicking on the ResidentialBuilding
	private HousingComplex complex;
	
	// prevents the repeated retrieval of the building name
	private boolean name = false;
	
	// the "boss" or greeter for this building and the on-call Mr. Fix-it
	private PayRecipientRole landlord;
	private MaintenanceWorkerRole worker;
	
	// used for producing jobs and residential roads in the complex
	public Map<Person, Role> population = new HashMap<Person, Role>();
	
	private HousingInfoPanel housingInfoPanel;
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 12;
	
	public ResidentialBuilding(int x, int y, int width, int height) {
		// set up proper coordinates
		super(x, y, width, height);
		
		// Stagger opening/closing time
		this.timeOffset = (instanceCount * timeDifference) % 2;
		instanceCount++;
		
		// set the position of where the door of the complex will be
		this.entrancePos = new XYPos(width/2, height);
		
		// worker for this building
		this.worker = new MaintenanceWorkerRole(null, this);
		
		// manager for this building 
		this.landlord = new PayRecipientRole(null, this);
		
		// set up complex
		this.complex = new HousingComplex(this);
		
		this.worker.setComplex(this.complex);
		
		// put the constant roles in the building map
		this.population.put(null, landlord);
		this.population.put(null, worker);
		
		this.housingInfoPanel = new HousingInfoPanel(population);
	}
	
	public Map<Person, Role> getPopulation(){
		return population;
	}
	
	public void addResident(ResidentRole resident){
		this.population.put(null, resident);
	}
	
	public MaintenanceWorkerRole getWorker(){
		return worker;
	}

	public PayRecipientRole getPayRecipient(){
		return landlord;
	}
	
	public HousingComplex getComplex(){
		return complex;
	}
	
	/* --- Interface Methods --- */
	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

	@Override
	public Role getGreeter() {
		return landlord;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Apartment;
	}

	@Override
	public Role getCustomerRole(Person person) {
		return null;
	}

	@Override
	public JPanel getAnimationPanel() {
		return complex;
	}

	@Override
	public JPanel getInfoPanel() {
		if(!name) { housingInfoPanel.name(this.getName()); name = true; }
		return housingInfoPanel;
	}
	
}
