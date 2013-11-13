package market.interfaces;

import java.util.List;



import market.Item;

public interface Customer {
	
	public abstract String getMaitreDName();

	public abstract String getName();
	
	//Message
	public abstract void msgHereisYourTotal(double cost);
	
	public abstract void msgHereisYourItem(List<Item> Items);

}