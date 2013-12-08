package restaurant.vonbeck.interfaces;

import restaurant.vonbeck.interfaces.Waiter.Order;


public interface Market {

	public abstract void msgOrder(Order o);

	public abstract void msgOrder(String food, int n);

	//Scheduler
	public abstract boolean pickAndExecuteAnAction();

	public abstract void msgPay(int price);

}