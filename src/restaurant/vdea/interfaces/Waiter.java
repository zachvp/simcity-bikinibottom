package restaurant.vdea.interfaces;

public interface Waiter {

	
	public abstract void msgSeatCustomer(Customer customer, int tableNumber);

	public abstract void msgReadyToOrder(Customer customer);

	public abstract void msgHereIsMyOrder(Customer customer, String choice);
	
	public abstract void msgNotEnoughFood(String choice, int t);

	public abstract void msgOrderReady(String choice, int t);

	public abstract void msgCheckPlease(Customer customer);

	/**
	 * @param check the customer's total he has to pay
	 * @param t customer's table number
	 * 
	 * Sent by the cashier prompting the waiter to get the bill from the cashier.
	 */
	public abstract void msgHereIsCheck(double check, int t); //3

	/**
	 * @param c the customer
	 * 
	 * Sent by the customer notifying the waiter that he can clear the table.
	 */
	public abstract void msgDoneAndPaying(Customer c); //7	

	public abstract boolean isNotOnBreak();

	public abstract boolean isGoingOnBreak();

	public abstract void setBusy(boolean b);

	public abstract int numOfCust();

	public abstract String getName();
	
	public abstract boolean isAtWork();


}
