package restaurant.anthony.gui;

import gui.AnimationPanel;
import gui.StaffDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;






import restaurant.InfoPanel;
import restaurant.anthony.CustomerRole;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.RoleFactory;
import agent.WorkRole;
import agent.interfaces.Person;




public class RestaurantBuilding extends gui.Building implements RoleFactory{
	
	XYPos entrancePosition;
	String name;
	AnimationPanel animationPanel = new AnimationPanel();	
	private RestaurantRecords records;
	private InfoPanel infoPanel = new InfoPanel(this);
	StaffDisplay staff;
	//ATTENTION
		//{records.SetCashierMarketInfoPanel((MarketInfoPanel)info);};
	Map<Person, CustomerRole> RestaurantCustomerMap = new HashMap<Person, CustomerRole>();
	RestaurantBackgroundLayoutGui BackgroundLayout = new RestaurantBackgroundLayoutGui();
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 12;
	
	public RestaurantBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		entrancePosition = new XYPos((width/2), height);
		
		// Stagger opening/closing time
		this.timeOffset = (instanceCount * timeDifference) % 2;
		instanceCount++;
		records = new RestaurantRecords(animationPanel, this);
		//RestaurantInfoPanel infoPanel = new RestaurantInfoPanel(getRecords());
		//info = infoPanel;
		//records.SetCashierMarketInfoPanel(infoPanel);
		
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
		return (entrancePosition);
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
			//Pass Cashier
		return records.host;
	}	

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return LocationTypeEnum.Restaurant;
	}

	@Override
	public Role getCustomerRole(Person person) {
		
		if (RestaurantCustomerMap.containsKey(person)){
			//System.out.println("IF STATEMENT In Market's getCustomerRole");
			RestaurantCustomerMap.get(person).activate();
			return RestaurantCustomerMap.get(person);
		}
		else {
			//System.out.println(" ELSE In Market's getCustomerRole");
			Role role = records.CreateCustomerRole(person);
			RestaurantCustomerMap.put(person, (CustomerRole)role);
			return role;
			/*
			CustomerRole role = new CustomerRole(person.getName(), person.getWallet().getCashOnHand(), ShoppingList, person);
			CustomerGui custGui = new CustomerGui(role);
			role.setGui(custGui);
			role.setLocation(this);
			role.setPerson(person);
			role.goingToBuy();
			person.addRole(role);
			return role;
			*/
		}
	}

	@Override
	public JPanel getAnimationPanel() {
		// TODO Auto-generated method stub
		return animationPanel;
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
	
	public void UpdateInfoPanel(){
		//((MarketInfoPanel) info).UpdateInventoryLevelWithoutButton();
		return;
	}

	public RestaurantRecords getRecords() {
		return records;
	}

	public void setRecords(RestaurantRecords records) {
		this.records = records;
	}
	
	@Override
	public boolean isOpen() {
		return cashierOnDuty() && hostOnDuty() && cookOnDuty() && waiterOnDuty();
	}
	
	public boolean hostOnDuty() {
		return records.host.isAtWork();
	}
	
	public boolean cashierOnDuty() {
		return records.host.cashier.isAtWork();
	}
	
	public boolean cookOnDuty() {
		return records.host.cook.isAtWork();
	}
	
	public boolean waiterOnDuty() {
		for (int i=0;i<records.host.Waiters.size();i++){
			if (records.host.Waiters.get(i).isAtWork()){
				return true;
			}
		}
		return false;
	}
	
	
	
	
}