package restaurant.strottma.gui;

import gui.Building;
import gui.StaffDisplay;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import restaurant.strottma.CashierRole;
import restaurant.strottma.CookRole;
import restaurant.strottma.CustomerRole;
import restaurant.strottma.WaiterRole;
import restaurant.strottma.HostRole;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;

/**
 * Represents Erik Strottmann's restaurant.
 * 
 * @author Erik Strottmann
 * @see Building
 */
public class RestaurantStrottmaBuilding extends Building {
	private Map<Person, CustomerRole> existingCustomers;
	private HostRole host;
	private CashierRole cashier;
	private CookRole cook;
	private List<WaiterRole> waiters;
	
	private InfoPanel infoPanel = new InfoPanel();
	RestaurantGui restaurantGui = new RestaurantGui();
	StaffDisplay staff;
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 12;
	
	public RestaurantStrottmaBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.existingCustomers = new HashMap<Person, CustomerRole>();
		
		// Stagger opening/closing time
		this.timeOffset = (instanceCount * timeDifference) % 2;
		instanceCount++;
		
		initRoles();
		staff = super.getStaffPanel();
		staff.addAllWorkRolesToStaffList();
	}
	
	private void initRoles() {
		// Instantiate WorkRoles
		host = new HostRole(null, this);
		cashier = new CashierRole(null, this);
		cook = new CookRole(null, this);
		
		// Create GUIs
		// TODO there's no cashier gui
		HostGui hostGui = new HostGui(host);
		// CashierGui cashierGui = new CashierGui(cashier);
		CookGui cookGui = new CookGui(cook);
		
		// Add GUIs to roles
		host.setGui(hostGui);
		// cashier.setGui(cashierGui);
		cook.setGui(cookGui);
		
		// Add GUIs to animation panel
		restaurantGui.getAnimationPanel().addGui(hostGui);
		// restaurantGui.getAnimationPanel().addGui(cashierGui);
		restaurantGui.getAnimationPanel().addGui(cookGui);
		
		
		// Now the same for waiters
		waiters = new ArrayList<WaiterRole>();
		
		for (int i = 0; i < 4; i++) {
			// Create the waiter and add it to the list
			WaiterRole w = new WaiterRole(null, this);
			waiters.add(w);
			
			// Set references between the waiter and other roles
			w.setOtherRoles(host, cook, cashier);
			host.addWaiter(w);
			
			// Create and set up the waiter GUI
			WaiterGui wGui = new WaiterGui(w, restaurantGui);
			w.setGui(wGui);
			restaurantGui.getAnimationPanel().addGui(wGui);
		}
		
	}
	
	/**
	 * Returns the entrance of this building: on its bottom side.
	 */
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
			role = new CustomerRole(person, this);
			role.setLocation(this);
			role.setHost(host);
			
			CustomerGui custGui = new CustomerGui(role, restaurantGui);
			role.setGui(custGui);
			restaurantGui.getAnimationPanel().addGui(custGui);
			
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
		// TODO initialize the animation panel
		//JPanel animationPanel = new agent.gui.AnimationPanel();
		return restaurantGui.getAnimationPanel();
	}
	
	@Override
	public JPanel getInfoPanel() {
		// TODO initialize the info panel
		return infoPanel;
	}
	
	@Override
	public StaffDisplay getStaffPanel() {
		return staff;
	}
	
	@Override
	public boolean isOpen() {
		return hostOnDuty() && cashierOnDuty() && cookOnDuty() &&
				waiterOnDuty();
	}

	public boolean hostOnDuty() {
		return host != null && host.isAtWork();
	}

	public boolean cashierOnDuty() {
		return cashier != null && cashier.isAtWork();
	}

	public boolean cookOnDuty() {
		return cook != null && cook.isAtWork();
	}

	public boolean waiterOnDuty() {
		for (WaiterRole w : waiters) {
			if (w.isAtWork()) { return true; }
		}
		return false;
	}
	
}
