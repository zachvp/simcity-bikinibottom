package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.gui.WaiterGui;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Waiter;
import agent.Agent;



public class WaiterAgent extends Agent implements Waiter {
	private static final long BREAK_MS = 12000;
	private String name;
	private Semaphore animating = new Semaphore(0,true);
	private List<WaiterAction> actionQueue = Collections.synchronizedList(new ArrayList<WaiterAction>());
	private Map<String, Integer> menuO = Collections.synchronizedMap(new HashMap<String, Integer>());
	private HostAgent host;
	private Cashier cashier;
	private int customerCount = 0;
	private WaiterGui waiterGui;
	private CookAgent cook;
	private RestaurantGui gui;
	
	private BreakState breakState = BreakState.Working;
	Timer timer = new Timer();
	//Constructor
	public WaiterAgent(HostAgent h, CashierAgent cash, CookAgent c, RestaurantGui g, String name, int waiterNum) {
		host = h;
		cashier = cash;
		cook = c;
		gui = g;
		waiterGui = new WaiterGui(this, waiterNum);
		this.name = name;
		menuO.put("Steak", 1599);
		menuO.put("Chicken", 1099);
		menuO.put("Salad", 599);
		menuO.put("Pizza", 899);
		g.getAnimationPanel().addGui(waiterGui);
		startThread();
	}
	
	
	//Custom inner classes
	
	private abstract class WaiterAction {
		public Customer cust;
		public Table table;
		public WaiterAgent waiter;
		public Order order;
		public HostAgent host;
		public Map<String, Integer> menu;
		public int numData;

		public abstract void run();
		
	}
	
	//Messages
	
	public void msgHeWantsFood(CustomerRole c, Table t) {
		customerCount++;
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter.BackToHost();
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cust.msgSitAtTable(table.tableNumber,waiter,new HashMap<String,Integer>(menu));
				DoSeatCustomer(cust, table);
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		action.cust = c;
		action.table = t;
		action.waiter = this;
		action.menu = menuO;
		actionQueue.add(action);
		stateChanged();
	}
	
	

	public void msgIWantToOrder(CustomerRole c) {
		WaiterAction action = new WaiterAction() {
			public void run(){
				// TODO is this illegal messaging?
				waiter.GoToCustomer(cust);
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String food = cust.getOrder();
				Order o = new Order(food, cust, waiter);
				waiter.GoToCook();
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				waiter.cook.msgCook(o);
			}
		};
		action.cust = c;
		action.waiter = this;
		actionQueue.add(action);
		stateChanged();
	}

	public void msgOrderReady(Order o) {
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter.GoToCook();
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cook.removePlate();
				waiter.displayFood(order.food);
				waiter.GoToCustomer(order.customer);
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				order.customer.msgHeresYourFood(order.food);
				waiter.displayFood("");
			}
		};
		action.order = o;
		action.waiter = this;
		actionQueue.add(action);
		stateChanged();
	}

	public void msgReadyToPay(CustomerRole c, String food) {
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter = (WaiterAgent) order.waiter;
				waiter.GoToCashier();
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cashier.msgNeedBill(order);
			}
		};
		
		action.order = new Order(food, c, this);
		actionQueue.add(action);
		stateChanged();
	}



	public void msgBill(int price, Customer c) {
		
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter.GoToCustomer(cust);
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cust.msgBill(numData, waiter.cashier);
			}
		};
		action.waiter = this;
		action.cust = c;
		action.numData = price;
		actionQueue.add(action);
		stateChanged();
		
	}



	public void msgImLeaving(CustomerRole c) {
		customerCount--;
		
		WaiterAction action = new WaiterAction() {
			public void run(){
				host.msgLeavingTable(cust);
			}
		};
		action.cust = c;
		action.host = host;
		actionQueue.add(action);
		stateChanged();
	}



	public void msgOutOfThis(Order o) {
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter = (WaiterAgent) order.waiter;
				waiter.displayFood("Out of " + order.food);
				waiter.GoToCustomer(order.customer);
				try {
					animating.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				waiter.displayFood("");
				order.customer.msgOutOfFood(order.food);
			}
		};
		action.order = o;
		action.menu = menuO;
		actionQueue.add(action);
		stateChanged();
		
	}

	public void msgCanGoOnBreak(boolean answer) {
		if (!answer) {
			Do("Host denied break");
			breakState = BreakState.Working;
			guiImWorking();
		} else {
			Do("Host approved break");
			breakState = BreakState.OnBreak;
			//Timer to go back to work
			timer.schedule(new TimerTask() {
				public void run() {
					breakState = BreakState.Working;
					guiImWorking();
					stateChanged();
				}
			},
			BREAK_MS);
		}
	}



	public void msgGUIGoOnBreak() { //CHECK THAT BREAKSTATE IS WORKING!
		WaiterAction action = new WaiterAction() {
			public void run(){
				waiter.breakState = BreakState.WantBreak;
				host.msgWantToGoOnBreak();
			}
		};
		action.host = host;
		action.waiter = this;
		actionQueue.add(action);
		stateChanged();
		
	}
	
	public void msgAnimationDone() {//from animation
		//print("msgAtTable() called");
		//returning = true;
		animating.release();// = true;
		//stateChanged();
	}

	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		
		try {

			if (!actionQueue.isEmpty() && breakState != BreakState.OnBreak) {
				WaiterAction currAction;
				synchronized (actionQueue) {
					currAction = actionQueue.get(0);
					actionQueue.remove(0);
				}
				currAction.run();
				return true;
			}

			waiterGui.DoGoToCenter();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	//Actions
	
	private void DoSeatCustomer(Customer cust, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		Do("Seating " + cust + " at " + table);
		waiterGui.DoBringToTable(cust, table.tableNumber); 

	}


	protected void GoToCook() {
		waiterGui.DoGoToCook();
		
	}

	protected void GoToCustomer(Customer customer) {
		waiterGui.DoGoToCustomer(customer);
	}


	protected void GoToCashier() {
		waiterGui.DoGoToCashier();			
	}


	protected void BackToHost() {
			waiterGui.DoGoToHost();
	}

	protected void displayFood(String food) {
		waiterGui.DoDisplayFood(food);
	}

	//Getters / setters
	
	public int customerCount() {
		return customerCount;
	}

	public String getName() {
		return name;
	}


	public WaiterGui getGui() {
		return waiterGui;
	}

	public BreakState getBreakState() {
		return breakState;
	}

	public void setBreakState(BreakState breakState) {
		this.breakState = breakState;
	}

	protected void guiImWorking() {
		gui.setWaiterEnabled(this);

	}



}
