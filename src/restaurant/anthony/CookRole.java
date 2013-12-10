package restaurant.anthony;

import CommonSimpleClasses.CityLocation;
import agent.Agent;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.anthony.MarketRole.Delivery;
import restaurant.anthony.WaiterRoleBase.Order;
import restaurant.anthony.gui.CookGui;
import restaurant.anthony.gui.HostGui;
import restaurant.anthony.interfaces.Market;
import restaurant.anthony.interfaces.Waiter;
import restaurant.anthony.Food;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Host Agent
 */
// Cook Agent
public class CookRole extends WorkRole {

	public enum AgentState {
		Idle, Cooking, OffWork, NotAtWork
	};
	public enum AgentEvent {
		NotAtWork, AtWork
	}

	private AgentEvent event = AgentEvent.NotAtWork;
	private AgentState state = AgentState.NotAtWork;
	private HostRole host;
	Timer timer = new Timer();
	public List<Order> MyOrders = Collections.synchronizedList(new ArrayList<Order>());
	private List<Food> ShoppingList = Collections.synchronizedList(new ArrayList<Food>());
	private List<MyMarketAgent> MarketList = Collections.synchronizedList(new ArrayList<MyMarketAgent>());

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

	public String getMaitreDName() {
		return person.getName();
	}

	public String getName() {
		return person.getName();
	}

	public List getMyOrders() {
		return MyOrders;
	}

	// Messages	
	public void msgAtFridge(){
		//print ("At Fridge");
		atFridge.release();
	}
	
	public void msgAtStove(int i){
		print ("At Stove");
		atStove.release();
	}
	
	public void msgAtPlatingArea(int i){
		print ("At PlatingArea");
		atPlateArea.release();
	}
	
	public void msgAtHome(){
		//print ("At home position");
		state = AgentState.Idle;
		atHome.release();
		stateChanged();
	}
	

	public void HeresTheOrder(Order order, Waiter w) {
		order.process = false;
		MyOrders.add(order);
		stateChanged();
		// DoCookOrder(order, w);
	}

	public void msgIGetOrder (Order order){
		cookGui.RemoveFood(order.Waiter.getWaiterNumber(), "PlatingArea");
	}
	
	public void Orderfulfillment(List<Delivery> DeliverList, Market m) {

		for (int i = 0; i < DeliverList.size(); i++) {
			// Re-stock the item into the food list
			print("DeliverList.size" + DeliverList.size());
			foods.get(DeliverList.get(i).choice).amount += DeliverList.get(i).amount;

			// The requirement is Fulfilled
			if (DeliverList.get(i).fulfilled) {
				// print (DeliverList.get(i).choice + " is fulfilled by " +
				// m.name);
				continue;
			}
			// The requirement cannot be Fulfilled
			else {
				// print (DeliverList.get(i).choice + " fails to fulfill by " +
				// m.name);
				CheckInventory();
				break;
			}

			// print (DeliverList.get(i).choice + " : " +
			// foods.get(DeliverList.get(i).choice).amount);
		}

	}
	
	@Override
	public void msgLeaveWork() {
		event = AgentEvent.NotAtWork;
		stateChanged();
		
	}
	
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

		while (!MyOrders.isEmpty() && state == AgentState.Idle) {
			synchronized(MyOrders){
			for (int i = 0; i < MyOrders.size(); i++) {
				if (!MyOrders.get(i).process) {
					DoCookOrder(MyOrders.get(i), MyOrders.get(i).Waiter);
					CheckInventory();
					MyOrders.remove(i);
					return true;
				}
			}
			}
		}

		if (!((List<Food>) ShoppingList).isEmpty())
			BuyFoods();
		
		if (event == AgentEvent.NotAtWork && MyOrders.size() == 0){
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
		event = AgentEvent.AtWork;
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
		/*
		print("Checking Inventory");
		// Clear The shopping List
		if (!ShoppingList.isEmpty())
			ShoppingList.clear();

		// Get The ShoppingList
		for (int i = 0; i < foods.size(); i++) {
			String j = null;
			if (i == 0)
				j = "Kelp Shake";
			if (i == 1)
				j = "Coral Bits";
			if (i == 2)
				j = "Krabby Patty";
			if (i == 3)
				j = "Kelp Rings";

			if (foods.get(j).amount <= 1) {
				// 5 is the threshold
				// print (foods.get(j).choice +
				// " is added to the shoppinglist");
				ShoppingList.add(new Food(foods.get(j).choice,
						foods.get(j).cookingTime, 5 - foods.get(j).amount));
			}
		}
		*/
	}

	private void BuyFoods() {

		// Find the LeastOrder Market out
		int LeastOrderTime = MarketList.get(0).OrderTimes;
		int LeastOrderMarketIndex = 0;
		for (int i = 0; i < MarketList.size(); i++) {
			if (LeastOrderTime > MarketList.get(i).OrderTimes) {
				LeastOrderTime = MarketList.get(i).OrderTimes;
				LeastOrderMarketIndex = i;
			}

		}
		// Order Food from the Least Order Market and Increment the Order time
		// by one.
		print("Ordering food to "
				+ MarketList.get(LeastOrderMarketIndex).market.name);
		MarketList.get(LeastOrderMarketIndex).market
				.BuyFood(ShoppingList, this);
		MarketList.get(LeastOrderMarketIndex).OrderTimes++;
	}

	public void SetMarket(List<MarketRole> MA) {
		for (int i = 0; i < MA.size(); i++) {
			MyMarketAgent temp = new MyMarketAgent(MA.get(i));
			MarketList.add(temp);
		}
	}

	private class Stove {
		boolean occupied;
		Stove (){
			occupied = false;
		}
	}
	
	private class MyMarketAgent {
		MarketRole market;
		int OrderTimes;

		MyMarketAgent(MarketRole ma) {
			market = ma;
			OrderTimes = 0;
		}
	}

	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setGui(CookGui cGui) {
		cookGui = cGui;
		
	}
	
	public void setHost(HostRole h){
		host = h;
	}
	
	public void setInventoryList(Map<String, Food> iList){
		foods = iList;
	}

	

}
