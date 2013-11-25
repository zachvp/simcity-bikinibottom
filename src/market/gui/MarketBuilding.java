package market.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import market.CustomerRole;
import market.Item;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import agent.RoleFactory;
import agent.TimeManager;
import agent.gui.AnimationPanel;
import agent.interfaces.Person;




public class MarketBuilding extends gui.Building implements RoleFactory{

	private int startinghour = 8;
	private int startingminutes = 29;
	private int endinghour = 18;
	private int endingminutes = 0;
	
	String name;
	AnimationPanel animationPanel = new AnimationPanel();	
	MarketRecords records = new MarketRecords(animationPanel, this);
	JPanel info = new MarketInfoPanel(records);
	//ATTENTION
	{records.SetCashierMarketInfoPanel((MarketInfoPanel)info);};
	Map<Person, market.CustomerRole> MarketCustomerMap = new HashMap<Person, market.CustomerRole>();
	
	public MarketBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
			//Pass down proper entrance position
		return (new XYPos(0,0));
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
			//Pass Cashier
		return records.ca;
	}
	
	public boolean isOpen() {
		return (TimeManager.getInstance().isNowBetween(startinghour,startingminutes,endinghour,endingminutes));
	}
	

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return LocationTypeEnum.Market;
	}

	@Override
	public Role getCustomerRole(Person person) {
		List<Item> ShoppingList = new ArrayList<Item>();
		Map<String,Integer> GroceryList = person.getResidentRole().getGroceries();
		{
			//FOODS
			for (int i=0;i<agent.Constants.FOODS.size();i++){
				ShoppingList.add(new Item(agent.Constants.FOODS.get(i),GroceryList.get(agent.Constants.FOODS.get(i))));
			}
			//CARS
			for (int i=0;i<agent.Constants.CARS.size();i++){
				ShoppingList.add(new Item(agent.Constants.CARS.get(i),GroceryList.get(agent.Constants.CARS.get(i))));
			}
		}
		
		if (MarketCustomerMap.containsKey(person)){
			MarketCustomerMap.get(person).setShoppingList(ShoppingList);
			return MarketCustomerMap.get(person);
		}
		else {
			CustomerRole role = new CustomerRole(person.getName(), person.getWallet().getCashOnHand(), ShoppingList, person);
			role.setLocation(this);
			role.setPerson(person);
			person.addRole(role);
			return role;
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
	
}