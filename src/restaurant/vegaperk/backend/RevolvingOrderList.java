package restaurant.vegaperk.backend;
import java.util.ArrayList;
import java.util.List;

import restaurant.vegaperk.interfaces.Waiter;

public class RevolvingOrderList {
	List<Order> orderList;
	
	public RevolvingOrderList() {
		this.orderList = new ArrayList<Order>();
	}
	
	/* --- Order Data --- */
	public enum OrderState {
		NEED_TO_COOK,
		PENDING,
		COOKING,
		COOKED,
		FINISHED,
		OUT_OF_CHOICE
	};
	
	public class Order {
		Waiter waiter;
		OrderState state;
		String choice;
		int table;
		
		public Order(String c, int t, Waiter w, OrderState s){
			choice = c;
			table = t;
			waiter = w;
			state = s;
		}
		
		public boolean shouldMessage() { return waiter instanceof WaiterRole; }
		
		public Waiter getWaiter() { return waiter; }
		public OrderState getOrderState() { return state; }
		public String getChoice() { return choice; }
		public int getTable() { return table; }
	}

	public void addOrder(String c, int t, Waiter w, OrderState pending) {
		orderList.add(new Order(c, t, w, pending));
	}
	
	public Order getNewOrder(String c, int t, Waiter w, OrderState pending) {
		return new Order(c, t, w, pending);
	}

	public boolean isEmpty() { return orderList.isEmpty(); }
	
	public int getSize() { return orderList.size(); }
}
