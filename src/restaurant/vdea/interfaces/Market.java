package restaurant.vdea.interfaces;

import java.util.List;

import restaurant.vdea.Food;


/**
 * Market interface built to unit test a MarketAgent.
 *
 * @author Victoria Dea
 *
 */
public interface Market {
	
	/**
	 * @param c the cook
	 * @param food the list of food the cook needs
	 * @param cash the cashier to send the bill to
	 * 
	 * Sent by the cook to restock inventory
	 */
	public abstract void msgOrderRequest(Cook c, List<Food> food, Cashier cash);

	/**
	 * @param check the amount to pay the bill
	 * 
	 * Sent by the cashier to pay the bill.
	 */
	public abstract void msgBillPayment(double check, Cashier c);
	
	/**
	 * 
	 * @return the name of the Market
	 */
	public abstract String getName();

	public abstract void msgNotEnough(double check, Cashier cash);
}
