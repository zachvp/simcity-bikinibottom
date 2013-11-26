package market.test.mock;


import java.util.List;


import java.util.Map;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.DeliveryReceiver;
import market.interfaces.ItemCollector;
import market.interfaces.PhonePayer;

public class MockCashier extends Mock implements Cashier {

	public EventLog log = new EventLog();
	Customer customer ;
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public ItemCollector itemCollector;
	public List<Item>SL;
	public List<Item>MissingList;
	public List<Item>DeliverList;
	
	public MockCashier(String name) {
		super(name);

	}



	@Override
	public void msgIWantItem(List<Item> ShoppingList, Customer C) {
		// TODO Auto-generated method stub
		SL = ShoppingList;
		log.add(new LoggedEvent("Cashier : Received message msgIWantItem"));
		double cost = 0;
		for (int i =0; i<ShoppingList.size();i++){
			double CurrentItemPrice = Constants.MarketPriceList.get(ShoppingList.get(i).name);
			cost += CurrentItemPrice*ShoppingList.get(i).amount;
		}
		customer.msgHereisYourTotal(cost, MissingList);
		
	}

	@Override
	public void msgHereAreItems(List<Item> Items, List<Item> MissingItems) {
		DeliverList = Items;
		MissingList = MissingItems;
		
	}

	@Override
	public void msgHereIsPayment(double payment, Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Cashier : Received message msgHereIsPayment"));
		
		customer.msgHereisYourItem(SL);
		
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

	public void setCustomer (Customer c){
		customer = c;
	}



	@Override
	public void msgPhoneOrder(List<Item> ShoppingList, PhonePayer pP,
			DeliveryReceiver rP, CityLocation building) {
		// TODO Auto-generated method stub
		
	}

	

	

}
