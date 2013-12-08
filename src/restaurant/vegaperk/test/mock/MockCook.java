package restaurant.vegaperk.test.mock;


import java.util.Map;

import restaurant.WaiterAgent.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCook extends Mock implements Cook {
	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCook(String name) {
		super(name);
	}


	@Override
	public void msgHereIsOrder(Waiter w, String c, int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotDeliver(Map<String, Integer> cannotDeliver) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgCanDeliver(Map<String, Integer> canDeliver) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsDelivery() {
		// TODO Auto-generated method stub
		
	}
}
