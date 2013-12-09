package restaurant.vdea.gui;

import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import restaurant.vdea.*;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import gui.StaffDisplay;

public class RestaurantBuilding extends Building{

	private Map<Person, CustomerRole> existingCustomers;
	private HostRole host;
	private CashierRole cashier;
	private CookRole cook;
	private List<WaiterRole> waiters;
	
	//private InfoPanel infoPanel = new InfoPanel();
	RestaurantGui restaurantGui = new RestaurantGui();
	private XYPos entrancePos;
	StaffDisplay staff;
	
	public RestaurantBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.entrancePos = new XYPos(width/2, height);
		
		host = new HostRole(null, this);
		cashier = new CashierRole(null, this);
		cook = new CookRole(null, this);
		
		HostGui hostGui = new HostGui(host);
		CookGui cookGui = new CookGui(cook);
		host.setGui(hostGui);
		cook.setGui(cookGui);
		
		staff = super.getStaffPanel();
		staff.addAllWorkRolesToStaffList();
	}

	@Override
	public XYPos entrancePos() {
		return entrancePos;
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
		return restaurantGui.getAnimationPanel();
	}

	@Override
	public JPanel getInfoPanel() {
		// TODO Auto-generated method stub
		return new JPanel();
	}

	@Override
	public StaffDisplay getStaffPanel() {
		return staff;
	}

}
