package market.interfaces;

import java.util.List;




import market.Item;

public interface Customer {
	
	//Message
	public abstract void msgHereisYourTotal(double cost, List<Item> MissingItems);
	
	public abstract void msgHereisYourItem(List<Item> Items);
	
	public abstract void msgNoItem();

	//Utilities
	public abstract String getMaitreDName();
	public abstract String getName();
	public abstract void setCashier(Cashier ca);
	public abstract void setShoppingList(List<Item>SL);
}