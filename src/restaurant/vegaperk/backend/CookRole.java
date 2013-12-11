package restaurant.vegaperk.backend;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.WorkRole;
import agent.interfaces.Person;
import gui.Building;
import gui.trace.AlertTag;

import java.awt.Dimension;
import java.util.*;

import kelp.Kelp;
import kelp.KelpClass;
import restaurant.vegaperk.backend.RevolvingOrderList.Order;
import restaurant.vegaperk.backend.RevolvingOrderList.OrderState;
import restaurant.vegaperk.gui.CookGui;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Cook;
import restaurant.vegaperk.interfaces.Waiter;
import market.Item;
import mock.EventLog;

/**
 * Cook Agent
 */
public class CookRole extends WorkRole implements Cook {
	public EventLog log = new EventLog();
	
	private String name;
	private CookGui cookGui;
	
	private Cashier cashier;
	
	private Kelp kelp = KelpClass.getKelpInstance();
	
	// used to create time delays and schedule events
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	private boolean onOpening = true;
	
	private RevolvingOrderList revolvingOrders;
	private boolean timerSet = false;
	private final int CHECK_REVOLVING_LIST_TIME = 5;
	
	private List<MyDelivery> deliveries = new ArrayList<MyDelivery>();
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	
	private List<Dimension> grillPositions = Collections.synchronizedList(new ArrayList<Dimension>());
	private List<Dimension> platePositions = Collections.synchronizedList(new ArrayList<Dimension>());
	
	private List<PlateZone> plateZones = Collections.synchronizedList(new ArrayList<PlateZone>());
	private List<Grill> grills = Collections.synchronizedList(new ArrayList<Grill>());
	
	private Map<String, Integer> groceries = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	// create an anonymous Map class to initialize the foods and cook times
	@SuppressWarnings("serial")
	private static final Map<String, Integer> cookTimes =
			Collections.synchronizedMap(new HashMap<String, Integer>(){
		{
			put("Krabby Patty", 10);
			put("Kelp Rings", 7);
			put("Coral Bits", 5);
			put("Kelp Shake", 2);
		}
	});
	
	private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	private int orderFromMarket = 0;
	
	@SuppressWarnings("serial")
	Map<String, Food> inventory = Collections.synchronizedMap(new HashMap<String, Food>(){
		{
			put("Krabby Patty", new Food("Krabby Patty", 1, 10, 1, 3));
			put("Kelp Rings", new Food("Kelp Rings", 1, 7, 1, 3));
			put("Coral Bits", new Food("Coral Bits", 1, 5, 1, 3));
			put("Kelp Shake", new Food("Kelp Shake", 1, 2, 1, 3));
		}
	});

	public CookRole(Person person, CityBuilding building) {
		super(person, building);
		
		for(int i = 0; i < 4; i++){
			int startY = 50;
			
			int grillX = 470;
			int plateX = 530;
			
			grillPositions.add(new Dimension(grillX, startY + 50*i));
			platePositions.add(new Dimension(plateX, startY + 50*i));
			
			grills.add(new Grill(grillPositions.get(i).width, grillPositions.get(i).height));
			plateZones.add(new PlateZone(grillPositions.get(i).width, grillPositions.get(i).height));
			PlateZone pz = plateZones.get(i);
			pz = null;
		}
		
	}

	/** Accessor and setter methods */
	public String getName() {
		return name;
	}
	
	/** Messages from other agents */
	
