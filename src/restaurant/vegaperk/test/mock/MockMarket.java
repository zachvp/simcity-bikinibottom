package restaurant.vegaperk.test.mock;


import java.util.Map;

import mock.EventLog;
import mock.Mock;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {
	public EventLog log = new EventLog();
	public Cashier cashier;
	public double money = 0; 
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */

	public MockMarket(String name) {
		super(name);
	}
	@Override
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		money += payment;
	}
	@Override
	public void msgNeedFood(Map<String, Integer> groceries) {
		// TODO Auto-generated method stub
		
	}
}
