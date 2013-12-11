package restaurant.vdea.interfaces;

import java.util.List;

import restaurant.vdea.Food;


/**
 * Cook interface built to unit test a CookAgent.
 *
 * @author Victoria Dea
 *
 */
public interface Cook extends market.interfaces.DeliveryReceiver{

	public abstract void msgThereIsAnOrder(Waiter w, String choice, int table);
	
	//public abstract void msgOrderFufillment(List<Food> shipment, boolean orderFull);

	public abstract void gotFood(int table);
}
