package market.test.mock;


import java.util.List;


import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;

public class MockCustomer extends Mock implements Customer {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier Cashier;
	public List<Item>ShoppingList;
	
	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgHereisYourTotal(double cost, List<Item> MissingItems) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the invoice " + cost));
		Cashier.msgHereIsPayment(cost, this);
	}

	@Override
	public void msgHereisYourItem(List<Item> Items) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received Items"));
	}

	@Override
	public void msgNoItem() {
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

	@Override
	public void setShoppingList(List<Item> SL) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goingToBuy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveMarket() {
		// TODO Auto-generated method stub
		
	}

	

	

}
