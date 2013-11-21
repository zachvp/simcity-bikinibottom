package market.interfaces;
import java.util.List;

import market.Item;


public interface DeliveryGuy {
	
	// Messages
		public abstract boolean msgAreYouAvailable();
	
		public abstract void msgDeliverIt(List<Item> DeliveryList, Customer OrdePerson, CityBuilding building);
	
	//Utilities
		public abstract String getMaitreDName();
		public abstract String getName();
		public abstract void setCashier(Cashier ca);
}