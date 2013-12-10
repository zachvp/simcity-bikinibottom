package restaurant.anthony.interfaces;

import restaurant.anthony.CashierRole.Check;

public interface Cashier extends market.interfaces.PhonePayer {

	public abstract String getMaitreDName();

	public abstract String getName();

	// Messages
	public abstract void ComputeBill(String choice, int table, Waiter waiter);

	public abstract void Payment(Check check, double cash, Customer cust);

	public abstract void PayDebt(double p);

	public abstract void msgAtHome();

	public abstract void msgAtExit();

}