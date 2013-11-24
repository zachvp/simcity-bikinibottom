package market.test.mock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import market.test.mock.LoggedEvent;
import market.test.mock.EventLog;
import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;

public class MockItemCollector extends Mock implements ItemCollector {

	public EventLog log = new EventLog();
	public Cashier Cashier;
	
	public MockItemCollector(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGetTheseItem(List<Item> ItemList, Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("ItemCollector : Received message msgGetTheseItem"));
		
		List<Item> tempInventoryList = new ArrayList<Item>();
		{
			tempInventoryList.add(new Item("CheapCar", 1));
			tempInventoryList.add(new Item("ExpensiveCar", 0));
			tempInventoryList.add(new Item("Pizza", 1));
			tempInventoryList.add(new Item("Sandwich", 0));
			tempInventoryList.add(new Item("Chicken", 0));
		}
		
			Cashier.msgHereAreItems(tempInventoryList, new ArrayList<Item>(), c);
		
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
		// TODO Auto-generated method stub
		
	}
	
}