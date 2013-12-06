package housing.backend;

import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import housing.gui.HousingInfoPanel;

/**
 * ResidentialBuilding is the class that will be slotted into the city map itself.
 * It is the wrapper for the {@link HousingComplx} that interfaces with the city GUI.
 * Upon clicking in the corresponding ResidentialBuilding area, the view within
 * the {@link HousingComplex} with the detailed housing animations will pop up. 
 * @author Zach VP
 */

@SuppressWarnings("serial")
public class ResidentialBuilding extends Building {
	// ResidentialBuilding is a CityLocation that will be added to kelp
	
	// location for the "door" to the building
	private XYPos entrancePos;
	
	// this displays after clicking on the ResidentialBuilding
	private HousingComplex complex;
	
	// prevents the repeated retrieval of the building name
	private boolean name = false;
	
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

		// set up the housing complex containing the building roles and GUI
		this.complex = new HousingComplex(this);
		
		this.housingInfoPanel = new HousingInfoPanel(getPopulation());
	}
	
	public Map<Person, Role> getPopulation(){
		return complex.getPopulation();
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
		return complex.getPayRecipient();
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
		return complex.getGui();
	}

	@Override
	public JPanel getInfoPanel() {
		if(!name) { housingInfoPanel.name(this.getName()); name = true; }
		return housingInfoPanel;
	}
	
}
