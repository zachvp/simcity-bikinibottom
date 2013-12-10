package restaurant.vegaperk.interfaces;

import java.util.Map;

import market.interfaces.DeliveryReceiver;
import restaurant.vegaperk.backend.MarketAgent;
import restaurant.vegaperk.gui.CookGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook extends DeliveryReceiver {
	/** From WaiterAgent */
	public void msgHereIsOrder(Waiter w, String c, int t);
	
	/** From Market(s) */
	public void msgCannotDeliver(Map<String, Integer> cannotDeliver);
	public void msgHereIsDelivery();

	public void msgCanDeliver(Map<String, Integer> canDeliver);

	public void msgAtDestination();

	public void setGui(CookGui gui);

	public void addMarket(MarketAgent m1);
}