package restaurant.lucas;

import java.util.ArrayList;
import java.util.List;

import org.omg.PortableServer.POAManagerPackage.State;

import restaurant.lucas.OrderWheel.Order.state;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;

public class OrderWheel {
	
	
	
	public static class Order
	{
		Order(String choice, int table, Customer customer, Waiter w, state s) {
			Choice = choice;
			this.table = table;
			cust = customer;
			waiter = w;
			orderState = s;
		}
		Customer cust;
		int table;
		public String Choice;
		Waiter waiter;
		public enum state {none, received, cooking, cooked, givenToWaiter};
		private state orderState;
		
	};
	
	List<Order> orderList = new ArrayList<Order>();
	
	public OrderWheel() {
		
	}
	
	public void addOrder(String choice, int tableNum, Customer c, Waiter w){
		orderList.add(new Order(choice, tableNum, c, w, state.received));
		
	}
	
	public Order getTopOrder() {
		return orderList.get(0);
	}
	
}