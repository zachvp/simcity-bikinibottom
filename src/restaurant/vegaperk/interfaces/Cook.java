package restaurant.vegaperk.interfaces;

import java.util.Map;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook {
	/** From WaiterAgent */
	public void msgHereIsOrder(Waiter w, String c, int t);
	
	/** From Market(s) */
	public void msgCannotDeliver(Map<String, Integer> cannotDeliver);
	public void msgHereIsDelivery();

	public void msgCanDeliver(Map<String, Integer> canDeliver);
}