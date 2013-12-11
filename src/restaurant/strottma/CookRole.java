package restaurant.strottma;

import gui.Building;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import kelp.Kelp;
import kelp.KelpClass;
import market.Item;
import restaurant.strottma.HostRole.Table;
import restaurant.strottma.gui.CookGui;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Cook;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.SingletonTimer;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Cook Role
 * 
 * @author Erik Strottmann
 */
public class CookRole extends WorkRole implements Cook {

	// private CookGui cookGui = null;
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private Timer timer;
	private boolean offWork = false;
	
	private Map<String, Food> foods;
	private List<MyDelivery> deliveries;
	private List<GrillOrPlate> grills;
	private List<GrillOrPlate> plateAreas;
	
	public List<GrillOrPlate> getGrills() { return grills; }
	public List<GrillOrPlate> getPlateAreas() { return plateAreas; }
	
	private Semaphore multiStepAction = new Semaphore(0, true);
	
	private CookGui gui;
	private Cashier cashier;
	
	private Kelp kelp = KelpClass.getKelpInstance();
	
	public void setGui(CookGui cookGui) {
		this.gui = cookGui;
	}
	
	public CookRole(Person person, CityLocation location) {
		super(person, location);
		
		this.timer = SingletonTimer.getInstance();

		this.foods = new HashMap<String, Food>();
		this.deliveries = new ArrayList<MyDelivery>();
		this.grills = new ArrayList<GrillOrPlate>();
		this.plateAreas = new ArrayList<GrillOrPlate>();
		
		// new Food(name, quantity, capacity, low threshold, cooking time in milliseconds)
		Food st = new Food("Krabby Patty",	3, 5, 2, 7*1000);
		Food ck = new Food("Kelp Shake",	0, 5, 2, 5*1000);
		Food sa = new Food("Coral Bits",	1, 5, 2, 2*1000);
		Food pz = new Food("Kelp Rings",	3, 5, 2, 8*1000);
		
		// create grills
		for (int i = 0; i < 5; i++) {
			grills.add(new GrillOrPlate(850-400, 114 + 30*i));
		}
		
		// create plating areas
		for (int i = 0; i < 5; i++) {
			plateAreas.add(new GrillOrPlate(750-400, 114 + 30*i));
		}
		
		this.foods.put(st.name, st);
		this.foods.put(ck.name, ck);
		this.foods.put(sa.name, sa);
		this.foods.put(pz.name, pz);
	}
	
	@Override
	public void activate() {
		super.activate();
		offWork = false;
		DoGoHome();
	}
	
	public Cashier getCashier() {
		return cashier;
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
	
	// Messages
	void msgHereIsAnOrder(Waiter w, Customer c, Table t,
			String choice) {
		
		Do(AlertTag.RESTAURANT, "Received an order for " + choice +
				" from " + c);

		orders.add( new Order(w, c, t, choice, OState.RECEIVED) );

		stateChanged();
	}
	
	void msgOrderDoneCooking(int orderNum) { // from TimerTask
		Do(AlertTag.RESTAURANT, "Order " + orderNum + " done cooking");

		Order o = orders.get(orderNum);
		o.state = OState.COOKED;

		stateChanged();
	}
	
	// from old market
//	public void msgCannotDeliver(Map<String, Integer> cannotDeliver, int marketNumber) {
//		if (cannotDeliver.size() != 0) {
//			Do("Market " + marketNumber + " can't complete delivery.");
//			if (marketNumber+1 < markets.size()) {
//				Do("Requesting delivery from next market.");
//				markets.get(marketNumber+1).msgLowOnFood(cannotDeliver, marketNumber+1);
//			}
//		}
//	}
	
	// from old market
//	public void msgHereIsDelivery(Map<String, Integer> delivery) {
//		synchronized (delivery) {
//			for (Map.Entry<String, Integer> entry : delivery.entrySet()) {
//				Food f = foods.get(entry.getKey());
//				if (f != null && entry.getValue() != 0) {
//					Do("Received delivery of " + entry.getValue() + " " + f.name);
//					f.quantity += entry.getValue();
//				}
//			}
//		}
//	}
		
	@Override
	// from market delivery guy
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (1/7) */
	public void msgHereIsYourItems(List<Item> DeliverList) {
		for (Item item : DeliverList) {
			Food f = foods.get(item.name);
			if (f != null && item.amount > 0) {
				Do(AlertTag.RESTAURANT, "Received delivery of " + item.amount
						+ " " + item.name);
				f.quantity += item.amount;
			}
		}
	}
	
	@Override
	// from market cashier
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (2/7) */
	public void msgHereIsMissingItems(List<Item> MissingItemList, int orderNum) {
		if (!MissingItemList.isEmpty()) {
			Do(AlertTag.RESTAURANT, "Market couldn't complete order");
			deliveries.get(orderNum).itemsToReorder.addAll(MissingItemList);
			stateChanged();
		}
	}
	
	public void msgAtDestination() { // from gui
		multiStepAction.release();
	}
	
	@Override
	public void msgLeaveWork() {
		offWork = true;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (3/7) */
		synchronized (foods) {
			for (Food f : foods.values()) {
				if (f.futureQuantity <= f.lowThreshold) {
					restockFood();
					return true;
				}
			}
		}
		
		/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (4/7) */
		synchronized (deliveries) {
			for (int i = 0; i < deliveries.size(); i++) {
				MyDelivery delivery = deliveries.get(i);
				if (delivery.state == DeliveryState.NEED_TO_REORDER) {
					retryDelivery(delivery, i);
					return true;
				}
			}
		}
		
		synchronized (orders) {
			for (Order o : orders) {
				if (o.state == OState.COOKED) {
					plateIt(o);
					o.state = OState.PLATED;
					return true;
				}
			}
		}
		
		synchronized (orders) {
			for (int i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.state == OState.RECEIVED) {
					if (tryToCookIt(o, i)) {
						o.state = OState.COOKING;
					} else {
						o.state = OState.FAILED;
					}
					return true;
				}
			}
		}
		
		DoGoHome(); // return to default position if nothing to do
		
		if (offWork) { gui.DoLeaveWork(); deactivate(); }
		
		// We have tried all our rules and found nothing to do. So return false
		// to main loop of abstract Role and wait.
		return false;
	}

