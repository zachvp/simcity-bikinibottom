package restaurant.vegaperk.test.mock;


import mock.EventLog;
import mock.Mock;
import restaurant.vegaperk.backend.WaiterAgent.Menu;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Customer;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {
	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public double money = 2.00;
	public double bill = 0;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	/** Messages From Cashier */
	public void msgHereIsCheck(double check, Cashier cash){
		log.add("Received Check from Cashier " + check);
		System.out.println("Received check from Cashier");
		cashier = cash;
		bill = check;
	}
	public void msgHereIsChange(double change){
		log.add("Received Change from Cashier " + change);
		money += change;
	}

	@Override
	public void msgSitAtTable(Waiter w, Menu m, int x, int y) {
		log.add("Received sit at table");
	}

	@Override
	public void msgWhatWouldYouLike() {
		log.add("Receievd what would you like");
	}

	@Override
	public void msgHereIsYourFood() {
		log.add("Receieved what would you like");
	}

	@Override
	public void msgOutOfChoice(String c) {
		log.add("Receieved out of choice");
	}

	@Override
	public void msgTablesAreFull() {
		log.add("Receieved tables are full");
	}

	@Override
	public String getName() {
		return super.getName();
	}
}
