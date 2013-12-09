package restaurant.lucas;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


import restaurant.lucas.gui.WaiterGui;
import restaurant.lucas.interfaces.Cook;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Host;
import restaurant.lucas.interfaces.Waiter;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Waiter Role
 * 
 * @author Jack Lucas
 */
public class WaiterRole extends WorkRole implements Waiter {

	WaiterGui waiterGui;
	
	static final int NTABLES = 3;//a global for the number of tables.


	private String name;
	
	int idleFactor = 0;
	//private Semaphore atTable = new Semaphore(0,true);
	
	private Semaphore active = new Semaphore(0);//overall semaphore
	//private Semaphore atDesk = new Semaphore(0,true);
	
	Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>();

	private Host host;
	private Cook cook;
	
	Timer BreakTimer;
	List<String> menu = new ArrayList<String>();
	Map<String, Double> menuMap = new HashMap<String, Double>();
	
	boolean wantToGoOnBreak = false;
	
	CashierRole cashier;

	enum customerState {waiting, readyToOrder, seated, ordered, reordering, needToReorder, orderIsReady, eating, doneEating, paying, leaving, done};
	public static class MyCustomer{
		MyCustomer(Customer c, int table, customerState cState){
			this.customer = c;
			this.table =table;
			this.state = cState;
			
		}
		
		Customer customer;
		int table;
		double bill;
		// beingSeated, seated, readyToOrder, ordering, orderPlaced, orderCooking, orderReady, orderOnWay, served, exiting, done};
		String choice;
		private customerState state;
	};
	

	List<Dimension> plateAreaLocs = new ArrayList<Dimension>();
	List<MyCustomer> myCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	CookRole MyCook;
	
	@Override
	public void msgAtDestination() {
		active.release();
	}

	@Override
	public void msgSitAtTable(Customer c, int table) {
		myCustomers.add(new MyCustomer(c, table, customerState.waiting)); //start c in waiting
//		if(name.equals("lazy"))
//			wantToGoOnBreak = true;
		stateChanged();	
	}

	@Override
	public void msgSetBreakBit() {
		wantToGoOnBreak = true;
		
	}

	@Override
	public void msgImReadyToOrder(Customer c) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.readyToOrder;
		stateChanged();//change cust state		
	}

	@Override
	public void msgHereIsMyChoice(Customer c, String choice) {
		MyCustomer mc = findCustomer(c);
		mc.choice = choice;
		doDisplayChoice(choice + "");
		active.release();
		
	}

	@Override
	public void msgOutOfFood(int tableNum, String choice) {
		for(MyCustomer mc : myCustomers) {//locate customer through their table number
			if(mc.table == tableNum) {
				Do("dont have this item in stock!");//made it working here
				mc.state = customerState.needToReorder;
//				doGoToTable(mc.customer, mc.table);
//				acquireSemaphore(active);
				
			}
		}
		stateChanged();		
	}

	@Override
	public void msgOrderIsReady(Customer c, String choic, int table, Dimension p) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.orderIsReady; 
		plateAreaLocs.add(p);
