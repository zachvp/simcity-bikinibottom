package restaurant.test.mock;


import restaurant.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public Waiter waiter;
	public EventLog log = new EventLog();

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(int table, Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseReorder(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}
	
	public void msgHereIsYourCheck(double total) {
		log.add(new LoggedEvent("Received msgHereIsCheck from waiter. Total = "+ total));
		System.out.printf(getName() + ": Recieved $%.2f bill from waiter.%n", total);
	}

	public void msgHereIsYourChange(double change) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
		System.out.printf(getName() + ": Recieved $%.2f in change from cashier.%n", change);
	}

	public void msgYouHaveDebt(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
		System.out.printf(getName() + ": Recieved $%.2f of debt from cashier.%n", remaining_cost);
	}

	@Override
	public String getChoice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPosY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPosX() {
		// TODO Auto-generated method stub
		return 0;
	}

}


/*else if (this.getName().toLowerCase().contains("rich")){
//test the non-normative scenario where the customer overpays if their name contains the string "rich"
cashier.HereIsMyPayment(this, Math.ceil(total));

}else{
//test the normative scenario
cash = 30;	//has enough money

}*/
//cashier.msgPayment(this, total, cash);
//waiter.msgDoneAndPaying(this);
