package restaurant.vegaperk.interfaces;

import restaurant.vegaperk.backend.WaiterRole.Menu;
import restaurant.vegaperk.interfaces.Cashier;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	/** From the Cashier */
	public void msgHereIsCheck(double check, Cashier cash);
	public void msgHereIsChange(double change);
	
	/** From WaiterAgent */
	public void msgSitAtTable(Waiter w, Menu m, int x, int y);
	public void msgWhatWouldYouLike();
	public void msgHereIsYourFood();
	public void msgOutOfChoice(String c);
	public void msgTablesAreFull();
	public Object getName();
}