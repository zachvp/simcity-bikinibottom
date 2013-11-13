package market.interfaces;
import java.util.List;

import market.Item;


public interface DeliveryGuy {

	public abstract String getMaitreDName();

	public abstract String getName();

	// Messages
	public abstract boolean msgAreYouAvailable();
	
	public abstract void msgDeliverIt(List<Item> DeliveryList, CityBuilding building);
}