package restaurant.vonbeck.interfaces;

import restaurant.vonbeck.CustomerAgent;
import restaurant.vonbeck.Table;
import restaurant.vonbeck.gui.WaiterGui;

public interface Waiter {

	public enum BreakState 
	{Working, WantBreak, OnBreak};
	

	public class Order {
		public String food;
		public Customer customer;
		public Waiter waiter;
		
		public Order(String f, Customer c, Waiter w) {
			food = f;
			customer = c;
			waiter = w;
		}
	}
	
	//Messages
	
	public void msgHeWantsFood(CustomerAgent c, Table t);

	public void msgIWantToOrder(CustomerAgent c);

	public void msgOrderReady(Order o);

	public void msgReadyToPay(CustomerAgent c, String food);

	public void msgBill(int price, Customer c);

	public void msgImLeaving(CustomerAgent c);

	public void msgOutOfThis(Order o);

	public void msgCanGoOnBreak(boolean answer);

	public void msgGUIGoOnBreak();
	
	public void msgAnimationDone();

	
	//Getters / setters
	
	public int customerCount();

	public String getName();
	
	public WaiterGui getGui();

	public BreakState getBreakState();

	public void setBreakState(BreakState breakState);

	
}
