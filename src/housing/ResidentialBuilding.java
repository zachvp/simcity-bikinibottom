package housing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import bank.BankCustomerRole;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import housing.gui.HousingComplex;
import housing.gui.ResidentRoleGui;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.PayRecipient;
import housing.interfaces.ResidentGui;

/**
 * ResidentialBuilding is the class that will be slotted into the city map itself.
 * It will then display in the overview city map. Upon clicking on it, the view within
 * the ResidentialBuilding with the detailed housing animations will pop up. 
 * @author Zach VP
 */

public class ResidentialBuilding extends Building {
	// ResidentialBuilding is a CityLocation that will be added to kelp
	CityLocation housingComplex;
	
	// location for the "door" to the building
	XYPos entrancePos;
	
	// this displays after clicking on the ResidentialBuilding
	HousingComplex complex;
	
	// manages the complex
	private PayRecipientRole payRecipientRole;
	
	// takes care of problems in the complex
	private MaintenanceWorker worker;
	
	// used for producing jobs and residential roads in the complex
	Map<Person, ResidentRole> population;
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static int timeDifference = 6;
	
	public ResidentialBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.entrancePos = new XYPos(width/2, height);
		this.housingComplex = this;
		this.complex = new HousingComplex();

		this.population = new HashMap<Person, ResidentRole>();
		
		// Stagger opening/closing time
		this.timeOffset = instanceCount + timeDifference;
		instanceCount++;
		
		this.initializeRoles();
	}

	private void initializeRoles(){
		// two objects that are common to all the dwellings in the complex
		payRecipientRole = new PayRecipientRole(null, this);
	 	worker = new MaintenanceWorkerRole(null, this);
	}
	
	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

	@Override
	public Role getGreeter() {
		return payRecipientRole;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Apartment;
	}

	@Override
	public Role getCustomerRole(Person person) {
		ResidentRole role = population.get(person);
		
		if(role == null) {
			role = new ResidentRole((PersonAgent) person, this);
			ResidentGui gui = new ResidentRoleGui(role);
			role.setGui(gui);
			role.setLocation(housingComplex);
		}
		
		else {
			role.setPerson(person);
		}
		
		person.addRole(role);
		return role;
	}

	@Override
	public JPanel getAnimationPanel() {
		return complex;
	}

	@Override
	public JPanel getInfoPanel() {
		// TODO Auto-generated method stub
		return new JPanel();
	}
	
}
