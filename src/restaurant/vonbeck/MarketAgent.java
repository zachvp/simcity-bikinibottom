package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;
import agent.Agent;


public class MarketAgent extends Agent implements Market {
	private class FoodReq {
		String food = "";
		int n = 0;
		
		FoodReq(String food, int n){
			this.food = food;
			this.n = n;
		}
	}
	
	private List<Order> urgentOs = Collections.synchronizedList(new ArrayList<Order>());
	private List<FoodReq> orders = Collections.synchronizedList(new ArrayList<FoodReq>());
	private Map<String, Integer> inventory = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> priceList = Collections.synchronizedMap(new HashMap<String, Integer>());
	private CookAgent cook;
	private Cashier cashier;
	private static final int INITIAL_STOCK = 2;
	
	
	MarketAgent(CookAgent cook, Cashier cashier) {
		this.cook = cook;
		this.cashier = cashier;
		inventory.put("Steak", INITIAL_STOCK);
		inventory.put("Chicken", INITIAL_STOCK);
		inventory.put("Salad", INITIAL_STOCK);
		inventory.put("Pizza", INITIAL_STOCK);
		priceList.put("Steak", 1400);
		priceList.put("Chicken", 850);
		priceList.put("Salad", 480);
		priceList.put("Pizza", 780);
		startThread();
	}
	
	//Messages
	
	/* (non-Javadoc)
	 * @see restaurant.Market#msgOrder(restaurant.interfaces.Waiter.Order)
	 */
	@Override
	public void msgOrder(Order o) {
		synchronized (urgentOs) {
			urgentOs.add(o);
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#msgOrder(java.lang.String, int)
	 */
	@Override
	public void msgOrder(String food, int n) {
		synchronized (orders) {
			orders.add(new FoodReq(food, n));
		}
		stateChanged();
	}

	//Scheduler
	/* (non-Javadoc)
	 * @see restaurant.Market#pickAndExecuteAnAction()
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized (urgentOs) {
			if (!urgentOs.isEmpty()) {
				check(urgentOs.get(0));
				urgentOs.remove(0);
				return true;
			}
		}
		
		synchronized (orders) {
			if (!orders.isEmpty()) {
				check(orders.get(0));
				orders.remove(0);
				return true;
			}
		}
		return false;
	}

	//Actions

	private void check(Order o) {
		synchronized (inventory) {
			if (inventory.get(o.food) <= 0) {
				cook.msgOutOfThis(this, o);
			} else {
				inventory.put(o.food, inventory.get(o.food) - 1);
				cook.msgOrderReady(this, o);
				cashier.msgBillFromMarket(priceList.get(o.food), this);
			}
		}
	}

	private void check(FoodReq foodReq) {
		synchronized (inventory) {
			int availableFood = inventory.get(foodReq.food);
			if (foodReq.n <= availableFood) {
				inventory.put(foodReq.food, inventory.get(foodReq.food)
						- foodReq.n);
				cook.msgOrderReady(this, foodReq.food, foodReq.n);
				cashier.msgBillFromMarket(
						foodReq.n * priceList.get(foodReq.food), this);
			} else if (foodReq.n > availableFood) {
				inventory.put(foodReq.food, 0);
				cook.msgOrderReady(this, foodReq.food, availableFood);
				if (availableFood > 0) cashier.msgBillFromMarket(
						availableFood * priceList.get(foodReq.food), this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Market#msgPay(int)
	 */
	@Override
	public void msgPay(int price) {
		//Get dollaaa	
	}
		

}
