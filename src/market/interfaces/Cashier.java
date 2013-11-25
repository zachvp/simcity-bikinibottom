package market.interfaces;

import java.util.List;
import java.util.Map;

import CommonSimpleClasses.CityBuilding;
import market.Item;

public interface Cashier {



	// Messages
	public abstract void msgPhoneOrder(List<Item>ShoppingList, Customer C, CityBuilding building);

	public abstract void msgIWantItem(List<Item> ShoppingList, Customer C);

	public abstract void msgHereAreItems(List<Item> Items, List<Item> MissingItems, Customer c);

	public abstract void msgHereIsPayment(double payment, Customer c);
	
	public abstract void msgLeaveWork();

	//Utilities
	public abstract void setDGList(List<DeliveryGuy> list);
	public abstract void addDGList(DeliveryGuy DG);
	public abstract void setICList(List<ItemCollector> list);
	public abstract void addICList(ItemCollector IC, Map<String,Integer> IList);
	public abstract Map<String, Integer> getInventoryList();
	public abstract String getMaitreDName();
	public abstract String getName();

	//Animations
	public abstract void AtFrontDesk();
	public abstract void AtBench();
	public abstract void AtExit();

	
	
	
	
}