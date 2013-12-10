package restaurant.vonbeck.gui;

import gui.AnimationPanel;
import gui.Building;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import restaurant.vonbeck.CashierRole;
import restaurant.vonbeck.CookRole;
import restaurant.vonbeck.CustomerRole;
import restaurant.vonbeck.HostRole;
import restaurant.vonbeck.WaiterRole;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;

public class RestaurantVonbeckBuilding extends Building {

	private HostRole host;
	RestaurantGui gui;
	private Map<Person, CustomerRole> existingCustomers = 
			Collections.synchronizedMap(new HashMap<Person, CustomerRole>());
	private AnimationPanel castedAnimationPanel;
	private CashierRole cashier;
	private CookRole cook;
	private RestaurantPanel restPanel;
	
	// TODO Return legitimate panels
	private JPanel infoPanel = new JPanel();
	private JPanel staffPanel = new JPanel();

	public RestaurantVonbeckBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		castedAnimationPanel = new AnimationPanel();
		animationPanel = castedAnimationPanel;
		castedAnimationPanel.addGui(new LayoutGui());
		
		gui = new RestaurantGui(castedAnimationPanel);
		
		cashier = new CashierRole(gui, this);
        cook = new CookRole(cashier, gui, this);
        host = new HostRole("Sarah", gui, cook, cashier, this);
        
        for (int i = 0; i < 3; i++) {
        	WaiterRole waiterRole = new WaiterRole(host, cashier, 
        			cook, gui, "waiterName", i, this);
        	host.addWaiter(waiterRole);
        }
		
		restPanel = new RestaurantPanel(host, cashier, cook, gui);
		gui.setRestPanel(restPanel);
		
	}

	@Override
	public XYPos entrancePos() {
		return new XYPos(Constants.BUILDING_WIDTH/2,
				Constants.BUILDING_HEIGHT);
	}

	@Override
	public Role getGreeter() {
		return host;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Restaurant;
	}

	@Override
	public Role getCustomerRole(Person person) {
		CustomerRole role = existingCustomers.get(person);
		if (role == null) {
			// Create a new role if none exists
			role = new CustomerRole(person.getName());
			CustomerGui g = new CustomerGui(role, gui);
			role.setLocation(this);
			role.setHost(host);
    		role.setPerson(person);
    		gui.getAnimationPanel().addGui(g);
    		role.setGui(g);
			
		} else {
			// Otherwise use the existing role
			role.setPerson(person);
		}
		
		// Add the role to the person, and return it.
		person.addRole(role);
		return role;
	}

	@Override
	public JPanel getAnimationPanel() {
		return animationPanel;
	}

	@Override
	public JPanel getInfoPanel() {
		
		return infoPanel;
	}

}
