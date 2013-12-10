package restaurant.vdea.interfaces;

import restaurant.vdea.Menu;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public abstract void msgRestaurantFull();	
	
	public abstract void msgFollowMeToTable(int table, Menu menu);
	
	public abstract void msgWhatWouldYouLike();

	public abstract void msgPleaseReorder(String choice);

	public abstract void msgHereIsYourFood();
	
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the waiter prompting the customer's money after the customer has asked for the check.
	 */
	public abstract void msgHereIsYourCheck(double total); //4

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	public abstract void msgHereIsYourChange(double change); //6

	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	public abstract void msgYouHaveDebt(double remaining_cost); //6.5

	public abstract String getChoice();

	public abstract void setWaiter(Waiter waiter);
	
	public abstract String getName();

	public abstract int getPosY();

	public abstract int getPosX();

	

}