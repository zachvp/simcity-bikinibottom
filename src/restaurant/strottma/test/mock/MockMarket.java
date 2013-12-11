package restaurant.strottma.test.mock;

import java.util.Map;

import mock.Mock;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Market;

public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public double money;
	public EventLog log;
	
	public MockMarket(String name) {
		super(name);
		money = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgLowOnFood(Map<String, Integer> cannotDeliver, int marketNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		money += payment;
	}

}
