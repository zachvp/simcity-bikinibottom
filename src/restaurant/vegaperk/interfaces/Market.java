package restaurant.vegaperk.interfaces;

import java.util.Map;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Market {

	/** From Cook */
	public void msgNeedFood(Map<String, Integer> groceries);
	
	/** From Cashier */
	public void msgHereIsPayment(double payment);
}