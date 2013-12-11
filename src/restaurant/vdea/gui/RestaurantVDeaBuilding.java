package restaurant.vdea.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import market.interfaces.DeliveryReceiver;
import market.interfaces.PhonePayer;
import restaurant.InfoPanel;
import restaurant.vdea.*;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import gui.RestaurantFakeOrderInterface;
import gui.StaffDisplay;

public class RestaurantVDeaBuilding extends Building
	implements RestaurantFakeOrderInterface{

	private Map<Person, CustomerRole> existingCustomers;
	private HostRole host;
	private CashierRole cashier;
	private CookRole cook;
	private List<WaiterRole> waiters;
	private final int WAITER_STAFF_COUNT = 4;
	
	private InfoPanel infoPanel = new InfoPanel(this);
	RestaurantGui restaurantGui = new RestaurantGui();
	private XYPos entrancePos;
	StaffDisplay staff;
	ScheduleTask task = ScheduleTask.getInstance();
	
	public RestaurantVDeaBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.entrancePos = new XYPos(width/2, height);
		this.existingCustomers = new HashMap<Person, CustomerRole>();
		waiters = new ArrayList<WaiterRole>();
		
		host = new HostRole(null, this);
		cashier = new CashierRole(null, this);
		cook = new CookRole(null, this);
		
		HostGui hostGui = new HostGui(host);
		CookGui cookGui = new CookGui(cook);
		CashierGui cashierGui = new CashierGui(cashier);
		host.setGui(hostGui);
		cook.setGui(cookGui);
		cook.setCashier(cashier);
		cashier.setGui(cashierGui);
		restaurantGui.animationPanel.addGui(hostGui);
		restaurantGui.animationPanel.addGui(cookGui);
		restaurantGui.animationPanel.addGui(cashierGui);
		
		infoPanel = new restaurant.InfoPanel(this);
		infoPanel.setKrabbyPattyPrice("5.99");
		infoPanel.setKelpShakePrice("1.99");
		infoPanel.setCoralBitsPrice("2.99");
		infoPanel.setKelpRingsPrice("3.99");
		infoPanel.setKrabbyPattyInventory(cook.krabbyPatty.getInventory());
		infoPanel.setKelpShakeInventory(cook.kelpShake.getInventory());
		infoPanel.setCoralBitsInventory(cook.coralBits.getInventory());
		infoPanel.setKelpRingsInventory(cook.kelpRings.getInventory());
		
		for(int i=0; i<WAITER_STAFF_COUNT; i++){
			WaiterRole w = new WaiterRole(null, this);
			w.setCashier(cashier);
			w.setCook(cook);
			w.setHost(host);
			host.addWaiter(w);
			waiters.add(w);
			
			WaiterGui waiterGui = new WaiterGui(w, restaurantGui, i);
			w.setGui(waiterGui);
    		restaurantGui.animationPanel.addGui(waiterGui);
		}
		
		staff = super.getStaffPanel();
		staff.addAllWorkRolesToStaffList();
		
		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgAllLeaveWork();
			}
		};		
		task.scheduleDailyTask(command, getClosingHour(), getClosingMinute());
	}
	
	public void updateInventory(){
		infoPanel.setKrabbyPattyInventory(cook.krabbyPatty.getInventory());
		infoPanel.setKelpShakeInventory(cook.kelpShake.getInventory());
		infoPanel.setCoralBitsInventory(cook.coralBits.getInventory());
		infoPanel.setKelpRingsInventory(cook.kelpRings.getInventory());
	}


	private void msgAllLeaveWork() {
		host.msgLeaveWork();
		payEmployee(host.getPerson());
		cashier.msgLeaveWork();
		payEmployee(cashier.getPerson());
		cook.msgLeaveWork();
		payEmployee(cook.getPerson());
		for (WaiterRole w: waiters){
			w.msgLeaveWork();
			payEmployee(w.getPerson());
		}
		
	}
	private void payEmployee(Person emp){
		double empCash = emp.getWallet().getCashOnHand();
		empCash += 200;
		emp.getWallet().setCashOnHand(empCash);
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
			role.setHost(host);
			role.setCashier(cashier);
			
			existingCustomers.put(person, role);
			person.addRole(role);
			
			CustomerGui custGui = new CustomerGui(role, restaurantGui);
			role.setGui(custGui);
			restaurantGui.animationPanel.addGui(custGui);
			
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
		infoPanel.setBuildingName(getName());
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


	@Override
	public DeliveryReceiver getCook() {
		return cook;
	}


	@Override
	public PhonePayer getCashier() {
		return cashier;
	}

}
