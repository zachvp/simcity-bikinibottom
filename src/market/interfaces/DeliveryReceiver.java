package market.interfaces;

import java.util.List;

import market.Item;

public interface DeliveryReceiver {

	public abstract void msgNoItem();
	
	public abstract void msgHereIsYourItems(List<Item> DeliverList);
	
}
