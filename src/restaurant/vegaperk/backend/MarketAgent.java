package restaurant.vegaperk.backend;

import agent.Agent;

import java.util.*;

import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Cook;
import restaurant.vegaperk.interfaces.Market;
import mock.EventLog;

/**
 * Market Agent
 */
public class MarketAgent extends Agent implements Market {
	String name = null;
	int id = -1;
	public EventLog log = new EventLog();
	
	private double money = 0.0;
	private double BILL = 25;
	
	private List<MyDelivery> deliveries = Collections.synchronizedList(new ArrayList<MyDelivery>());
	
	private Map<String, Integer> canDeliver = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Map<String, Integer> cannotDeliver = Collections.synchronizedMap(new HashMap<String, Integer>());

	private Cook cook = null;
	private Cashier cashier = null;
	
	private static final int DELIVER_TIME = 15; 
	Timer timer = new Timer();
	
	Map<String, Integer> inventory;

//	latter parameters determine the amount of each type of food
	public MarketAgent(String name) {
		super();
		this.name = name;
		
//		create an anonymous Map class to initialize the foods and cook times
		inventory = new HashMap<String, Integer>();
	}
	
	/** Accessor and setter methods */
	public int getId() {
		return id;
	}
	
	/** Messages from other agents */
	
	/** From Cook */
	public void msgNeedFood(Map<String, Integer> groceries){
		for(Map.Entry<String, Integer> food : groceries.entrySet()){
			deliveries.add(new MyDelivery(food.getKey(), food.getValue()));
			if(food.getKey().equals("Coral Bits")){
				BILL = 500;
			}
			else{
				BILL = 25;
			}
		}
		stateChanged();
	}
	
	/** From Timer */
	private void timerDone(){
		cook.msgHereIsDelivery();
		for(Map.Entry<String, Integer> entry : canDeliver.entrySet()){
			int currentAmount = inventory.get(entry.getKey());
			currentAmount -= entry.getValue();
		}
	}
	
	/** From Cashier */
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		money += payment;
		Do("Received payment. Money is now " + money);
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		synchronized(deliveries){
			for(MyDelivery md : deliveries){	
				if(md.state == DeliveryState.PENDING){
					Do("Checking inventory");
					checkInventory(md);
					return true;
				}
			}
		}
		synchronized(cannotDeliver){
			if(!cannotDeliver.isEmpty()){
				Do("Checking inventory");
				tellCookNoFood();
				return true;
			}
		}
		synchronized(canDeliver){
			if(!canDeliver.isEmpty()){
				Do("Checking inventory");
				deliverFood();
				return true;
//			}
			}
		}
		return false;
	}
	
	/** Actions */
	private void checkInventory(MyDelivery md){
		int currentAmount = inventory.get(md.type);
		if(currentAmount >= md.amount){
			Do("Can deliver " + md.type + ". Please wait...");
			md.state = DeliveryState.CAN_DELIVER;
			canDeliver.put(md.type, md.amount);
		}
		else{
			Do("Cannot deliver " + md.type);
			md.state = DeliveryState.CANNOT_DELIVER;
			cannotDeliver.put(md.type, md.amount);
		}
		deliveries.remove(md);
		stateChanged();
	}
	private void deliverFood(){
		//put into a different action
		cook.msgCanDeliver(canDeliver);
		canDeliver.clear();
//		if(cashier != null) cashier.msgHereIsBill(BILL, this);
		timer.schedule(new TimerTask() {
			public void run() {
				timerDone();
				stateChanged();
			}
		},
		//Retrieve from the map how long the food takes to cook
		1000 * DELIVER_TIME);
	}
	private void tellCookNoFood(){
//		cook.msgCannotDeliver(cannotDeliver);
		cannotDeliver.clear();
	}
	
	
	/** Utilities */
	public void setCashier(Cashier c){
		cashier = c;
	}
	public void setCook(Cook c){
		cook = c;
	}
	public void setInventory(int amount){
		inventory.put("Krabby Patty", amount);
		inventory.put("Kelp Rings", amount);
		inventory.put("Coral Bits", amount);
		inventory.put("Kelp Shake", amount);
	
	}

	/** Classes */
	enum DeliveryState { PENDING, CAN_DELIVER, CANNOT_DELIVER };
	private class MyDelivery{
		DeliveryState state;
		String type;
		int amount;
		
		MyDelivery(String f, int q){
			type = f;
			amount = q;
			state = DeliveryState.PENDING;
		}
	}
}