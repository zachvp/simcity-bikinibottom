package market.gui;

import gui.AnimationPanel;
import gui.StaffDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import market.CustomerRole;
import market.Item;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.RoleFactory;
import agent.WorkRole;
import agent.interfaces.Person;




public class MarketBuilding extends gui.Building implements RoleFactory{
	
	XYPos entrancePosition;
	String name;
	AnimationPanel animationPanel = new AnimationPanel();	
	private MarketRecords records;
	JPanel info;
	StaffDisplay staff;
	//ATTENTION
		//{records.SetCashierMarketInfoPanel((MarketInfoPanel)info);};
	Map<Person, market.CustomerRole> MarketCustomerMap = new HashMap<Person, market.CustomerRole>();
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 12;
	
	public MarketBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		entrancePosition = new XYPos((width/2), height);
		
		// Stagger opening/closing time
		this.timeOffset = (instanceCount * timeDifference) % 2;
		instanceCount++;
		
		records = new MarketRecords(animationPanel, this);
		MarketInfoPanel infoPanel = new MarketInfoPanel(getRecords());
		info = infoPanel;
		records.SetCashierMarketInfoPanel(infoPanel);
		
		//staff = new StaffDisplay();
		//staff.setBuilding(this);
		staff = super.getStaffPanel();
		staff.addAllWorkRolesToStaffList();
	}

	@Override
	public XYPos entrancePos() {
		return (entrancePosition);
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
			//Pass Cashier
		return getRecords().cashier;
	}	

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return LocationTypeEnum.Market;
	}

	@Override
	public Role getCustomerRole(Person person) {
		
		/*
		List<Item> ShoppingList = new ArrayList<Item>();
		Map<String,Integer> GroceryList = new HashMap<String,Integer>();
		for (int i=0;i<Constants.FOODS.size();i++){
			GroceryList.put(Constants.FOODS.get(i), 100);
		}
		for (int i=0;i<Constants.CARS.size();i++){
			GroceryList.put(Constants.CARS.get(i), 100);
		}
		{
			//FOODS
			for (int i=0;i<CommonSimpleClasses.Constants.FOODS.size();i++){
				ShoppingList.add(new Item(CommonSimpleClasses.Constants.FOODS.get(i),GroceryList.get(CommonSimpleClasses.Constants.FOODS.get(i))));
			}
			//CARS
			for (int i=0;i<CommonSimpleClasses.Constants.CARS.size();i++){
				ShoppingList.add(new Item(CommonSimpleClasses.Constants.CARS.get(i),GroceryList.get(CommonSimpleClasses.Constants.CARS.get(i))));
			}
		}
		*/
		List<Item> ShoppingList = new ArrayList<Item>();
		Map<String,Integer> GroceryList = person.getShoppingList();
		boolean empty = true;
		for (int i=0;i<Constants.FOODS.size();i++){
			Integer numOfItems = GroceryList.get(Constants.FOODS.get(i));
			if (numOfItems == null) numOfItems = 0;
			if (numOfItems > 0) empty = false;
			ShoppingList.add(   new Item(   Constants.FOODS.get(i), numOfItems)    );
		}
		for (int i=0;i<Constants.CARS.size();i++){
			Integer numOfItems = GroceryList.get(Constants.CARS.get(i));
			if (numOfItems == null) numOfItems = 0;
			if (numOfItems > 0) empty = false;
			ShoppingList.add(new Item(Constants.CARS.get(i), numOfItems));
		}
		if (empty) {
			ShoppingList.add(new Item(Constants.FOODS.get(0), 5));
		}
		
		if (MarketCustomerMap.containsKey(person)){
			//System.out.println("IF STATEMENT In Market's getCustomerRole");
			MarketCustomerMap.get(person).setShoppingList(ShoppingList);
			MarketCustomerMap.get(person).goingToBuy();
			return MarketCustomerMap.get(person);
		}
		else {
			//System.out.println(" ELSE In Market's getCustomerRole");
			Role role = records.CreateCustomerRole(person.getName(), person.getWallet().getCashOnHand(), ShoppingList, person);
			MarketCustomerMap.put(person, (CustomerRole)role);
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
		// TODO Auto-generated method stub
		return info;
	}
	
	@Override
	public StaffDisplay getStaffPanel() {
		return staff;
	}
	
	public void UpdateInfoPanel(){
		((MarketInfoPanel) info).UpdateInventoryLevelWithoutButton();
		return;
	}

	public MarketRecords getRecords() {
		return records;
	}

	public void setRecords(MarketRecords records) {
		this.records = records;
	}
	
	@Override
	public boolean isOpen() {
		return cashierOnDuty() && /*deliveryGuyOnDuty() &&*/ itemCollectorOnDuty();
	}
	
	public boolean cashierOnDuty() {
		return records.cashier.isAtWork();
	}
	
	public boolean deliveryGuyOnDuty() {
		for (DeliveryGuy dg : records.cashier.getDGList()) {
			if (dg instanceof WorkRole && ((WorkRole) dg).isAtWork()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean itemCollectorOnDuty() {
		for (ItemCollector ic : records.cashier.getICList()) {
			if (ic instanceof WorkRole && ((WorkRole) ic).isAtWork()) {
				return true;
			}
		}
		return false;
	}

	
	
}