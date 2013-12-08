package restaurant.vegaperk.interfaces;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	/** From Waiter */
	public void msgDoneEating(Customer c, double b, Waiter w);
	
	/** From Customer */
	public void msgHereIsPayment(Customer c, double p);
	
	/** From Market  */
	public void msgHereIsBill(double bill, Market m);
}