package market.interfaces;

import java.util.List;

import market.Item;
import agent.WorkRole;

public interface DeliveryReceiver {

	public abstract void msgNoItem();
	
	public abstract void msgHereIsYourItems(List<Item> DeliverList);
	
	public abstract void msgHereIsMissingItems(List<Item> MissingItemList);
	
}
