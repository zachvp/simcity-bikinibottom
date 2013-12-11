package restaurant.strottma.test.mock;

import mock.Mock;
import restaurant.strottma.WaiterRole.Menu;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;

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

	public MockCustomer(String name) {
		super(name);

	}
	
	@Override
	public void msgHereIsBill(Cashier cashier, double bill) {
		log.add(new LoggedEvent("Received msgHereIsBill from cashier. Bill = " + bill));
		
		/*
		if (this.name.toLowerCase().contains("thief")) {
			// test the non-normative scenario where the customer has no money if their name contains the string "thief"
			// cashier.IAmShort(this, 0);

		} else if (this.name.toLowerCase().contains("rich")) {
			// test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.msgHereIsPayment(this, Math.ceil(bill));

		} else {
			// test the normative scenario
			cashier.msgHereIsPayment(this, bill);
		}
		*/
	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Received msgHereIsChange from cashier. Change = " + change));
	}

	@Override
	public void msgFollowMeToTable(Waiter waiter, Menu menu, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Menu getMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgNoRoom() {
		// TODO Auto-generated method stub
		
	}

}
