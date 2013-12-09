package restaurant.lucas;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.lucas.gui.WaiterGui;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
//import restaurant.HostAgent.Table;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterRole extends WorkRole implements Waiter {
	static final int NTABLES = 3;//a global for the number of tables.


	private String name;
	
	int idleFactor = 0;
	//private Semaphore atTable = new Semaphore(0,true);
	
	private Semaphore active = new Semaphore(0);//overall semaphore
	//private Semaphore atDesk = new Semaphore(0,true);
	
	Map<Integer, Dimension> tableMap = new HashMap<Integer, Dimension>();

	public WaiterGui waiterGui = null;
	private HostRole host;
	private CookRole cook;
	
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
	
	boolean atWork;
	///private static enum waiterState {doingNothing, seatingCustomer, aboutToTakeOrder, takingOrder, bringingOrderToCook, pickingUpOrder, deliveringOrder};
	//private static enum Event {seatCustomer, seatedCustomer, aboutToTakeOrder, arrivedAtTable, orderPlaced, orderBroughtToCook, orderIsReady, orderPickedUp, orderDelivered, customerExited};
	///private waiterState state;
	
	public WaiterRole(String name, HostRole h, CookRole c, CashierRole cashier, int idle) {
		super();
		host = h;
		cook = c;
		this.cashier = cashier;
		this.name = name;
		idleFactor = idle;
	
		
		this.BreakTimer= new Timer();
		
		tableMap = h.getTableMap();
		menuMap.put("Tofu",  15.99);
		menuMap.put("Rice", 10.99);
		menuMap.put("Sushi",  5.99);
		menuMap.put("Noodles",  8.99);
		
		menu.add("Tofu");
		menu.add("Rice");
		menu.add("Sushi");
		menu.add("Noodles");
//		// make some tables
//		tables = new ArrayList<Table>(NTABLES);
//		for (int ix = 0; ix <= NTABLES; ix++) {
//			tables.add(new Table(ix, (ix%4 + 1) * 100, (ix/4 + 1) * 100  ));//how you add to a collections
//		}
	}
	
	public WaiterRole(Person p, CityLocation c) {
		super(p, c);
		
		atWork = false;
		this.BreakTimer= new Timer();
		
		Dimension t1 = new Dimension(100, 100);
		Dimension t2 = new Dimension(200, 100);
		Dimension t3 = new Dimension(300, 100);
		Dimension t4 = new Dimension(400, 100);
		tableMap.put(0, t1);
		tableMap.put(1, t2);
		tableMap.put(2, t3);
		tableMap.put(3, t4);
		
		menuMap.put("Tofu",  15.99);
		menuMap.put("Rice", 10.99);
		menuMap.put("Sushi",  5.99);
		menuMap.put("Noodles",  8.99);
		
		menu.add("Tofu");
		menu.add("Rice");
		menu.add("Sushi");
		menu.add("Noodles");
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages ////////////////////#################################################################
	//#########################################################################################
	public void msgAtDestination()//from gui, when at destination
	{
//		Do("before release");
		active.release();
//		Do("released Waiter");
		//MyCustomers.add(new MyCustomer(c, t, waiting));
		//stateChanged();
	}
	
	public void msgSitAtTable(Customer c, int table){
		myCustomers.add(new MyCustomer(c, table, customerState.waiting)); //start c in waiting
//		if(name.equals("lazy"))
//			wantToGoOnBreak = true;
		stateChanged();	
		
	}
	
	public void msgSetBreakBit() {
		wantToGoOnBreak = true;
	}
	
	public void msgImReadyToOrder(Customer c){
//		active.release();
		
		
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.readyToOrder;
		stateChanged();//change cust state

	}
	
	public void msgHereIsMyChoice(Customer cust, String choice) {
		MyCustomer mc = findCustomer(cust);
		mc.choice = choice;
		doDisplayChoice(choice + "");
		active.release();
	}
	
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
	
	public void msgOrderIsReady(Customer c, String choice, int table, Dimension plateDim) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.orderIsReady; 
		plateAreaLocs.add(plateDim);
//		plateAreas.add(p);//TODO
		stateChanged();
	}

	
	public void msgReadyToPay(Customer c) {
		MyCustomer mc = findCustomer(c);
		mc.state = customerState.doneEating;
		stateChanged();
		//stub for v2.1
	}
	
	
	public void msgHereIsCheck(Customer c, double check) {
	
		MyCustomer mc = findCustomer(c);
		mc.bill = check;
		active.release();
		
		
	}
	
	public void msgLeavingTable(Customer cust) {
		MyCustomer mc = findCustomer(cust);
		mc.state = customerState.leaving;
		stateChanged();

	}
	
	public void msgGoOnBreak() {
		doGoOnBreak();//INVOKING ACTION OUTSIDE OF MESSAGE BAD
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(!atWork) {
			goToWork();
			return true;
		}
		
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

	// Actions ///////////////////////////##################################################

	private void goToWork() {

		doGoToWork();
		atWork = true; 
	}
	
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
//		plateAreas.get(0).o = null;//TODO
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
	

	
	// The animation DoXYZ() routines
	private void doGoToWork() {
		atWork = true;
		waiterGui.DoGoToDesk(idleFactor);
		acquireSemaphore(active);
	}
	
	private void doGoToTable(Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		
		//print("Seating " + customer + " at " + table);
		Dimension dim = tableMap.get(table);
		
		waiterGui.DoBringToTable(dim.width, dim.height); 

	}
	
	private void doGoToCook(int x, int y) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		
//		print("Going To Cook");
		waiterGui.DoGoToCook(x, y); 

	}
	
	private void doGoToCashier() {
		waiterGui.DoGoToCashier();
		
	}
	
	private void requestBreak() {
		Do("I want to go on break");
		host.msgIdLikeToGoOnBreak(this);
		wantToGoOnBreak = false;
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

	//utilities
	
	public void returningToWork() {
		doGoAway();
		host.msgReturningToWork(this);
	}
	
	public boolean getWantToGoOnBreak() {
		return wantToGoOnBreak;
	}

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
	
	public void acquireSemaphore(Semaphore sem) {
		try {
//			Do("acquired waiter");
			sem.acquire();
			
		} catch (InterruptedException e) {
			// 
			e.printStackTrace();
		}
	}
	
	public void doGoToHost() {
		waiterGui.DoGoToHost();
	}
	
	public void setIdlePosition(int factor) {
		idleFactor = factor;
	}
	
	public void doGoAway() {
//		Do("Idle");
		waiterGui.DoGoAway(idleFactor);
	}
	
	
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
//		waiterGui.DoGoAway(idleFactor);
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void pauseAllCustomers() { //called from restaraunt panel
		for(MyCustomer mc : myCustomers) {
//			mc.customer.pause();//TODO fix pause for interfaces ASKKK
		}
	}
	
	public void restartAllCustomers() {//called from restaraunt panel
		for(MyCustomer mc : myCustomers) {
//			mc.customer.restart();
		}
	}
	
	

//
	
	private class Menu {
		
	}



	@Override
	public boolean isAtWork() {
		return isActive();
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

	public void setOtherRoles(HostRole host, CookRole cook,
			CashierRole cashier) {
		this.host = host;
		this.cook = cook;
		this.cashier = cashier;
	}







	
}

