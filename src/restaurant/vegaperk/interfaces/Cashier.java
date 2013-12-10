package restaurant.vegaperk.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier extends market.interfaces.PhonePayer {
	/** From Waiter */
	public void msgDoneEating(Customer c, Waiter w, Double d);
	
	/** From Customer */
	public void msgHereIsPayment(Customer c, double p);
	
	public void msgAtDestination();
}