//		plateAreas.add(p);//TODO
		stateChanged();		
	}

	@Override
	public void msgReadyToPay(Customer c) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.doneEating;
		stateChanged();		
	}

	@Override
	public void msgHereIsCheck(Customer c, double check) {
		MyCustomer mc = findCustomer(c);
		mc.bill = check;
		active.release();		
	}

	@Override
	public void msgLeavingTable(Customer c) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.leaving;
		stateChanged();		
	}

	@Override
	public void msgGoOnBreak() {
		doGoOnBreak();
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

	@Override
	protected boolean pickAndExecuteAnAction() {

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.waiting) {
					seatCustomer(c.customer, c.table);
					c.state = customerState.seated;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) { 
				if(c.state == customerState.readyToOrder) {

					takeOrder(c);
					c.state = customerState.ordered;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.needToReorder) {
					reTakeOrder(c);
					c.state = customerState.seated;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.orderIsReady) {
					deliverOrder(c.customer, c.table, c.choice);
					c.state=customerState.eating;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.doneEating) {
					getCheck(c);
					c.state = customerState.paying;
					return true;//TODO added return true
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.leaving) {
					callHost(c);
					c.state=customerState.done; 
					return true;
				}	
			}
		}
		
		if(wantToGoOnBreak) {
			requestBreak();
			
		}


		return false;
	}
	
	public void setGui(WaiterGui w) {
		waiterGui = w;
	}
	
	//action

	public void seatCustomer(Customer customer, int table) {
		Dimension dim = tableMap.get(table);
		
		doGoToHost();
		acquireSemaphore(active);
		waiterGui.DoBringToTable(dim.width, dim.height);//animation
		customer.msgFollowMeToTable(this, dim.width, dim.height, menuMap);
		acquireSemaphore(active);
		doGoAway();
	}
	
	private void takeOrder(MyCustomer mc){

		doGoToTable(mc.customer, mc.table);
//		Do("1");
		acquireSemaphore(active);
//		Do("ICAN");
		//doTakeOrder();
//		Do("2");
		mc.customer.msgWhatWouldYouLike();
//		Do("3");
		Do("What would you like customer?" + mc.customer);
//		Do("4");
		acquireSemaphore(active);//released by customer msg
//		Do("5");
		doGoToCook(530, 100);
		acquireSemaphore(active);
		cook.msgHereIsAnOrder(mc.choice, mc.table, mc.customer, this);
		doDisplayChoice("");
		doGoAway();
		
	}
	
	private void reTakeOrder(MyCustomer mc) {
		doGoToTable(mc.customer, mc.table);
		acquireSemaphore(active);
		Do("We are out of that item");
		doGoAway();
//		List<String> newMenu = menu;
		Map<String, Double> newMenuMap = menuMap;
		newMenuMap.remove(mc.choice);
//		newMenu.remove(mc.choice);
		mc.customer.msgOutOfChoice(newMenuMap);
		
		
	}
	

	private void deliverOrder(Customer cust, int table, String choice) {
		Do("Beginning order delivery process");
		doGoToCook(plateAreaLocs.get(0).width, plateAreaLocs.get(0).height);
		acquireSemaphore(active);
		
		cook.msgNullifyPlateArea(plateAreaLocs.get(0).height);
		plateAreaLocs.remove(0);
//		plateAreas.get(0).o = null;
//		plateAreas.remove(0);
		
		
		doGoToTable(cust, table);
		doDisplayChoice(choice);
		acquireSemaphore(active);
		Do("Here is your food " + cust);
		doDisplayChoice("none");
		cust.msgHereIsYourFood();
//		Do("Now i should leave you " + cust);
		doGoAway();//stop creepily watching customer eat
		
	}
	
	private void getCheck(MyCustomer mc) {
		doGoToCashier();
		acquireSemaphore(active);
		cashier.msgComputeBill(mc.customer, mc.choice, this);
		acquireSemaphore(active);
		doGoToTable(mc.customer, mc.table);
		acquireSemaphore(active);
		mc.customer.msgHereIsCheck(cashier, mc.bill);
		doGoAway();
	}

	private void callHost(MyCustomer mc) {
		host.msgLeavingTable(mc.customer);
		myCustomers.remove(mc);
		if(myCustomers.size() == 0)
			host.msgNoCustomers(this);
	}
	
	private void requestBreak() {
		Do("I want to go on break");
		host.msgIdLikeToGoOnBreak(this);
		wantToGoOnBreak = false;
	}

	
	
	//animation
	
	public void doGoToHost() {
		waiterGui.DoGoToHost();
	}
	
	public void doGoAway() {
		waiterGui.DoGoAway(idleFactor);
	}
	
	public void doGoToCashier() {
		waiterGui.DoGoToCashier();
	}
	
	public void doGoToCook(int x, int y) {
		waiterGui.DoGoToCook(x, y);
	}
	
	private void doGoToTable(Customer c, int tableNum) {
		Dimension dim = tableMap.get(tableNum);
		
		waiterGui.DoBringToTable(dim.width, dim.height);
	}
	
	//accessors, utilities
	
	public void doDisplayChoice(String choice) {
		waiterGui.displayChoice(choice);
	}
	
	public MyCustomer findCustomer(Customer c) {
		for(MyCustomer mc : myCustomers) {
			if(mc.customer == c) {
				return mc;
			}
		}
		 return null;
	}
	
	private void doGoOnBreak() {
		Do("Im taking a break");
		waiterGui.DoGoOnBreak();
		BreakTimer.schedule(new TimerTask() {
			
			public void run() {
				returningToWork();
			}
		}, 10000);
	}
	
	public void returningToWork() {
		doGoAway();
		host.msgReturningToWork(this);
	}
	
	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}