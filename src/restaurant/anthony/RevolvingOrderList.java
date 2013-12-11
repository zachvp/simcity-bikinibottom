package restaurant.anthony;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.anthony.interfaces.Waiter;

public class RevolvingOrderList {
	List<Order> orderList;
	
	public RevolvingOrderList() {
		this.orderList = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	/* --- Order Data --- */
	public enum OrderState {
		NEED_TO_COOK,
		PENDING,
		COOKING,
		COOKED,
		FINISHED,
		OUT_OF_CHOICE,
		PICKED_UP
	};
	
	public class Order {
		String choice;
		OrderState state;
		int table;
		int stove;
		restaurant.anthony.interfaces.Waiter Waiter;

		public Order(String c, int t, Waiter w){
			choice = c;
			table = t;
			Waiter = w;
			state = OrderState.NEED_TO_COOK;
		}
		
		public boolean shouldMessage() { return Waiter instanceof WaiterRole; }
		
		public Waiter getWaiter() { return Waiter; }
		public OrderState getOrderState() { return state; }
		public String getChoice() { return choice; }
		public int getTable() { return table; }
		public boolean IsProcessed() { return (state == OrderState.COOKED);}
		
	}
	
	

	public void addOrder(String c, int t, Waiter w, OrderState pending) {
		orderList.add(new Order(c, t, w));
	}
	
	public Order getNewOrder(String c, int t, Waiter w, OrderState pending) {
		return new Order(c, t, w);
	}

	public boolean isEmpty() { return orderList.isEmpty(); }
	
	public int getSize() { return orderList.size(); }
}
