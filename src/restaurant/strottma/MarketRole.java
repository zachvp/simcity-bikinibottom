package restaurant.strottma;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Cook;
import restaurant.strottma.interfaces.Market;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.SingletonTimer;
import agent.PersonAgent;
import agent.Role;

/**
 * Restaurant Market Agent
 * 
 * @author Erik Strottmann
 */
public class MarketRole extends Role implements Market {
	// private MarketGui marketGui = null;
	private Map<String, Integer> inventory = Collections.synchronizedMap(new HashMap<String, Integer>());
	private List<MyDelivery> deliveries = Collections.synchronizedList(new ArrayList<MyDelivery>());
	private Timer timer;
	private double money;
	DecimalFormat df = new DecimalFormat("#.##");
	
	private static final int DELIVERY_TIME = 3;// * 60; // seconds
	
	private Cook cook;
	private Cashier cashier;

	public MarketRole(PersonAgent person, CityLocation location) {
		super(person, location);

		this.timer = SingletonTimer.getInstance();
		
		inventory.put("Steak", 5);
		inventory.put("Chicken", 3);
		inventory.put("Pizza", 5);
		inventory.put("Salad", 0);
		
		this.money = 0.0; // default
	}
	
	// TODO Market shouldn't assume there's only one cook.
	public void setCook(Cook cook) {
		this.cook = cook;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
	
	// Messages
	
	// from Cook
	public void msgLowOnFood(Map<String, Integer> d, int marketNumber) {
		MyDelivery md = new MyDelivery(d, DeliveryState.UNPROCESSED, marketNumber);
		deliveries.add(md);
		stateChanged();
	}
	
	// from timer
	private void msgDeliveryReady(int deliveryNum) {
		MyDelivery d = deliveries.get(deliveryNum);
		d.state = DeliveryState.READY;
		stateChanged();
	}
	
	@Override
	// from Cashier
	public void msgHereIsPayment(double payment) {
		Do("Received payment of $" + df.format(payment) + " from cashier.");
		this.money += payment;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		synchronized (deliveries) {
			for (int i = 0; i < deliveries.size(); i++) {
				MyDelivery d = deliveries.get(i);
				if (d.state == DeliveryState.UNPROCESSED) {
					startDelivery(d, i);
					d.state = DeliveryState.PREPARING;
					return true;
				}
			}
		}
		synchronized (deliveries) {
			for (int i = 0; i < deliveries.size(); i++) {
				MyDelivery d = deliveries.get(i);
				if (d.state == DeliveryState.READY) {
					sendDelivery(d);
					d.state = DeliveryState.DONE;
					return true;
				}
			}
		}
		
		// We have tried all our rules and found nothing to do. So return false
		// to main loop of abstract Agent and wait.
		return false;
	}

	// Actions
	private void startDelivery(MyDelivery delivery, final int deliveryNum) {
		Do("Preparing delivery for cook " + cook.getName());
		Map<String, Integer> cannotDeliver = Collections.synchronizedMap(new HashMap<String, Integer>());
		synchronized (deliveries) {
			for (Map.Entry<String, Integer> entry : delivery.delivery.entrySet()) {
				String key = entry.getKey();
				int deliveryQuantity = entry.getValue();
				int inventoryQuantity = inventory.get(key);
				int quantityDifference = inventoryQuantity - deliveryQuantity;
				
				if (quantityDifference >= 0) {
					// We have enough. Reduce the count in the inventory.
					inventory.put(key, quantityDifference);
				} else {
					// We don't have enough. Reduce the amount in the delivery, and message the Cook.
					delivery.delivery.put(key, inventoryQuantity);
					inventory.put(key, 0);
					cannotDeliver.put(key, (-quantityDifference));
				}
			}
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				msgDeliveryReady(deliveryNum);
			}
		}, DELIVERY_TIME * 1000);
		
		cook.msgCannotDeliver(cannotDeliver, delivery.marketNumber);
	}
	
	private void sendDelivery(MyDelivery delivery) {
		Do("Delivery for cook " + cook.getName() + " ready");
		cook.msgHereIsDelivery(delivery.delivery);
		if (delivery.getCost() > 0) {
			Do("Billing cashier $" + df.format(delivery.getCost()));
			cashier.msgHereIsBill(delivery.getCost(), this);
		}
	}

	// The animation DoXYZ() routines
	/* None, at least for now */

	// Utilities
	/*
	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}
	*/

	private enum DeliveryState {UNPROCESSED, PREPARING, READY, DONE};
	private class MyDelivery {
		Map<String, Integer> delivery;
		DeliveryState state;
		int marketNumber;
		
		public static final double FLAT_RATE = 5.00; // the cost for any item in a delivery
		
		MyDelivery(Map<String, Integer> delivery, DeliveryState state,
				int marketNumber) {
			this.delivery = new HashMap<String, Integer>(delivery);
			this.state = state;
			this.marketNumber = marketNumber;
		}
		
		public double getCost() {
			double cost = 0.0;
			synchronized (delivery) {
				for (Integer quantity : delivery.values()) {
					cost += FLAT_RATE * quantity;
				}
			}
			return cost;
		}
	}
	
}

