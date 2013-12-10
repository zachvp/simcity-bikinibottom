package restaurant.vegaperk.backend;
import java.util.ArrayList;
import java.util.List;

import restaurant.vegaperk.interfaces.Waiter;

public class RevolvingOrderList {
	List<Order> orderList;
	
	RevolvingOrderList() {
		this.orderList = new ArrayList<Order>();
	}
	
	
	public enum OrderState { PENDING,
		COOKING,
		COOKED,
		FINISHED
	};
	
	public class Order{
		Waiter waiter;
		OrderState state;
		String choice;
		int table;
		
		Order(String c, int t, Waiter w, OrderState s){
			choice = c;
			table = t;
			waiter = w;
			state = s;
		}
	}

}
