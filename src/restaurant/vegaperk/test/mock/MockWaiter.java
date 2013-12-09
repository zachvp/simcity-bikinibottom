package restaurant.vegaperk.test.mock;


import mock.EventLog;
import mock.Mock;
import restaurant.vegaperk.backend.CashierRole;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Customer;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {
	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockWaiter(String name) {
		super(name);

	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCustomerLeavingTable(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotPay(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmDoneEating(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Customer c, double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderDone(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseSeatCustomer(Customer customer, int tableID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDenyBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCustomerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgHomePosition(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void setCashier(CashierRole cashier) {
		// TODO Auto-generated method stub
		
	}

}
