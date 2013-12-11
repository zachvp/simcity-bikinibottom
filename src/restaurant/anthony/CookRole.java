package restaurant.anthony;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Agent;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.InfoPanel;
import restaurant.anthony.MarketRole.Delivery;
import restaurant.anthony.WaiterRoleBase.Order;
import restaurant.anthony.gui.CookGui;
import restaurant.anthony.gui.HostGui;
import restaurant.anthony.interfaces.Cook;
import restaurant.anthony.interfaces.Market;
import restaurant.anthony.interfaces.Waiter;
import restaurant.anthony.Food;
import gui.Building;
import gui.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kelp.Kelp;
import kelp.KelpClass;
import market.Item;

/**
 * Restaurant Host Agent
 */
// Cook Agent
public class CookRole extends WorkRole implements Cook {

	public enum AgentState {
		Idle, Cooking, OffWork, NotAtWork
	};
	private AgentState state = AgentState.NotAtWork;
	private HostRole host;
	Timer timer = new Timer();
	private List<MyDelivery> deliveries = new ArrayList<MyDelivery>();
	public List<Order> MyOrders = Collections.synchronizedList(new ArrayList<Order>());
	private List<Item> ShoppingList = Collections.synchronizedList(new ArrayList<Item>());
	private Kelp kelp = KelpClass.getKelpInstance();
	private InfoPanel infoPanel;
	
	private List<Stove> Stoves = Collections.synchronizedList(new ArrayList<Stove>());
	{
	for (int i=0; i<3;i++){
		Stoves.add(new Stove());
	}
	}
	
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atHome = new Semaphore(0,true);
	private Semaphore atStove = new Semaphore(0,true);
	private Semaphore atPlateArea = new Semaphore(0,true);
	private Semaphore atExit = new Semaphore (0,true);
	
	private CookGui cookGui;
	
	Map<String, Food> foods = new HashMap<String, Food>();
	