	// Actions
	boolean tryToCookIt(Order o, final int orderNum) {
		if (o.food.quantity <= 0) {
			Do(AlertTag.RESTAURANT, "Can't cook order for " +
					o.customer.getName() + " - out of " + o.food.name);
			o.waiter.msgOutOfChoice(o.customer, o.table, o.getChoice());
			return false;
		}
		
		Do(AlertTag.RESTAURANT, "Cooking " + o.food.name + " for " +
				o.customer.getName());
		
		if (!o.ingredientsReady) {
			Do(AlertTag.RESTAURANT, "Getting ingredients for order");
			DoGoToFridge();
			
			acquire(multiStepAction);
			
			Do(AlertTag.RESTAURANT, "At fridge.");
			for (Order other : orders) {
				other.ingredientsReady = true;
			}
		}
		
		o.food.decrementQuantity();
		
		GrillOrPlate grill = null;
		for (GrillOrPlate g : grills) {
			if (g.getOrder() == null) {
				g.setOrder(o);
				grill = g;
				break;
			}
		} // we have more grills than tables, so this will always break early
		
		DoStartCooking(grill, o.getChoice()); // animation - disabled (for now?)
		// wait until the animation is complete
		acquire(multiStepAction);
		grill.showOrder();
		
		timer.schedule( new TimerTask() {
			public void run() {
				msgOrderDoneCooking(orderNum);
			}
		}, foods.get(o.getChoice()).cookingTime);
		
		DoGoHome(); // animation - return to the default location
		
		return true;
	}
	
