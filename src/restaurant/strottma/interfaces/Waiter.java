package restaurant.strottma.interfaces;

import restaurant.strottma.HostRole.Table;
import restaurant.strottma.interfaces.Cook.GrillOrPlate;

public interface Waiter {
	/**
	 * Sent from the cashier; the waiter should pass this along to the customer.
	 * 
	 * @param c the customer who this bill belongs to
	 * @param bill how much the customer owes
	 */
	public void msgHereIsBill(Customer c, double bill);

	// TODO: table is currently an inner class of HostAgent. Fix that eventually.
	public void msgOutOfChoice(Customer customer, Table table, String choice);

	public void msgOrderIsReady(Customer customer, Table table, String choice, GrillOrPlate plateArea);

	public void msgIAmReadyToOrder(Customer customer);

	public void msgDoneEating(Customer customer);

	public void msgLeaving(Customer customert);

	public void msgHereIsMyChoice(Customer customer, String choice);
}
