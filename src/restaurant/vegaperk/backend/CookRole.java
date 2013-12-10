package restaurant.vegaperk.backend;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.WorkRole;
import agent.interfaces.Person;
import gui.trace.AlertTag;

import java.awt.Dimension;
import java.util.*;

import restaurant.vegaperk.gui.CookGui;
import restaurant.vegaperk.interfaces.Cook;
import restaurant.vegaperk.interfaces.Waiter;
import mock.EventLog;

/**
 * Cook Agent
 */
public class CookRole extends WorkRole implements Cook {
	public EventLog log = new EventLog();
	
	private String name;
	private CookGui cookGui;
	
	// used to create time delays and schedule events
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	private boolean onOpening = true;
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private List<Grill> grills = Collections.synchronizedList(new ArrayList<Grill>());
	
	private List<Dimension> grillPositions = Collections.synchronizedList(new ArrayList<Dimension>());
	private List<Dimension> platePositions = Collections.synchronizedList(new ArrayList<Dimension>());
	
	private List<PlateZone> plateZones = Collections.synchronizedList(new ArrayList<PlateZone>());
	
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
			put("Krabby Patty", new Food("Krabby Patty", 2, 10, 1, 3));
			put("Kelp Rings", new Food("Kelp Rings", 2, 7, 1, 3));
			put("Coral Bits", new Food("Coral Bits", 2, 5, 1, 3));
			put("Kelp Shake", new Food("Kelp Shake", 10, 2, 1, 3));
		}
	});

	public CookRole(Person person, CityBuilding building) {
		super(person, building);
		
		for(int i = 0; i < 4; i++){
			int startY = 50;
			
			int grillX = 370;
			int plateX = 430;
			
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
		orders.add(new Order(c, t, w, OrderState.PENDING));
		stateChanged();
	}
	public void msgGotFood(int table){
		for(Order o : orders){
			if(o.table == table){
				DoRemovePlateFood(table);
				PlateZone pz = plateZones.get(table);
				pz = null;
			}
		}
		stateChanged();
	}
	/** From Market(s) */
	public void msgCannotDeliver(Map<String, Integer> list){
		Do("Can't fulfill order.");
		orderFromMarket++;
		
		if(orderFromMarket < markets.size()){
			orderFoodThatIsLow(list);
		}
		else{
			Do("Not at any markets!");
			orderFromMarket = 0;
		}
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
		if(!groceries.isEmpty()){
			orderFoodThatIsLow(groceries);
		}
		synchronized(orders){
			for(Order o : orders){
				if(o.state==OrderState.COOKED){
					plateIt(o);
					return true;
				}
			}
			for(Order o : orders){
				if(o.state==OrderState.PENDING){
					tryToCookFood(o);
					return true;
				}
			}
		}
		return false;
	}
	
	/** Actions */
	private void tryToCookFood(Order o){
		o.state = OrderState.COOKING;
		Food f = inventory.get(o.choice);
		if(f.amount <= 0){
			Do("Out of food");
			o.waiter.msgOutOfChoice(o.choice, o.table);
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
//		must iterate through by integer instead of pointers because of the timer below
		for(int i = 0; i < orders.size(); i++){
			if(orders.get(i) == o){
				timeFood(i);
			}
		}
	}

	/**
	 * Delay the food cook time
	 * @param i circumvents timer restrictions
	 */
	private void timeFood(final int i){
		Runnable command = new Runnable() {
			public void run(){
				Do(orders.get(i).choice + " done.");
				orders.get(i).state = OrderState.COOKED;
				stateChanged();
			}
		};
		
		// resident role will deactivate after the delay below
		schedule.scheduleTaskWithDelay(command,
				cookTimes.get(orders.get(i).choice) * Constants.MINUTE);
	}
	
	private void plateIt(Order o){
		o.state = OrderState.FINISHED;
		Do("Order Plated");
		o.waiter.msgOrderDone(o.choice, o.table);
		Do("Order done " + o.choice);
		plateZones.get(o.table).setOrder(o);
		
		DoPlateFood(o.table, o.choice);
		waitForInput();
	}
	
	private void openStore(){
		onOpening = false;
		
		DoDrawGrillAndPlates();
		Do("Opening restaurant");
		
		for(Map.Entry<String, Food> entry : inventory.entrySet()){
			Food f = entry.getValue();
			if(f.amount <= f.low){
				orderFoodThatIsLow(groceries);
			}
		}
	}
	
	private void orderFoodThatIsLow(Map<String, Integer> list){
		Do("Ordered food.");
		MarketAgent m = markets.get(orderFromMarket);
		m.msgNeedFood(list);
		list.clear();
	}
	
	/** Animation Functions */
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
		cookGui.DoGoHome();
	}
	
	/** Utility Functions */
	public void addMarket(MarketAgent m){
		markets.add(m);
	}

	public void setGui(CookGui gui){
		cookGui = gui;
	}
	
	/** Classes */
	enum OrderState { PENDING, COOKING, COOKED, FINISHED };
	private class Order{
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
	
	private class Food{
		String type;
		int amount, cookTime, low, capacity;
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
	
	@Override
	protected void Do(String msg) {
		Do(AlertTag.RESTAURANT, msg);
	}

	@Override
	public boolean isAtWork() {
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		return isActive();
	}

	@Override
	public void msgLeaveWork() {
		this.deactivate();
	}
}