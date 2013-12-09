package restaurant.anthony.test.mock;


import java.util.List;

import restaurant.anthony.CookRole;
import restaurant.anthony.Food;
import restaurant.anthony.HostRole;
import restaurant.anthony.CashierRole.Check;
import restaurant.anthony.WaiterRole.Menu;
import restaurant.anthony.WaiterRole.Order;
import restaurant.anthony.gui.CustomerGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Market;
import restaurant.anthony.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	public EventLog log = new EventLog();
	
	public Cashier cashier;
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public String getMaitreDName(){
		return null;}

	public String getName(){
		return null;}

	// Messages
	public void BuyFood(List<Food> shoppingList2, CookRole co){}

	public void HeresTheMoney(double payment) {
		log.add(new LoggedEvent("I Got the money from cashier " + payment));
	}

	public void setCashier(Cashier ca) {
		cashier = ca;
	}

}
