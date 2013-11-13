package market.interfaces;

import java.util.List;

import market.Item;

public interface ItemCollector {
	
	public abstract String getMaitreDName();

	public abstract String getName();
	
	//Message
		public abstract void msgGetTheseItem(List<Item> ItemList, Customer c);
		
		public abstract int msgHowManyOrdersYouHave();
}