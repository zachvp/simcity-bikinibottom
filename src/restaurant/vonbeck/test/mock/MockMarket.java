package restaurant.vonbeck.test.mock;

import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;

public class MockMarket extends Mock implements Market {

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrder(String food, int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgPay(int price) {
		log.add(new LoggedEvent("Received msgPay from cashier. Total = "+ price));
	}

}
