package market.interfaces;

import java.util.List;




import market.Item;

public interface Customer {
	
	//Message
	public abstract void msgHereisYourTotal(double cost);
	
	public abstract void msgHereisYourItem(List<Item> Items);

	//Utilities
	public abstract String getMaitreDName();
	public abstract String getName();
	public abstract void setCashier(Cashier ca);
}