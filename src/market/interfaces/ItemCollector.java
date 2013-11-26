package market.interfaces;

import java.util.List;
import java.util.Map;

import agent.interfaces.Person;
import market.Item;
import market.ItemCollectorRole.ItemCollectorstate;

public interface ItemCollector {
	
		//Message
		public abstract void msgGetTheseItem(List<Item> ItemList, Customer c);
		
		public abstract int msgHowManyOrdersYouHave();
		
		public abstract void msgLeaveWork();
		
	//Utilities
		public abstract String getMaitreDName();
		public abstract String getName();
		public abstract void setCashier(Cashier ca);
		public abstract void setGui(ItemCollectorGuiInterfaces itemCollectorGui);
		public abstract void setInventoryList(Map<String, Integer> inventoryList);
		public abstract void setState(ItemCollectorstate s);


	//Animations
		public abstract void AtCollectStation();
		public abstract void Ready();
		public abstract void AtExit();

		public abstract Person getPerson();

		

}