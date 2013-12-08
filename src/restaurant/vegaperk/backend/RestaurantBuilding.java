package restaurant.vegaperk.backend;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import CommonSimpleClasses.XYPos;
import restaurant.vegaperk.gui.RestaurantGui;

@SuppressWarnings("serial")
public class RestaurantBuilding extends Building {
	private XYPos entrancePos;
	
	private RestaurantGui gui = new RestaurantGui();
	
	private Map<Person, CustomerRole> existingCustomerRoles = new HashMap<Person, CustomerRole>();
	
	public RestaurantBuilding(int x, int y, int width, int height) {
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
			role = new CustomerRole("default");
			gui.getAnimationPanel().addGui(role.getGui());
			existingCustomerRoles.put(person, role);
			person.addRole(role);
		}
		
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

}
