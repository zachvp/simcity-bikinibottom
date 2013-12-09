package restaurant.vegaperk.interfaces;

import restaurant.vegaperk.backend.CashierRole;
import restaurant.vegaperk.interfaces.Customer;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter {
	/** Messages from CustomerAgent */ 
	public void msgReadyToOrder(Customer c);
	
	public void msgHereIsMyOrder(Customer c, String choice);
	
	public void msgCustomerLeavingTable(Customer c);
	
	public void msgCannotPay(Customer c);
	
	public void msgIAmDoneEating(Customer c);
	
	/** Messages from the Cashier */
	public void msgHereIsCheck(Customer c, double check);

	public void msgOutOfChoice(String choice, int table);

	public void msgOrderDone(String choice, int table);

	/** Messages from the Host */
	public void msgPleaseSeatCustomer(Customer customer, int tableID);

	public void msgCanGoOnBreak();

	public void msgDenyBreak();

	public String getName();

	public int getCustomerCount();

	public void msgHomePosition(int position);

	void setCashier(CashierRole cashier);
}