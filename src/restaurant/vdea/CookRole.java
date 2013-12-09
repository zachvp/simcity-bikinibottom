package restaurant.vdea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.vdea.gui.*;
import restaurant.vdea.interfaces.*;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

public class CookRole extends WorkRole implements Cook{

	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public enum OrderStatus
	{pending, enoughFood, notEnoughFood, cooking, cooked, done, none, shipmentIncomplete};
	//private OrderStatus status = OrderStatus.pending;//The start state
	private OrderStatus shipmentStatus = OrderStatus.none;

	private Cashier cashier;
	private String name;	
	private Timer timer = new Timer();
	public CookGui cookGui = null;
	public List<Market> markets = new ArrayList<Market>();
	int marketTried = 0;

	//Food(name, init inventory numbers)
	private Food chicken = new Food("chicken", 3);
	private Food salad = new Food("salad", 2);
	private Food pizza = new Food("pizza", 3);
	private Food steak = new Food("steak", 6);
	public List<Food> inventory = new ArrayList<Food>();
	boolean orderSent;

	public CookRole(Person person, CityLocation location) {
		super(person, location);

		this.name = super.getName();

		inventory.add(chicken);
		inventory.add(pizza);
		inventory.add(salad);
		inventory.add(steak);
	}

	public void setCashier(Cashier c){
		cashier = c;
	}

	public void addMarket(Market m){
		markets.add(m);
	}

	// Messages

	//from waiter
	public void msgThereIsAnOrder(Waiter w, String choice, int table) {
		orders.add(new Order(w, choice, table));
		//status = OrderStatus.pending;
		stateChanged();
	}

	//from market
	public void msgOrderFufillment(List<Food> shipment, boolean orderFull){
		print("RESTOCKING INVENTORY");
		for (Food f: shipment){
			if(f.equals(steak)){
				steak.addInventory(f.quantity);
				//print("steak in "+ f.quantity);
			}
			else if(f.equals(chicken)){
				chicken.addInventory(f.quantity);
				//print("chicken in "+ f.quantity);
			}
			else if(f.equals(salad)){
				salad.addInventory(f.quantity);
				//print("salad in "+ f.quantity);
			}
			else if(f.equals(pizza)){
				pizza.addInventory(f.quantity);
				//print("pizza in "+ f.quantity);
			}
		}
		orderSent = false; //received back order

		for(Food i: inventory){
			print("new "+ i.getName() + " quantity: "+ i.quantity);
		}

		if(!orderFull){
			print("Shipment from market incomplete");
			shipmentStatus = OrderStatus.shipmentIncomplete;
			stateChanged();
		}
		else{
			print("Inventory is full!");
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		if  (shipmentStatus == OrderStatus.shipmentIncomplete){
			restockInventory();
		}
		else{
			synchronized(orders){
				for (Order order : orders) {
					if(order.status == OrderStatus.pending){
						checkOrder(order);
					}
					if (order.status == OrderStatus.enoughFood) {
						cookOrder(order);
					}
					if (order.status == OrderStatus.notEnoughFood){
						sendBackOrder(order);
						return true;
					}
					if (order.status == OrderStatus.cooked){
						orderDone(order);
						return true;
					}
				}
			}
		}
		return false;
	}

	// Actions

	private void checkOrder(Order o){
		boolean enough = checkFoodInventory(o.choice);	//check if there is enough food

		if (enough){
			print("There is enough " + o.choice);
			decreaseQuantity(o);
			o.status = OrderStatus.enoughFood;
			//stateChanged();
		}
		else{
			print("There is not enough " + o.choice);
			o.status = OrderStatus.notEnoughFood;
			//restockInventory();	//TODO MarketAgent test
			//stateChanged();
		}
	}

	private void decreaseQuantity(Order o){
		if(o.choice.getName().equals("steak")){
			steak.cook();
			System.out.println(steak.quantity + " steaks left");
		}
		else if(o.choice.getName().equals("chicken")){
			chicken.cook();
		}
		else if(o.choice.getName().equals("salad")){
			salad.cook();
		}
		else if(o.choice.getName().equals("pizza")){
			pizza.cook();
		}
	}


	//do we have enough to make one more food item?
	//and check if food hits threshold
	public boolean checkFoodInventory(Food f){
		Food compare = new Food();
		print("Checking amount of " + f);
		if(f.equals(steak)){
			compare = steak;
		}
		else if(f.equals(chicken)){
			compare = chicken;
		}
		else if(f.equals(salad)){
			compare = salad;
		}
		else if(f.equals(pizza)){
			compare = pizza;
		}


		if(!orderSent && compare.quantity <= compare.threshold){	//TODO inventory
			//print("low on " + compare);
			print("under threshold, need to restock");
			restockInventory();
		}

		//System.out.println(compare + ", amount: " + compare.amount + ", threshold: " + compare.threshold);
		return (compare.quantity) >= 1;//compare.threshold;
	}


	//if one of the foods is low, triggers to check all inventory
	private void restockInventory() {
		print("restocking inventory");
		List<Food> shoppingList = new ArrayList<Food>();
		if(shipmentStatus == OrderStatus.shipmentIncomplete){
			for (Food f: inventory){
				if (f.quantity <= f.capacity){
					shoppingList.add(f);
				}
			}
			shipmentStatus = OrderStatus.none;
		}
		else{
			for (Food f: inventory){
				if (f.quantity <= f.threshold){

					shoppingList.add(f);
				}
			}
		}

		orderSent = true;
		markets.get(marketTried).msgOrderRequest(this, shoppingList, cashier); //TODO
		marketTried = marketTried == markets.size()-1 ? 0 : marketTried + 1;	//switches between markets
	}

	private void sendBackOrder(Order o){
		o.w.msgNotEnoughFood(o.choice.getName(), o.table);
		orders.remove(o);
		//o.status = OrderStatus.done;
	}


	private void cookOrder(final Order o) { //TODO fix
		print("Cooking " + o.choice + "...");
		o.status = OrderStatus.none;
		cookGui.DoCooking(o.choice.getName(), o.table);
		timer.schedule(new TimerTask() {
			public void run() {
				o.status = OrderStatus.cooked;
				stateChanged();
			}
		},
		o.getOrderCookTime() * 500);
	}

	private void orderDone(Order o){
		cookGui.doneCooking(o.table);
		print(o.choice +" is ready!");
		o.w.msgOrderReady(o.choice.getName(), o.table);
		orders.remove(o);
		o.status = OrderStatus.done;
	}

	//utilities

	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}

	public void gotFood(int table){
		cookGui.collected(table);
	}


	private class Order {
		Waiter w;
		int table;
		Food choice;
		OrderStatus status = OrderStatus.pending;//The start state

		Order(Waiter inW, String inChoice, int t){
			w = inW;
			table = t;
			choice = new Food(inChoice);
		}

		public int getOrderCookTime(){
			return choice.getCookTime();
		}
	}


	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

}
