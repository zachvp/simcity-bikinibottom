package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.vonbeck.gui.FoodGui;
import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;
import agent.Agent;


public class CookAgent extends Agent {
	private static final long COOK_DELAY_MS = 5000;
	private List<Order> orders 
		= Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, Integer> inventory 
		= Collections.synchronizedMap(new HashMap<String, Integer>());
	private static final int INITIAL_STOCK = 5;
	private static final int STOCK_THRESHOLD = 5;
	private static final int STOCK_MAINTAIN = 10;
	private List<MarketAgent> markets 
		= Collections.synchronizedList(new ArrayList<MarketAgent>());
	private List<CookAction> actionQueue
		= Collections.synchronizedList(new ArrayList<CookAction>());	
	private FoodGui foodGui;
	
	private abstract class CookAction {
		Market market;
		Order o;
		String food;
		int num;
		
		public abstract void run();
	}
	
	@SuppressWarnings("unused")
	public CookAgent(Cashier cashier, RestaurantGui gui) {
		for (int i = 0; i < 3; i++)
			markets.add(new MarketAgent(this, cashier));
		
		inventory.put("Steak", INITIAL_STOCK);
		inventory.put("Chicken", INITIAL_STOCK);
		inventory.put("Salad", INITIAL_STOCK);
		inventory.put("Pizza", INITIAL_STOCK);
		
		
		
		for(String food : inventory.keySet()) {
			if (INITIAL_STOCK < STOCK_THRESHOLD) {
				int numLeftToOrder = STOCK_MAINTAIN - INITIAL_STOCK;
				
				Do("Ordering " + numLeftToOrder + " " + 
						food + " to market " + 0 +"!");
				markets.get(0).msgOrder(food, numLeftToOrder);
			}
		}
		
		foodGui = new FoodGui(gui);
		
		startThread();
	}
	
	//Messages
	
	void msgCook(Order o) {
		synchronized (orders) {
			orders.add(o);
		}
		stateChanged();
	}

	public void msgOutOfThis(Market market, Order o) {
		CookAction action = new CookAction() {
			public void run () {
				int nextMarket = 0;

				synchronized (markets) {
					for (int i = 0; i < markets.size(); i++) {
						if (markets.get(i) == market) {
							nextMarket = i + 1;
							break;
						}
					}
				}
				Do("Received " + 0 + " " + o.food + 
						" from market " + (nextMarket - 1));

				synchronized (markets) {
					if (nextMarket < markets.size()) {
						Do("Ordering " + 1 + " " + o.food + " to market "
								+ nextMarket + "!");
						markets.get(nextMarket).msgOrder(o);
					} else {
						o.waiter.msgOutOfThis(o);
					}
				}
			}
		};
		action.o = o;
		action.market = market;
		actionQueue.add(action);
		stateChanged();
	}

	public void msgOrderReady(Market market, Order o) {
		CookAction action = new CookAction() {
			public void run () {

				int nextMarket = 0;

				synchronized (markets) {
					for (int i = 0; i < markets.size(); i++) {
						if (markets.get(i) == market) {
							nextMarket = i + 1;
							break;
						}
					}
				}
				Do("Received " + 1 + " " + o.food + " from market "
						+ (nextMarket - 1));
				synchronized (inventory) {
					inventory.put(o.food, inventory.get(o.food) + 1);
				}
				msgCook(o);

			}
		};
		action.o = o;
		action.market = market;
		actionQueue.add(action);
		stateChanged();
	}

	public void msgOrderReady(Market market, String food, int num) {
		CookAction action = new CookAction() {
			public void run () {
				synchronized (inventory) {
					inventory.put(food, inventory.get(food) + num);
				}
				int nextMarket = 0;

				synchronized (markets) {
					for (int i = 0; i < markets.size(); i++) {
						if (markets.get(i) == market) {
							nextMarket = i + 1;
							break;
						}
					}
				}
				Do("Received " + num + " " + 
						food + " from market " + (nextMarket - 1));

				int numLeftToOrder = STOCK_MAINTAIN - inventory.get(food);

				synchronized (markets) {
					if (numLeftToOrder > 0) {
						if (nextMarket < markets.size()) {
							Do("Ordering " + numLeftToOrder + " " + food
									+ " to market " + nextMarket + "!");
							markets.get(nextMarket).msgOrder(food,
									numLeftToOrder);
						}
					}
				}
			}
		};
		action.food = food;
		action.market = market;
		action.num = num;
		actionQueue.add(action);
		stateChanged();
	}

	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {

		if (!actionQueue.isEmpty()) {
			CookAction currAction;
			synchronized (actionQueue) {
				currAction = actionQueue.get(0);
				actionQueue.remove(0);
			}
			currAction.run();
			return true;
		}



		if (!orders.isEmpty()) {
			Order currOrder;
			synchronized (orders) {
				currOrder = orders.get(0);
				orders.remove(0);
			}
			cook(currOrder);

			return true;
		}

		return false;
	}
	
	//Actions

	private void cook(Order o) {
		synchronized (markets) {
			synchronized (inventory) {
				if (inventory.get(o.food) <= 0) {
					//o.waiter.msgOutOfThis(o);
					Do("Ordering 1 " + o.food + " to market 0!");
					markets.get(0).msgOrder(o);
				} else {
					inventory.put(o.food, inventory.get(o.food) - 1);
					if (inventory.get(o.food) <= STOCK_THRESHOLD) {
						int numToOrder= STOCK_MAINTAIN-inventory.get(o.food);
						Do("Ordering " + numToOrder + " " + o.food
								+ " to market 0!");
						markets.get(0).msgOrder(o.food, numToOrder);
					}
					
					foodGui.toggleCooking();
					
					synchronized (this) {
						try {
							wait(COOK_DELAY_MS);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					foodGui.toggleCooking();
					foodGui.addPlate();
					o.waiter.msgOrderReady(o);
					
					//Move food from grill to plating=
				}
			}
		}
	}

	public void removePlate() {
		foodGui.removePlate();
	}
	
	
}
