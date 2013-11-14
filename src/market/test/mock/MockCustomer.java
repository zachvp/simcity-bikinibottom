package market.test.mock;


import java.util.List;

import market.CashierAgent;
import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;

public class MockCustomer extends Mock implements Customer {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgHereisYourTotal(double cost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereisYourItem(List<Item> Items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	

}
