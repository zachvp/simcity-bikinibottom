package market.test.mock;


import java.util.List;


import java.util.Map;

import CommonSimpleClasses.CityBuilding;
import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;

public class MockCashier extends Mock implements Cashier {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public ItemCollector itemCollector;
	public List<Item>ShoppingList;
	
	public MockCashier(String name) {
		super(name);

	}

	@Override
	public void msgPhoneOrder(List<Item> ShoppingList, Customer C,
			CityBuilding building) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantItem(List<Item> ShoppingList, Customer C) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereAreItems(List<Item> Items, List<Item> MissingItems,
			Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double payment, Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDGList(List<DeliveryGuy> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDGList(DeliveryGuy DG) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setICList(List<ItemCollector> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addICList(ItemCollector IC, Map<String, Integer> IList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtFrontDesk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtBench() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setItemCollector(ItemCollector ic){
		itemCollector = ic;
	}

	@Override
	public Map<String, Integer> getInventoryList() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