	void plateIt(Order o) {
		Do(AlertTag.RESTAURANT, "Plating order for " + o.customer);
		
		GrillOrPlate grill = null;
		// remove the order from the grill
		for (GrillOrPlate g : grills) {
			if (g.getOrder() == o) {
				grill = g;
				break;
			}
		}
		
		DoPickUpOrder(grill); // animation
		// wait until the cook is at the grill
		acquire(multiStepAction);
		grill.hideOrder();
		grill.setOrder(null);
		
		GrillOrPlate plateArea = null;
		for (GrillOrPlate p : plateAreas) {
			if (p.getOrder() == null) {
				p.setOrder(o);
				plateArea = p;
				break;
			}
		} // we have more grills than tables, so this will always break early
		
		DoPlateIt(plateArea, o.getChoice()); // animation - disabled (for now?)
		// wait until the cook is at the plating area
		acquire(multiStepAction);
		plateArea.showOrder();
		
	    o.waiter.msgOrderIsReady(o.customer, o.table, o.getChoice(), plateArea);
	    o.state = OState.COMPLETED;
	    
		DoGoHome(); // animation - return to the default location
	}
	
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (5/7) */
	void restockFood() {
		// Find all the items we still need to order.
		Set<Item> itemsToOrder = new HashSet<Item>();
		for (Map.Entry<String, Food> entry : foods.entrySet()) {
			Food f = entry.getValue();
			if (f.futureQuantity <= f.lowThreshold) {
				Do(AlertTag.RESTAURANT, "Out of " + f.name);
				itemsToOrder.add(new Item(f.name,
						f.capacity - f.futureQuantity));
				f.futureQuantity = f.capacity;
			}
		}
		
		if (itemsToOrder.isEmpty()) {
			return;
		}
				
		// Find a market to order from
		Building market = null;
		Building restaurant = (Building) getLocation();
		
		List<CityLocation> openMarkets = kelp.placesNearMe(getLocation(),
				LocationTypeEnum.Market);
		if (openMarkets != null && !openMarkets.isEmpty()) {
			market = (Building) openMarkets.get(0);
		}
		
		// Request the delivery
		MyDelivery delivery = new MyDelivery(itemsToOrder);
		delivery.markets = new ArrayList<CityLocation>();
		int orderNum = 0;
		
		market.interfaces.Cashier marketCashier =
				(market.interfaces.Cashier) market.getGreeter();
		
		Do(AlertTag.RESTAURANT, "Placing an order with market " + market);
		marketCashier.msgPhoneOrder(new ArrayList<Item>(delivery.items),
				this.cashier, this, restaurant, orderNum);
		delivery.markets.add(market);
	}
	
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (6/7) */
	private void retryDelivery(MyDelivery delivery, final int orderNum) {
		// Find a market that hasn't been tried yet.
		List<CityLocation> openMarkets = kelp.placesNearMe(getLocation(),
				LocationTypeEnum.Market);
		Building market = null;
		for (CityLocation m : openMarkets) {
			if (!delivery.markets.contains(m)) {
				market = (Building) m;
				break;
			}
		}
		if (market == null) {
			// TODO BIG IMPORTANT I CHANGED HOW THIS WORKS --------------------
			// No market can complete our order! 
			TimerTask task = new TimerTask() {
				/**
				 * Decrement the "future quantity", but after a delay so we
				 * don't keep retrying the same order every few milliseconds.
				 */
				@Override
				public void run() {
					Do(AlertTag.RESTAURANT,
							"No market can complete this order.");
					
					MyDelivery d = deliveries.get(orderNum);
					for (Item i : d.itemsToReorder) {
						foods.get(i.name).futureQuantity -= i.amount;
					}
					d.state = DeliveryState.COMPLETE;
				}
			};
			timer.schedule(task, 2 * Constants.MINUTE);
			return;
		}
		
		// Request the delivery from the new market
		delivery.markets.add(market);
		
		Building restaurant = (Building) getLocation();
		market.interfaces.Cashier marketCashier =
				(market.interfaces.Cashier) market.getGreeter();
		
		Do(AlertTag.RESTAURANT, "Placing an order with market " + market);
		marketCashier.msgPhoneOrder(
				new ArrayList<Item>(delivery.itemsToReorder),
				this.cashier, this, restaurant, orderNum);
		/* TODO IMPORTANT, I CHANGED THIS - ONLY REORDER itemsToReorder, NOT
		 * ALL ITEMS */
	}
	
//	void restockFoodsssss() {
//		
//		// TODO old - delete
//		
//		// First, look for items we need now.
//		Set<Item> request = new HashSet<Item>();
//		synchronized (foods) {
//			for (Map.Entry<String, Food> entry : foods.entrySet()) {
//				Food f = entry.getValue();
//				if (f.futureQuantity <= f.lowThreshold) {
//					Do("Out of " + f.name);
//					request.add(new Item(entry.getKey(),
//							f.capacity - f.futureQuantity));
//					f.futureQuantity = f.capacity;
//				}
//			}
//		}
//		
//		MyDelivery delivery = new MyDelivery();
//		CityLocation marketLoc = null;
//		int orderNum = 0;
//		
//		if (!request.isEmpty()) {
//			// if there are any new items to request
//			delivery.items = request;
//			delivery.markets = new ArrayList<CityLocation>();
//			List<CityLocation> availableMarkets =
//					kelp.placesNearMe(getLocation(), LocationTypeEnum.Market);
//			if (availableMarkets != null) {
//				marketLoc = availableMarkets.get(0);
//				orderNum = deliveries.size();
//			}
//			
//			
//		} else {
//			// request an old order at another market, if applicable
//
//			for (int i = 0; i < deliveries.size(); i++) {
//			// for (MyDelivery oldDelivery : deliveries) {
//				MyDelivery oldDelivery = deliveries.get(i);
//				if (oldDelivery.state == DeliveryState.NEED_TO_REORDER) {
//					// found a delivery to reorder! try to find another market
//					List<CityLocation> nearbyMarket =
//							kelp.placesNearMe(getLocation(),
//									LocationTypeEnum.Market);
//					for (CityLocation nearestMarket : nearbyMarket) {
//						if (!oldDelivery.markets.contains(nearestMarket)) {
//							// found a market!
//							delivery = oldDelivery;
//							orderNum = i;
//							break;
//						}	
//					}
//					Do(AlertTag.RESTAURANT,
//							"No market can fulfill this delivery.");
//					
//					// Reduce futureQuantity of undeliverable items
//					for (Item item : oldDelivery.items) {
//						if (foods.containsKey(item.name)) {
//							Food f = foods.get(item.name);
//							f.futureQuantity -= item.amount;
//						}
//					}
//				}
//			}
//		}
//		
//		// Place an order if we need a delivery and we found a market 
//		if (!delivery.items.isEmpty() && marketLoc != null) {
//			Building market = (Building) marketLoc;
//			Building restaurant = (Building) getLocation();
//			
//			market.interfaces.Cashier marketCashier =
//					(market.interfaces.Cashier) market.getGreeter();
//			restaurant.strottma.interfaces.Cashier restaurantCashier =
//					(restaurant.strottma.interfaces.Cashier)
//							restaurant.getGreeter();
//			
//			marketCashier.msgPhoneOrder(new ArrayList<Item>(delivery.items),
//					restaurantCashier, this, restaurant, orderNum);
//			delivery.state = DeliveryState.PLACED;
//			delivery.markets.add(market);
//			
//			Do(AlertTag.RESTAURANT, "Placed an order with market " + market);
//			
//		} else {
//			delivery.state = DeliveryState.COMPLETE;
//			
//		}
//	}

