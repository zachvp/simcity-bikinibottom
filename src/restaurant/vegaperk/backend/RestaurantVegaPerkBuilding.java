package restaurant.vegaperk.backend;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import CommonSimpleClasses.XYPos;
import restaurant.vegaperk.gui.RestaurantGui;
import restaurant.vegaperk.gui.RestaurantPanel;

@SuppressWarnings("serial")
public class RestaurantVegaPerkBuilding extends Building {
	private XYPos entrancePos;
	
	private RestaurantGui gui = new RestaurantGui(this);
	
	private Map<Person, CustomerRole> existingCustomerRoles = new HashMap<Person, CustomerRole>();
	
	public RestaurantVegaPerkBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.entrancePos = new XYPos(width / 2, height);
		
	}

	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

	@Override
	public Role getGreeter() {
		return gui.getHost();
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Restaurant;
	}

	@Override
	public Role getCustomerRole(Person person) {
		CustomerRole role = existingCustomerRoles.get(person);
		
		// TODO implement person, building constructor for customer
		if(role == null) {
			role = ((RestaurantPanel) getInfoPanel()).addCustomer("Customers", person.getName(), person);
		}
		else {
			role.setPerson(person);
		}
		
		person.addRole(role);
		return role;
	}

	@Override
	public JPanel getAnimationPanel() {
		return gui.getAnimationPanel();
	}

	@Override
	public JPanel getInfoPanel() {
		return gui.getInfoPanel();
	}

	@Override
	public JPanel getStaffPanel() {
		// TODO Auto-generated method stub
		return gui.getInfoPanel();
	}

}
