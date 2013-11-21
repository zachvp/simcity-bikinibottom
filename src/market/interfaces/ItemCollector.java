package market.interfaces;

import java.util.List;
import java.util.Map;

import market.Item;

public interface ItemCollector {
	
	//Message
		public abstract void msgGetTheseItem(List<Item> ItemList, Customer c);
		
		public abstract int msgHowManyOrdersYouHave();
		
	//Utilities
		public abstract String getMaitreDName();
		public abstract String getName();
		public abstract void setCashier(Cashier ca);

		public abstract void setInventoryList(Map<String, Item> inventoryList);
}