	// The animation DoXYZ() routines
	private void DoGoHome() {
		gui.DoGoHome();
	}
	
	private void DoGoToFridge() {
		gui.DoGoToFridge();
	}
	
	private void DoStartCooking(GrillOrPlate g, String orderText) {
		gui.DoCook(g.getX(), g.getY(), orderText.substring(0, 2));
	}
	
	private void DoPickUpOrder(GrillOrPlate g) {
		gui.DoCook(g.getX(), g.getY(), null);
	}
	
	private void DoPlateIt(GrillOrPlate p, String orderText) {
		gui.DoPlate(p.getX(), p.getY(), orderText.substring(0, 2));
	}
	

	// Utilities
	/*
	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}
	*/
	
	/**
	 * Convenience for try-catch block around Semaphore.acquire().
	 */
	private void acquire(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			System.out.println("something went wrong with your semaphore");
			e.printStackTrace();
		}
	}

	private enum OState {RECEIVED, COOKING, COOKED, PLATED, COMPLETED, FAILED};
	public class Order {
		Waiter waiter;
		Customer customer;
		Table table;
		Food food;
		OState state;
		boolean ingredientsReady = false;

		Order(Waiter waiter, Customer customer, Table table, String choice,
				OState state) {
			
			this.waiter = waiter;
			this.customer = customer;
			this.table = table;
			this.food = foods.get(choice);
			this.state = state;
		}

		public String getChoice() {
			return this.food.name;
		}
	}

	private class Food {
		String name;
		int quantity;		// current quantity
		int capacity;		// maximum quantity
		int lowThreshold;	// when quantity is at least this low, restock
		int futureQuantity;	// what the quantity will be after the delivery
		int cookingTime;	// in milliseconds

		Food(String name, int quantity, int capacity, int lowThreshold, int cookingTime) {
			this.name = name;
			this.quantity = quantity;
			this.capacity = capacity;
			this.lowThreshold = lowThreshold;
			this.futureQuantity = quantity;
			this.cookingTime = cookingTime;
		}

		void decrementQuantity() {
			this.quantity -= 1;
			this.futureQuantity -= 1;
		}
	}
	
	public class GrillOrPlate extends Cook.GrillOrPlate {
		int x;
		int y;
		Order order = null;
		boolean showOrder = false;
		
		GrillOrPlate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() { return this.x; }
		public int getY() { return this.y; }
		
		public synchronized Order getOrder() { return this.order; }
		public synchronized void setOrder(Order o) { this.order = o; }
		public synchronized void removeOrder() { order = null; }
		
		public synchronized void showOrder() { showOrder = true; }
		public synchronized void hideOrder() { showOrder = false; }
		public synchronized boolean orderVisible() { return showOrder; }
	}
	
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (7/7) */
	private enum DeliveryState {PLACED, NEED_TO_REORDER, COMPLETE};
	private class MyDelivery {
		Set<Item> items;
		Set<Item> itemsToReorder;
		List<CityLocation> markets;
		DeliveryState state;
		
		MyDelivery(Set<Item> items) {
			this.items = items;
			this.itemsToReorder = new HashSet<Item>();
			this.markets = new ArrayList<CityLocation>();
			this.state = DeliveryState.PLACED;
		}
		
		MyDelivery() {
			this(new HashSet<Item>());
		}
	}
	
	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		// TODO maybe cooks can go on breaks in v3
		return false;
	}
	
}

