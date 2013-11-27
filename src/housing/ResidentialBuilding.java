package housing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
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
//	private CityLocation residence;
	
	// location for the "door" to the building
	private XYPos entrancePos;
	
	// this displays after clicking on the ResidentialBuilding
	private HousingComplex complex;
	
	// the "boss" or greeter for this building and the on-call Mr. Fix-it
	private PayRecipientRole landlord;
	private MaintenanceWorkerRole worker;
	
	// used for producing jobs and residential roads in the complex
	public Map<Person, Role> population;
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 6;
	
	public ResidentialBuilding(int x, int y, int width, int height) {
		// set up proper coordinates
		super(x, y, width, height);
		
		// Stagger opening/closing time
		this.timeOffset = instanceCount * timeDifference;
		instanceCount++;
		
		this.entrancePos = new XYPos(width/2, height);
		
		// keeps track of building members
		this.population = new HashMap<Person, Role>();

		// worker for this building
		worker = new MaintenanceWorkerRole(null, this);
		
		// manager for this building 
		landlord = new PayRecipientRole(null, this);
		
		// set up complex
		this.complex = new HousingComplex(this);
		
		worker.setComplex(this.complex);
		
		// put the constant roles in the building map
		this.population.put(null, landlord);
		this.population.put(null, worker);
		
	}
	
	public Set<Person> getPopulation(){
		return population.keySet();
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
		return new HousingInfoPanel(this);
	}
	
}
