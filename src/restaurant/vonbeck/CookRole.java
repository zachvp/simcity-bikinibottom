package restaurant.vonbeck;

import gui.trace.AlertTag;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import kelp.Kelp;
import kelp.KelpClass;
import market.Item;
import market.gui.MarketBuilding;
import market.interfaces.DeliveryReceiver;
import market.interfaces.PhonePayer;
import restaurant.vonbeck.gui.CookGui;
import restaurant.vonbeck.gui.FoodGui;
import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.gui.RestaurantVonbeckBuilding;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Agent;
import agent.Role;
import agent.WorkRole;


public class CookRole extends WorkRole implements DeliveryReceiver {
	private static final long COOK_DELAY_MS = 5000;
	private List<Order> orders 
		= Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, FoodData> inventory 
		= Collections.synchronizedMap(new HashMap<String, FoodData>());
	private static final int INITIAL_STOCK = 5;
	private static final int STOCK_THRESHOLD = 5;
	private static final int STOCK_MAINTAIN = 10;
	private FoodGui foodGui;
	private CookGui cookGui;
	List<MarketBuilding> markets = null;
	private List<MarketOrder> orderList = 
			Collections.synchronizedList(new ArrayList<MarketOrder>());
	private Cashier cashier;
	Timer timer = SingletonTimer.getInstance();

	
	class MarketOrder {
		int marketNum;
		List<Item> itemsToBuy;
		
		public MarketOrder(int marketNum, List<Item> itemsToBuy) {
			this.marketNum = marketNum;
			this.itemsToBuy = itemsToBuy;
		}
	}
	
	class FoodData {
		String name;
		int actualQty;
		int capacity;
		int lowThreshold;
		int futureQty;
		
		public FoodData(String name, int actualQty, int capacity,
				int lowThreshold, int futureQty) {
			this.name = name;
			this.actualQty = actualQty;
			this.capacity = capacity;
			this.lowThreshold = lowThreshold;
			this.futureQty = futureQty;
		}
		
		void decrementQty(){
			actualQty -= 1;
			futureQty -= 1;
		}
	}
	
	@SuppressWarnings("unused")
	public CookRole(Cashier cashier, RestaurantGui gui, 
			RestaurantVonbeckBuilding building) {
		super(building);
		
		for (String foodName : Constants.FOODS) {
			inventory.put(foodName, new FoodData
					(foodName, INITIAL_STOCK, STOCK_MAINTAIN, 
							STOCK_THRESHOLD, INITIAL_STOCK));
		}
		 this.cashier = cashier;
		
		foodGui = new FoodGui(gui);
		
		this.cookGui = new CookGui(this);
		gui.getAnimationPanel().addGui(cookGui);
	}
	
	public void activate() {
		super.activate();
		cookGui.DoGoToWork();
	}
	
	//Messages
	
	void msgCook(Order o) {
		synchronized (orders) {
			orders.add(o);
		}
		stateChanged();
	}

	@Override
	public void msgHereIsYourItems(List<Item> DeliverList) {
		synchronized (inventory){ for (Item item : DeliverList) {
			FoodData currFood = inventory.get(item.name);
			currFood.actualQty += item.amount;
		}}
		
		stateChanged();
	}

	@Override
	public void msgHereIsMissingItems(final List<Item> MissingItemList, 
			int orderNum) {
		if (!MissingItemList.isEmpty()) {
			Do(AlertTag.RESTAURANT, "Market couldn't complete order");
			if (orderNum < markets.size()) {
				orderList.add(new MarketOrder(orderNum+1, new ArrayList<Item>(MissingItemList)));
			} else {
				
				Do(AlertTag.RESTAURANT, "No market can complete this order.");
				
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						for (Item item : MissingItemList) {
							FoodData data = inventory.get(item.name);
							data.futureQty -= item.amount;
						}
					}
				}, 120000);
				
				
			}
			stateChanged();
		}
	}

	

	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if(!orderList.isEmpty()) {
			orderFromMarket(orderList.get(0));
			orderList.remove(0);
			return true;
		}
		
		synchronized (inventory) {
			for (Entry<String, FoodData> entry : inventory.entrySet()) {
				if (entry.getValue().futureQty <
						entry.getValue().lowThreshold) {
					restockFood();
					return true;
				}
			}
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
		synchronized (inventory) {

			inventory.get(o.food).decrementQty();
		
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

			//Move food from grill to plating
		}
	}

	public void restockFood() {
		
		//Populating markets list
		if (markets == null || markets.size() == 0) {
			Kelp kelp = KelpClass.getKelpInstance();
			List<CityLocation> marketsTemp = 
					kelp.placesNearMe(getLocation(), LocationTypeEnum.Market);
			markets = Collections.synchronizedList(new ArrayList<MarketBuilding>());
			
			for (CityLocation cL : marketsTemp) {
				markets.add((MarketBuilding) cL);
			}
		}
		
		ArrayList<Item> itemsToBuy = new ArrayList<Item>();

		
		synchronized (inventory) {
			for (Entry<String, FoodData> entry : inventory.entrySet()) {
				if (entry.getValue().futureQty <
						entry.getValue().lowThreshold) {
					
					int amount = entry.getValue().capacity 
							- entry.getValue().futureQty;
					
					itemsToBuy.add(new Item(entry.getKey(), amount));
					entry.getValue().futureQty += amount;
				}
			}
		}
		
		orderList.add(new MarketOrder(0, itemsToBuy));
		
	}

	private void orderFromMarket(MarketOrder o) {
		MarketBuilding market = markets.get(o.marketNum);
		
		if(market.isOpen()) {
			market.interfaces.Cashier marketCashier =
					(market.interfaces.Cashier) market.getGreeter();

			marketCashier.msgPhoneOrder(o.itemsToBuy, this.cashier,
					this, getLocation(), o.marketNum);
		} else {
			msgHereIsMissingItems(o.itemsToBuy, o.marketNum);
		}
		
	}

	public void removePlate() {
		foodGui.removePlate();
	}

	@Override
	public boolean isAtWork() {
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		return false;
	}

	@Override
	public void msgLeaveWork() {
		//No action, wait for host signal
	}

	public void msgGoHome() {
		cookGui.DoLeaveWork();
		deactivate();
	}

	public boolean thereIsNoFood() {
		for (Entry<String, FoodData> entry : inventory.entrySet()) {
			if (entry.getValue().actualQty > 0) {
				return false;
			}
		}
		return true;
	}

	public void makeLowOnFood() {
		inventory.get(Constants.FOODS.get(0)).futureQty -= 
				inventory.get(Constants.FOODS.get(0)).actualQty;
		inventory.get(Constants.FOODS.get(0)).actualQty = 0; 
		stateChanged();
	}
	
	
}