	public CookRole(Person person, CityLocation location) {
		super(person , location);

	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return person.getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#getName()
	 */
	@Override
	public String getName() {
		return person.getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#getMyOrders()
	 */
	@Override
	public List getMyOrders() {
		return MyOrders;
	}

	// Messages	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgAtFridge()
	 */
	@Override
	public void msgAtFridge(){
		//print ("At Fridge");
		atFridge.release();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgAtStove(int)
	 */
	@Override
	public void msgAtStove(int i){
		print ("At Stove");
		atStove.release();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgAtPlatingArea(int)
	 */
	@Override
	public void msgAtPlatingArea(int i){
		print ("At PlatingArea");
		atPlateArea.release();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgAtHome()
	 */
	@Override
	public void msgAtHome(){
		//print ("At home position");
		state = AgentState.Idle;
		atHome.release();
		stateChanged();
	}
	

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#HeresTheOrder(restaurant.anthony.WaiterRoleBase.Order, restaurant.anthony.interfaces.Waiter)
	 */
	@Override
	public void HeresTheOrder(Order order, Waiter w) {
		order.process = false;
		MyOrders.add(order);
		stateChanged();
		// DoCookOrder(order, w);
	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgIGetOrder(restaurant.anthony.WaiterRoleBase.Order)
	 */
	@Override
	public void msgIGetOrder (Order order){
		cookGui.RemoveFood(order.Waiter.getWaiterNumber(), "PlatingArea");
	}
	
	/* (non-Javadoc)
	 * @see msgHereIsMissingItems
	 */
	public void msgHereIsMissingItems(List<Item> MissingItemList, int orderNum) {
		if (!MissingItemList.isEmpty()) {
			Do(AlertTag.RESTAURANT, "Market couldn't complete order");
			deliveries.get(orderNum).itemsToReorder.addAll(MissingItemList);
			stateChanged();
		}
	}
	
	@Override
	// from market delivery guy
	public void msgHereIsYourItems(List<Item> DeliverList) {
		for (Item item : DeliverList) {
			Food f = foods.get(item.name);
			if (f != null && item.amount > 0) {
				Do(AlertTag.RESTAURANT, "Received delivery of " + item.amount
						+ " " + item.name);
				f.amount += item.amount;
			}
		}
		UpdateInfoPanel();
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#msgLeaveWork()
	 */
	@Override
	public void msgLeaveWork() {
		state = AgentState.NotAtWork;
		stateChanged();
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#atExit()
	 */
	@Override
	public void atExit(){
		atExit.release();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		/*
		 * for (int i=0; i<4;i++){ String choice; if (i == 0){ choice = "Steak";
		 * } if (i == 1){ choice = "Chicken"; } if (i == 2){ choice = "Pizza"; }
		 * if (i == 3){ choice = "Salad"; } }
		 */
		if (state == AgentState.NotAtWork){
			GoToWork();
			return true;
		}

		while (!MyOrders.isEmpty()) {
			synchronized(MyOrders){
			for (int i = 0; i < MyOrders.size(); i++) {
				if (!MyOrders.get(i).process) {
					DoCookOrder(MyOrders.get(i), MyOrders.get(i).Waiter);
					MyOrders.remove(i);
					return true;
				}
			}
			}
		}

		for (int i = 0 ; i <deliveries.size();i++){
			if (deliveries.get(i).state.equals(DeliveryState.NEED_TO_REORDER)){
				retryDelivery(deliveries.get(i), i);
				return true;
			}
		}
		
		if (state == AgentState.OffWork && MyOrders.size() == 0){
			OffWork();
		}

		return false;
	}

	// Actions
	private void OffWork(){
		cookGui.GoToExit();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
		state = AgentState.NotAtWork;
	}
	
	private void GoToWork(){
		cookGui.GoToWork();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void DoCookOrder(final Order o, final Waiter w) { // final?
		print(o.name);
		state = AgentState.Cooking;
		final Food currentFood = foods.get(o.name);
		GoToFridge();

		if (currentFood.amount == 0) {
			print("No more " + currentFood.choice);
			GoToHome();
			w.msgNoMoreFood(o.name, o);
			CheckInventory();
			return;
		} else
			GoToStove(w.getWaiterNumber());
			cookGui.AddFood(w.getWaiterNumber(), "Stove");
			/*
			for (int i=0;i<Stoves.size();i++){
				if (!Stoves.get(i).occupied)
				{
					o.stove = i+1;
					Stoves.get(i).occupied = true;
					GoToStove(o.stove);
					cookGui.AddFood(i, "Stove");
					
					break;
				}
			}
			*/
			GoToHome();
			timer.schedule(new TimerTask() {
				public void run() {
					print("Done cooking " + o.name);
					GoToStove(w.getWaiterNumber());
					
					cookGui.RemoveFood(w.getWaiterNumber(), "Stove");
					Stoves.get(w.getWaiterNumber()).occupied = false;
					GoToPlateArea(w.getWaiterNumber());
					cookGui.AddFood(w.getWaiterNumber(), "PlatingArea");
					w.OrderIsReady(o);
					currentFood.foodConsumed();
					//Update InfoPanel
					UpdateInfoPanel();
					GoToHome();
					state = AgentState.Idle;
					if (currentFood.amount <= 5)
						CheckInventory();
					stateChanged();
				}
			}, currentFood.cookingTime);

	}
	
	private void GoToFridge(){
		cookGui.GoToFridge();
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void GoToStove(int i){
		cookGui.GoToStove(i);
		try {
			atStove.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void GoToPlateArea(int i){
		cookGui.GoToPlateArea(i);
		try {
			atPlateArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GoToHome(){
		cookGui.GoToHomePosition();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void CheckInventory() {
		if (!ShoppingList.isEmpty()){
		ShoppingList.clear();
		}
		
		for (Map.Entry<String, Food> entry : foods.entrySet()) {
			Food f = entry.getValue();
			if (f.amount <= f.amountLowThreshold) {
				Do(AlertTag.RESTAURANT, "Going to buy food : " + f.choice);
				ShoppingList.add(new Item(f.choice, f.amountBuyThreshold - f.amount));
			}
		}
		
		if (ShoppingList.isEmpty()){
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
				MyDelivery delivery = new MyDelivery(ShoppingList);
				delivery.markets = new ArrayList<CityLocation>();
				int orderNum = 0;
				
				market.interfaces.Cashier marketCashier =
						(market.interfaces.Cashier) market.getGreeter();
				
				Do(AlertTag.RESTAURANT, "Placing an order with market " + market);
				marketCashier.msgPhoneOrder(new ArrayList<Item>(delivery.items),
						host.cashier, this, restaurant, orderNum);
				delivery.markets.add(market);
				deliveries.add(delivery);
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
				host.cashier, this, restaurant, orderNum);
	}



	private class Stove {
		boolean occupied;
		Stove (){
			occupied = false;
		}
	}
	
	private enum DeliveryState {PLACED, NEED_TO_REORDER, COMPLETE};
	private class MyDelivery {
		List<Item> items;
		List<Item> itemsToReorder;
		List<CityLocation> markets;
		DeliveryState state;
		
		MyDelivery(List<Item> items) {
			this.items = items;
			this.itemsToReorder = new ArrayList<Item>();
			this.markets = new ArrayList<CityLocation>();
			this.state = DeliveryState.PLACED;
		}
		
		MyDelivery() {
			this(new ArrayList<Item>());
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#isAtWork()
	 */
	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}
	
	private void UpdateInfoPanel(){
		Map<String, Integer> tempMap = new HashMap<String,Integer>();
		for (Map.Entry<String, Food> entry : foods.entrySet()) {
			int currentAmount = entry.getValue().amount;
			String currentName = entry.getKey();
			tempMap.put(currentName, currentAmount);
		}
		infoPanel.UpdateInfoPanel(tempMap);
	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#isOnBreak()
	 */
	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#setGui(restaurant.anthony.gui.CookGui)
	 */
	@Override
	public void setGui(CookGui cGui) {
		cookGui = cGui;
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#setHost(restaurant.anthony.HostRole)
	 */
	@Override
	public void setHost(HostRole h){
		host = h;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.anthony.Cook#setInventoryList(java.util.Map)
	 */
	@Override
	public void setInventoryList(Map<String, Food> iList){
		foods = iList;
	}
	
	public void setInfoPanel (InfoPanel in){
		infoPanel = in;
	}
	


	

}
