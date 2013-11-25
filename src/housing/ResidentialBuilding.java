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

public class ResidentialBuilding extends Building {
	CityLocation housingComplex;
	XYPos entrancePos;
	HousingComplex complex = new HousingComplex();
	
	Map<Person, Role> roles = new HashMap<Person, Role>();
	
	// constants
	private final int EMPLOYEE_START_HOUR = 6;
	private final int EMPLOYEE_END_HOUR = 11;

	public ResidentialBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.entrancePos = new XYPos(x + width/2, y + height);
		this.housingComplex = this;
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
