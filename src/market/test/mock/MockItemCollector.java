package market.test.mock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agent.Constants;
import market.test.mock.LoggedEvent;
import market.test.mock.EventLog;
import market.Item;
import market.ItemCollectorRole.ItemCollectorstate;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;

public class MockItemCollector extends Mock implements ItemCollector {

	public EventLog log = new EventLog();
	public Cashier Cashier;
	Map<String, Integer> tempInventoryList = null;
	
	public MockItemCollector(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGetTheseItem(List<Item> ItemList, Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("ItemCollector : Received message msgGetTheseItem"));
		
		/*
		List<Item> ShoppingList = new ArrayList<Item>();
		for (int i = 0 ; i < Constants.CARS.size(); i ++){
			ShoppingList.add(new Item(Constants.CARS.get(i),tempInventoryList.get(Constants.CARS.get(i))));
		}
		for (int i = 0 ; i < Constants.FOODS.size(); i ++){
			ShoppingList.add(new Item(Constants.FOODS.get(i),tempInventoryList.get(Constants.FOODS.get(i))));
		}
		*/
		
		
			Cashier.msgHereAreItems(ItemList, new ArrayList<Item>(), c);
		
	}

	@Override
	public int msgHowManyOrdersYouHave() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtCollectStation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ready() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInventoryList(Map<String, Integer> inventoryList) {
		tempInventoryList = inventoryList;
		
	}

	@Override
	public void setGui(ItemCollectorGuiInterfaces itemCollectorGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setState(ItemCollectorstate s) {
		// TODO Auto-generated method stub
		
	}
	
}