package restaurant.strottma.interfaces;

public interface Cashier {
	public void msgHereIsPayment(Customer c, double payment);
	public void msgDoneEating(Customer c, Waiter waiterAgent, double price);
	// public void msgHereIsBill(double bill, Market market); // from market
}
