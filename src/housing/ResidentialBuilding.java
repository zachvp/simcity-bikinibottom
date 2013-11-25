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
	
	// used for producing jobs and residential roads in the complex
	Map<Person, Role> population;
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static int timeDifference = 6;
	
	public ResidentialBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.entrancePos = new XYPos(width/2, height);
		this.housingComplex = this;
		this.complex = new HousingComplex();

		this.population = new HashMap<Person, Role>();
		
		// Stagger opening/closing time
		this.timeOffset = instanceCount + timeDifference;
		instanceCount++;
	}

	
	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

	@Override
	public Role getGreeter() {
		return (Role) complex.getPayRecipientRole();
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Apartment;
	}

	@Override
	public Role getCustomerRole(Person person) {
		Role role = population.get(person);
		if(role == null) {
			role = new ResidentRole((PersonAgent) person);
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
