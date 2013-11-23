package restaurant.strottma.interfaces;

import restaurant.strottma.WaiterRole.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {	
	/**
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 * 
	 * @param cashier customer should use this pointer to pay the cashier.
	 * @param bill cost of the customer's meal.
	 */
	public void msgHereIsBill(Cashier cashier, double bill);
	
	/**
	 * Sent by the cashier to end the transaction between him and the customer.
	 * Will be >= 0.00, even if the customer still owes the restaurant.
	 * 
	 * @param change total change (if any) due to the customer overpaying
	 */
	public void msgHereIsChange(double change);
	
	/**
	 * @return the customer's name
	 */
	public String getName();

	// TODO: right now, menu is an inner class of WaiterRole. 
	public void msgFollowMeToTable(Waiter waiter, Menu menu, int x, int y);

	public void msgWhatWouldYouLike();

	public void msgHereIsYourFood();

	public void msgOutOfChoice(Menu menu);

	public Menu getMenu();

	public void msgNoRoom();
}