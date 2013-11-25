package housing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
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
	Map<Person, Role> roles;
	
	// constants
	private final int EMPLOYEE_START_HOUR = 6;
	private final int EMPLOYEE_END_HOUR = 11;

	public ResidentialBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.entrancePos = new XYPos(x + width/2, y + height);
		this.housingComplex = this;
		this.complex = new HousingComplex();
		this.roles = new HashMap<Person, Role>();
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
		
		return this.getGreeter();
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
