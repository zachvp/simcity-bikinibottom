package market.gui;

import gui.AnimationPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import market.CustomerRole;
import market.Item;
import CommonSimpleClasses.TimeManager;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Constants;
import agent.Role;
import agent.RoleFactory;
import agent.interfaces.Person;




public class MarketBuilding extends gui.Building implements RoleFactory{
	
	XYPos entrancePosition;
	String name;
	AnimationPanel animationPanel = new AnimationPanel();	
	private MarketRecords records = new MarketRecords(animationPanel, this);
	JPanel info = new MarketInfoPanel(getRecords());
	//ATTENTION
		//{records.SetCashierMarketInfoPanel((MarketInfoPanel)info);};
	Map<Person, market.CustomerRole> MarketCustomerMap = new HashMap<Person, market.CustomerRole>();
	
	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static int timeDifference = 6;
	
	public MarketBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		entrancePosition = new XYPos((width/2), height);
		
		// Stagger opening/closing time
		this.timeOffset = instanceCount + timeDifference;
		instanceCount++;
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
		
		List<Item> ShoppingList = new ArrayList<Item>();
		Map<String,Integer> GroceryList = new HashMap<String,Integer>();
		for (int i=0;i<Constants.FOODS.size();i++){
			GroceryList.put(agent.Constants.FOODS.get(i), 100);
		}
		for (int i=0;i<Constants.CARS.size();i++){
			GroceryList.put(agent.Constants.CARS.get(i), 100);
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
		
		if (MarketCustomerMap.containsKey(person)){
			System.out.println("IF STATEMENT In Market's getCustomerRole");
			MarketCustomerMap.get(person).setShoppingList(ShoppingList);
			MarketCustomerMap.get(person).goingToBuy();
			return MarketCustomerMap.get(person);
		}
		else {
			System.out.println(" ELSE In Market's getCustomerRole");
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
	
}