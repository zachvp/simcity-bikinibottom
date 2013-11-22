package restaurant.strottma;

import restaurant.strottma.HostRole.Table;
import restaurant.strottma.gui.CookGui;
import restaurant.strottma.interfaces.Cook;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Market;
import restaurant.strottma.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;
import agent.Role;

/**
 * Restaurant Cook Role
 */
public class CookRole extends Role implements Cook {

	// private CookGui cookGui = null;
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private Timer timer;
	
	private Map<String, Food> foods;
	private List<Market> markets;
	private List<GrillOrPlate> grills;
	private List<GrillOrPlate> plateAreas;
	
	public List<GrillOrPlate> getGrills() { return grills; }
	public List<GrillOrPlate> getPlateAreas() { return plateAreas; }
	
	private Semaphore multiStepAction = new Semaphore(0, true);
	
	private CookGui gui;
	
	public void setGui(CookGui cookGui) {
		this.gui = cookGui;
	}

	public CookRole(PersonAgent person) {
		super(person);
		
		this.timer = new Timer();

		this.foods = new HashMap<String, Food>();
		this.markets = new ArrayList<Market>();
		this.grills = new ArrayList<GrillOrPlate>();
		this.plateAreas = new ArrayList<GrillOrPlate>();
		
		// new Food(name, quantity, capacity, low threshold, cooking time in milliseconds)
		Food st = new Food("Steak",   3, 5, 2, 7*1000);
		Food ck = new Food("Chicken", 0, 5, 2, 5*1000);
		Food sa = new Food("Salad",   1, 5, 2, 2*1000);
		Food pz = new Food("Pizza",   3, 5, 2, 8*1000);
		
		// create grills
		for (int i = 0; i < 5; i++) {
			grills.add(new GrillOrPlate(850, 14 + 30*i));
		}
		
		// create plating areas
		for (int i = 0; i < 5; i++) {
			plateAreas.add(new GrillOrPlate(750, 14 + 30*i));
		}
		
		this.foods.put(st.name, st);
		this.foods.put(ck.name, ck);
		this.foods.put(sa.name, sa);
		this.foods.put(pz.name, pz);
	}
	
	public void addMarket(Market m) {
		markets.add(m);
	}
	
	// Messages
	void msgHereIsAnOrder(Waiter w, Customer c, Table t,
			String choice) {
		
		Do("Received an order for " + choice + " from " + c);

		orders.add( new Order(w, c, t, choice, OState.RECEIVED) );

		stateChanged();
	}
	
	void msgOrderDoneCooking(int orderNum) { // from TimerTask
		Do("Order " + orderNum + " done cooking");

		Order o = orders.get(orderNum);
		o.state = OState.COOKED;

		stateChanged();
	}
	
	public void msgCannotDeliver(Map<String, Integer> cannotDeliver, int marketNumber) {
		if (cannotDeliver.size() != 0) {
			Do("Market " + marketNumber + " can't complete delivery.");
			if (marketNumber+1 < markets.size()) {
				Do("Requesting delivery from next market.");
				markets.get(marketNumber+1).msgLowOnFood(cannotDeliver, marketNumber+1);
			}
		}
	}
	
	public void msgHereIsDelivery(Map<String, Integer> delivery) {
		synchronized (delivery) {
			for (Map.Entry<String, Integer> entry : delivery.entrySet()) {
				Food f = foods.get(entry.getKey());
				if (f != null && entry.getValue() != 0) {
					Do("Received delivery of " + entry.getValue() + " " + f.name);
					f.quantity += entry.getValue();
				}
			}
		}
	}
	
	public void msgAtDestination() { // from gui
		multiStepAction.release();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		synchronized (foods) {
			for (Food f : foods.values()) {
				if (f.futureQuantity <= f.lowThreshold) {
					restockFood();
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
		
		// We have tried all our rules and found nothing to do. So return false
		// to main loop of abstract Role and wait.
		return false;
	}

	// Actions
	boolean tryToCookIt(Order o, final int orderNum) {
		if (o.food.quantity <= 0) {
			Do("Can't cook order for " + o.customer.getName() + " - out of " + o.food.name);
			o.waiter.msgOutOfChoice(o.customer, o.table, o.getChoice());
			return false;
		}
		
		Do("Cooking " + o.food.name + " for " + o.customer.getName());
		
		if (!o.ingredientsReady) {
			Do("Getting ingredients for order");
			DoGoToFridge();
			
			acquire(multiStepAction);
			
			Do("At fridge.");
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
		Do("Plating order for " + o.customer);
		
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
	
	void restockFood() {
		Map<String, Integer> delivery = new HashMap<String, Integer>();
		synchronized (foods) {
			for (Map.Entry<String, Food> entry : foods.entrySet()) {
				Food f = entry.getValue();
				if (f.futureQuantity <= f.lowThreshold) {
					Do("Out of " + f.name);
					delivery.put(entry.getKey(), f.capacity - f.futureQuantity);
					f.futureQuantity = f.capacity;
				}
			}
		}
		if (delivery.size() != 0 && markets.size() != 0) {
			Market m = markets.get(0);
			Do("Restocking food from market " + m.getName());
			m.msgLowOnFood(delivery, 0);
		}
	}

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
	
}