	/** From Waiter */
	public void msgHereIsOrder(Waiter w, String c, int t){
		orders.add(revolvingOrders.getNewOrder(c, t, w, OrderState.NEED_TO_COOK));
		stateChanged();
	}
	public void msgGotFood(int table){
		for(Order o : orders){
			if(o.table == table){
				removeOrder(o);
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgHereIsDelivery() {
		Do("Received delivery.");
	}
	public void msgCanDeliver(Map<String, Integer> deliveries){
		for(Map.Entry<String, Integer> f: deliveries.entrySet()){
			groceries.remove(f.getKey());
			Food food = inventory.get(f.getKey());
			food.amount = food.capacity;
		}
	}
	
	/** From the Cook Gui */
	public void msgAtDestination(){
		doneWaitingForInput();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(onOpening){
			openStore();
			return true;
		}
		
		synchronized(revolvingOrders) {
			for(Order o : revolvingOrders.orderList) {
				if(o.state == OrderState.PICKED_UP) {
					removeOrder(o);
				}
			}
		}
		
		synchronized(inventory) {
			for(Food f : inventory.values()) {
				if(f.futureQuantity <= f.low) {
					restockFood();
					return true;
				}
			}
		}
		
		synchronized (deliveries) {
			for (int i = 0; i < deliveries.size(); i++) {
				MyDelivery delivery = deliveries.get(i);
				
				if (delivery.state == DeliveryState.NEED_TO_REORDER) {
					retryDelivery(delivery, i);
					return true;
				}
			}
		}
		
		synchronized(orders){
			for(Order o : orders){
				if(o.state==OrderState.COOKED){
					plateIt(o);
					return true;
				}
				else if(o.state==OrderState.NEED_TO_COOK){
					tryToCookFood(o);
					return true;
				}
			}
		}
		
		if(!timerSet) {
			Runnable command = new Runnable() {
				public void run(){
					synchronized(revolvingOrders) {
						for(Order o : revolvingOrders.orderList) {
							if(o.state == OrderState.COOKED){
								plateIt(o);
							}
							
							else if(o.state == OrderState.NEED_TO_COOK){
								tryToCookFood(o);
							}
						}
					}
					timerSet = false;
					stateChanged();
				}
			};
			timerSet = true;
			
			schedule.scheduleTaskWithDelay(command,
			CHECK_REVOLVING_LIST_TIME * Constants.MINUTE);
			return true;
		}

		return false;
	}
	
	/** Actions */
	void restockFood() {
		// Find all the items we still need to order.
		Set<Item> itemsToOrder = new HashSet<Item>();
		for (Map.Entry<String, Food> entry : inventory.entrySet()) {
			Food f = entry.getValue();
			if (f.futureQuantity <= f.low) {
				Do(AlertTag.RESTAURANT, "Out of " + f.type);
				itemsToOrder.add(new Item(f.type,
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
	
	private void retryDelivery(MyDelivery delivery, int orderNum) {
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
			Do(AlertTag.RESTAURANT, "No market can complete this order.");
			delivery.state = DeliveryState.COMPLETE;
			return;
		}
		
		// Request the delivery from the new market
		delivery.markets.add(market);
		
		Building restaurant = (Building) getLocation();
		market.interfaces.Cashier marketCashier =
				(market.interfaces.Cashier) market.getGreeter();
		
		Do(AlertTag.RESTAURANT, "Placing an order with market " + market);
		marketCashier.msgPhoneOrder(new ArrayList<Item>(delivery.items),
				this.cashier, this, restaurant, orderNum);
	}
	
	private void removeOrder(Order o) {
		DoRemovePlateFood(o.table);
		PlateZone pz = plateZones.get(o.table);
		pz = null;
	}
	
	private void tryToCookFood(Order o){
		o.state = OrderState.COOKING;
		Food f = inventory.get(o.choice);
		if(f.amount <= 0){
			Do("Out of food");
			o.state = OrderState.OUT_OF_CHOICE;
			
			if(o.waiter instanceof WaiterRole)
				((WaiterRole) o.waiter).msgOutOfChoice(o.choice, o.table);
			
			orders.remove(o);
			
			return;
		}
		
		f.amount--;
		if(f.amount == f.low){
			groceries.put(f.type, f.capacity - f.amount);
		}
		
		DoGoToFridge();
		waitForInput();
		
		DoToggleHolding(o.choice);
		DoGoToGrill(o.table);
		waitForInput();
		
		DoPlaceFood(o.table, o.choice);
		
		DoToggleHolding(null);
		DoGoHome();
		waitForInput();
		
		timeFood(o);
	}

	/**
	 * Delay the food cook time
	 * @param i circumvents timer restrictions
	 */
	private void timeFood(final Order o){
		Runnable command = new Runnable() {
			public void run(){
				o.state = OrderState.COOKED;
				stateChanged();
			}
		};
		
		// resident role will deactivate after the delay below
		schedule.scheduleTaskWithDelay(command,
				cookTimes.get(o.choice) * Constants.MINUTE);
	}
	
	private void plateIt(Order o){
		o.state = OrderState.FINISHED;
		
		if(o.waiter instanceof WaiterRole)
			((WaiterRole)o.waiter).msgOrderDone(o.choice, o.table);
		
		Do("Order done " + o.choice);
		plateZones.get(o.table).setOrder(o);
		
		DoPlateFood(o.table, o.choice);
		waitForInput();
	}
	
	private void openStore(){
		onOpening = false;
		
		DoDrawGrillAndPlates();
	}
	
	/** Animation Functions */
	public void setPresent(boolean b) {
		cookGui.setPresent(b);
	}
	
	private void DoDrawGrillAndPlates(){
		cookGui.setGrillDrawPositions(grillPositions, platePositions);
	}
	private void DoGoToGrill(int grillIndex){
		cookGui.DoGoToGrill(grillIndex);
	}
	private void DoToggleHolding(String item){
		cookGui.DoToggleItem(item);
	}
	private void DoPlaceFood(int grillIndex, String food){
		cookGui.DoPlaceFood(grillIndex, food);
	}
	private void DoPlateFood(int grillIndex, String food){
		cookGui.DoPlateFood(grillIndex, food);
	}
	private void DoRemovePlateFood(int pos){
		cookGui.DoRemovePlateFood(pos);
	}
	private void DoGoToFridge(){
		cookGui.DoGoToFridge();
	}
	private void DoGoHome(){
		setPresent(true);
		cookGui.DoGoHome();
	}
	
	/** Utility Functions */
	public void addMarket(MarketAgent m){
		markets.add(m);
	}

	public void setGui(CookGui gui){
		cookGui = gui;
	}
	
	/* --- Classes --- */
	private enum DeliveryState { PLACED, NEED_TO_REORDER, COMPLETE }
	
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
	
	private class Food {
		String type;
		int amount, cookTime, low, capacity,
		futureQuantity;
		
		OrderState os;
		
		Food(String t, int amt, int ct, int lo, int cap){
			type = t;
			amount = amt;
			cookTime = ct;
			low = lo;
			capacity = cap;
		}
	}
	
	private class Grill{
		int x, y;
		
		Grill(int dx, int dy){
			x = dx;
			y = dy;
		}
	}
	
	private class PlateZone{
		Order order;
		int x, y;
		
		PlateZone(int dx, int dy){
			order = null;
			x = dx;
			y = dy;
		}
		private void setOrder(Order o){
			order = o;
		}
	}
	
	public void setRevolvingOrders(RevolvingOrderList orderList) {
		this.revolvingOrders = orderList;
	}
	
	@Override
	protected void Do(String msg) {
		Do(AlertTag.RESTAURANT, msg);
	}

	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		return false;
	}

	@Override
	public void msgLeaveWork() {
		cookGui.DoLeaveWork();
		waitForInput();
		
		this.deactivate();
	}

	@Override
	public void msgHereIsYourItems(List<Item> DeliverList) {
		for (Item item : DeliverList) {
			Food f = inventory.get(item.name);
			if (f != null && item.amount > 0) {
				Do(AlertTag.RESTAURANT, "Received delivery of " + item.amount
						+ " " + item.name);
				f.amount += item.amount;
			}
		}
	}

	@Override
	public void msgHereIsMissingItems(List<Item> MissingItemList, int orderNum) {
		if (!MissingItemList.isEmpty()) {
			Do(AlertTag.RESTAURANT, "Market couldn't complete order");
			deliveries.get(orderNum).itemsToReorder.addAll(MissingItemList);
			stateChanged();
		}
	}

	public void setCashier(CashierRole cashier) {
		this.cashier = cashier;
	}
}