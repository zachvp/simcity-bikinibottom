package restaurant.vegaperk.backend;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import market.interfaces.DeliveryReceiver;
import market.interfaces.PhonePayer;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import gui.RestaurantFakeOrderInterface;
import gui.StaffDisplay;
import CommonSimpleClasses.XYPos;
import restaurant.InfoPanel;
import restaurant.vegaperk.gui.RestaurantGui;
import restaurant.vegaperk.gui.RestaurantPanel;

@SuppressWarnings("serial")
public class RestaurantVegaPerkBuilding extends Building
	implements RestaurantFakeOrderInterface{
	private XYPos entrancePos;
	
	private RestaurantGui gui = new RestaurantGui(this);
	
	private Map<Person, CustomerRole> existingCustomerRoles = new HashMap<Person, CustomerRole>();
	
	private StaffDisplay staff;
	private InfoPanel infoPanel = new InfoPanel(this);
	
	public RestaurantVegaPerkBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.entrancePos = new XYPos(width / 2, height);
		
		staff = super.getStaffPanel();
		staff.addAllWorkRolesToStaffList();
		
		infoPanel = new restaurant.InfoPanel(this);
		infoPanel.setKrabbyPattyPrice("1.00");
		infoPanel.setKelpShakePrice("1.00");
		infoPanel.setCoralBitsPrice("1.00");
		infoPanel.setKelpRingsPrice("1.00");
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
		infoPanel.setBuildingName(getName());
		return infoPanel;
	}

	@Override
	public StaffDisplay getStaffPanel() {
		return staff;
	}
	
	@Override public boolean isOpen() {
		return true;
	}

	@Override
	public DeliveryReceiver getCook() {
		return gui.getRestPanel().cook;
	}

	@Override
	public PhonePayer getCashier() {
		return gui.getRestPanel().cashier;
	}

}
