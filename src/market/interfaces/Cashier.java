package market.interfaces;

import java.util.List;

import market.Item;

public interface Cashier {

	public abstract String getMaitreDName();

	public abstract String getName();

	// Messages
	public abstract void msgPhoneOrder(List<Item>ShoppingList, Customer C, CityBuilding building);

	public abstract void msgIWantItem(List<Item> ShoppingList, Customer C);

	public abstract void msgHereAreItems(List<Item> Items, Customer c);

	public abstract void msgHereIsPayment(double payment, Customer c);

}