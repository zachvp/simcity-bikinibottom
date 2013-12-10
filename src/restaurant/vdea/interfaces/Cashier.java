package restaurant.vdea.interfaces;

/**
 * Cashier interface built to unit test a CashierAgent.
 *
 * @author Victoria Dea
 *
 */
public interface Cashier {
	
	/**
	 * @param w the waiter
	 * @param c the customer
	 * @param choice the customer's food
	 * @param table the customer's table
	 * 
	 * Sent by the waiter prompting the cashier to calculate the bill.
	 */
	public abstract void msgComputeBill(Waiter w, Customer c, String choice, int table); //2
	
	/**
	 * @param c the customer
	 * @param check total amount customer has to pay
	 * @param cash amount customer is paying with
	 * 
	 * Sent by the customer to pay his bill.
	 */
	public abstract void msgPayment(Customer c, double check, double cash); //5
	
	/**
	 * @param m the Market
	 * @param bill the bill the cashier has to pay to the market
	 * 
	 * Sent by the market to give cashier the bill.
	 */
	public void msgMarketBill(Market m, double bill);
}
