package restaurant.vdea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.vdea.interfaces.Waiter;

/**
 * RevolvingOrderList acts as both an order factory (for the cook) and a
 * single container for orders accessed by the new waiter and cook. 
 * @author Zach VP
 *
 */
public class RevolvingOrderList {
	List<Order> orderList;
	
	public RevolvingOrderList() {
		this.orderList = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	// TODO move order data from cook into here
	/* --- Order Data --- */
	public enum OrderState {
		pending, enoughFood, notEnoughFood, cooking, 
		cooked, done, none, shipmentIncomplete
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
	}

	// TODO accessed by PC waiter to submit a new order
	public void addOrder(String c, int t, Waiter w, OrderState pending) {
		orderList.add(new Order(c, t, w, pending));
	}
	
	// TODO accessed by the cook to create a new order in his own list (like the old way)
	public Order getNewOrder(String c, int t, Waiter w, OrderState pending) {
		return new Order(c, t, w, pending);
	}
}
