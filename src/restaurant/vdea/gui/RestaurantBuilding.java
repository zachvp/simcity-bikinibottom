package restaurant.vdea.gui;

import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import restaurant.vdea.*;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;

public class RestaurantBuilding extends Building{

	private Map<Person, CustomerRole> existingCustomers;
	private HostRole host;
	private CashierRole cashier;
	private CookRole cook;
	private List<WaiterRole> waiters;
	
	//private InfoPanel infoPanel = new InfoPanel();
	RestaurantGui restaurantGui = new RestaurantGui();
	private XYPos entrancePos;
	
	public RestaurantBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.entrancePos = new XYPos(width/2, height);
	}

	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Restaurant;
	}

	@Override
	public Role getCustomerRole(Person person) {
		// TODO Auto-generated method stub
		return null;
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
	public JPanel getStaffPanel() {
		// TODO Auto-generated method stub
		return new JPanel();
	}

}
