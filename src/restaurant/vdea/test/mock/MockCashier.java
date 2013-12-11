package restaurant.vdea.test.mock;

import restaurant.vdea.interfaces.*;

public class MockCashier extends Mock implements Cashier {
	
	public EventLog log = new EventLog();

	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgComputeBill(Waiter w, Customer c, String choice, int table) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgPayment(Customer c, double check, double cash) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void msgMarketBill(Market m, double bill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsYourTotal(double total,
			market.interfaces.Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

}
