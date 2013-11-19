package market.interfaces;

import java.util.List;

import market.Item;

public interface Cashier {



	// Messages
	public abstract void msgPhoneOrder(List<Item>ShoppingList, Customer C, CityBuilding building);

	public abstract void msgIWantItem(List<Item> ShoppingList, Customer C);

	public abstract void msgHereAreItems(List<Item> Items, List<Item> MissingItems, Customer c);

	public abstract void msgHereIsPayment(double payment, Customer c);

	//Utilities
	public abstract void setDGList(List<DeliveryGuy> list);
	public abstract void addDGList(DeliveryGuy DG);
	public abstract void setICList(List<ItemCollector> list);
	public abstract void addICList(ItemCollector IC);
	public abstract String getMaitreDName();
	public abstract String getName();
	
	
}