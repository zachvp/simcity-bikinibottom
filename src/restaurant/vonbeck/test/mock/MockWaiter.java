package restaurant.vonbeck.test.mock;

import restaurant.vonbeck.CashierRole;
import restaurant.vonbeck.CustomerRole;
import restaurant.vonbeck.Table;
import restaurant.vonbeck.gui.WaiterGui;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public CashierRole cashier;

	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHeWantsFood(CustomerRole c, Table t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToOrder(CustomerRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderReady(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToPay(CustomerRole c, String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBill(int price, Customer c) {
		c.msgBill(price, cashier);
		
	}

	@Override
	public void msgImLeaving(CustomerRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfThis(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanGoOnBreak(boolean answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGUIGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int customerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BreakState getBreakState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBreakState(BreakState breakState) {
		// TODO Auto-generated method stub
		
	}

}
