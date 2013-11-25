package restaurant.strottma;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import restaurant.strottma.HostRole.Table;
import restaurant.strottma.gui.WaiterGui;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Cook.GrillOrPlate;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Waiter Role
 * 
 * @author Erik Strottmann
 */
public class WaiterRole extends WorkRole implements Waiter {
	
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	private Semaphore multiStepAction = new Semaphore(0,true);
	private HostRole host;
	private CookRole cook;
	private Cashier cashier;

	Timer timer = new Timer();
	private int breakDelay = 15 * 1000;
	DecimalFormat df = new DecimalFormat("#.##");
	
	private int waitX = 200, waitY; // default position
	public void setWaitY(int y) { this.waitY = y; }
	public int getWaitX() { return this.waitX; }
	public int getWaitY() { return this.waitY; }

	public WaiterGui waiterGui = null;

	private enum BreakState {NORMAL, WANT_BREAK, BREAK_PENDING, BREAK_APPROVED,
			BREAK_DENIED, ON_BREAK, BREAK_DONE};

	private BreakState bState;
	
	int shiftStartHour;
	int shiftStartMinute;
	int shiftEndHour;
	int shiftEndMinute;

	public WaiterRole(Person person, CityLocation location) {
		super(person, location);

		this.bState = BreakState.NORMAL;
		
		this.shiftStartHour = 8; // 08:00
		this.shiftStartMinute = 0;
		this.shiftEndHour = 20; // 20:00
		this.shiftEndMinute = 0;
	}
	
	public void setOthers(HostRole host, CookRole cook, Cashier cashier) {
		this.host = host;
		this.cook = cook;
		this.cashier = cashier;
	}
	
	@Override
	public String toString() {
		return "waiter " + getName();
	}
	
	public HostRole getHost() {
		return host;
	}
	
	public int getHowBusy() {
		return customers.size();
	}
	
	public boolean isAvailable() {
		return true; // available if he exists!
	}
	
	// Messages
	
	public void msgAtDestination() { // from animation
		// print("msgAtDestination() called");
		multiStepAction.release();
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
	}
	
	public void msgSitAtTable(Customer c, Table t) { // from host
		customers.add( new MyCustomer(c, t, CState.WAITING) );
		stateChanged();
	}
	
	public void msgIAmReadyToOrder(Customer c) { // from customer
		MyCustomer mc = findCustomer(c);
		mc.state = CState.READY_TO_ORDER;
		stateChanged();
	}
	
	public void msgHereIsMyChoice(Customer c, String choice) { // from customer
		MyCustomer mc = findCustomer(c);
		mc.choice = choice;
		multiStepAction.release();
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
	}

	public void msgOutOfChoice(Customer c, Table t, String choice) {
		MyCustomer mc = findCustomer(c);
		mc.state = CState.OUT_OF_CHOICE;
		stateChanged();
	}
	
	public void msgOrderIsReady(Customer c, Table t, String choice, GrillOrPlate plateArea) { // from cook
		MyCustomer mc = findCustomer(c);
		mc.state = CState.ORDER_READY;
		mc.plateArea = plateArea;
		stateChanged();
	}
	
	public void msgDoneEating(Customer c) { // from customer
		MyCustomer mc = findCustomer(c);
		mc.state = CState.DONE_EATING;
		stateChanged();
	}
	
	public void msgHereIsBill(Customer c, double bill) { // from cashier
		MyCustomer mc = findCustomer(c);
		mc.bill = bill;
		multiStepAction.release();
	}
	
	public void msgLeaving(Customer c) { // from customer
		MyCustomer mc = findCustomer(c);
		mc.state = CState.LEAVING;
		stateChanged();
	}

	public void msgGotFatigued() { // from gui
		bState = BreakState.WANT_BREAK;
		stateChanged();
	}

	public void msgBreakApproved() { // from host
		bState = BreakState.BREAK_APPROVED;
		stateChanged();
	}

	public void msgBreakDenied() { // from host
		bState = BreakState.BREAK_DENIED;
		waiterGui.DoGoOffBreak();
		stateChanged();
	}

	public void msgEndBreak() { // from gui
		bState = BreakState.BREAK_DONE;
		multiStepAction.release();
	}
	
	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try {
			if (bState == BreakState.WANT_BREAK) {
				askToGoOnBreak();
				bState = BreakState.BREAK_PENDING;
				return true;
	
			} else if (bState == BreakState.BREAK_APPROVED
					&& currentCustomersCount() == 0) {
	
				bState = BreakState.ON_BREAK;
				goOnBreak();
				return true;
	
			} else if (bState == BreakState.BREAK_DENIED) {
				bState = BreakState.NORMAL;
				cannotGoOnBreak();
				return true;
				
			} else if (bState == BreakState.BREAK_DONE) {
				bState = BreakState.NORMAL;
				finishBreak();
				return true;
			}
	
			for (MyCustomer c : customers) {
				if (c.state == CState.OUT_OF_CHOICE) {
					askCustomerToReorder(c);
					c.state = CState.SEATED;
					return true;
				}
			}
	
			// high priority - release empty tables
			for (MyCustomer c : customers) {
				if (c.state == CState.LEAVING) {
					clearTable(c);
					c.state = CState.DONE;
					return true;
				}
			}
			
			for (MyCustomer c : customers) {
				if (c.state == CState.WAITING) {
					seatCustomer(c);
					c.state = CState.SEATED;
					return true;
				}
			}
			
			for (MyCustomer c : customers) {
				if (c.state == CState.READY_TO_ORDER) {
					takeOrder(c);
					c.state = CState.ORDERED;
					return true;
				}
			}
			
			for (MyCustomer c : customers) {
				if (c.state == CState.ORDER_READY) {
					deliverOrder(c);
					c.state = CState.EATING;
					return true;
				}
			}
			
			for (MyCustomer c : customers) {
				if (c.state == CState.DONE_EATING) {
					getBill(c);
					c.state = CState.PAYING;
				}
			}
		} catch (ConcurrentModificationException e) {
			return true;
		}
		
		DoLeaveCustomer();
		
		// We have tried all our rules and found nothing to do. So return false
		// to main loop of abstract Role and wait.
		return false;
	}

	
	// Actions

	private void seatCustomer(MyCustomer c) {
		Do("Seating customer " + c.c.getName());
		DoGoToCustWaitArea();
		acquire(multiStepAction); // wait until the waiter is at the host
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
		
		Menu menu = new Menu();
		c.menu = menu;
		c.c.msgFollowMeToTable( this, menu, c.table.getX(), c.table.getY() );
		DoGoToTable(c.table); // animation
		acquire(multiStepAction); // wait until customer is seated
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
		
		DoLeaveCustomer();
	}
	
	private void takeOrder(MyCustomer c) {
		DoGoToTable(c.table); // animation
		acquire(multiStepAction); // wait until the waiter is at table
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
		
		// DoTakeOrder(); // animation - disabled for now
		c.c.msgWhatWouldYouLike();
		acquire(multiStepAction); // wait until customer orders
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
		
		DoGoToCook(c.menu.getShortName(c.choice)); // animation
		acquire(multiStepAction); // wait until waiter is at kitchen
		// print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
		
		cook.msgHereIsAnOrder(this, c.c, c.table, c.choice);
	}

	private void askCustomerToReorder(MyCustomer c) {
		DoGoToTable(c.table); // animation
		acquire(multiStepAction); // wait until the waiter is at table

		Menu menu = c.c.getMenu();
		menu.remove(c.choice);
		c.c.msgOutOfChoice(menu);

		DoLeaveCustomer();
	}
	
	private void deliverOrder(MyCustomer c) {
	    DoGoToCook(); // animation
	    acquire(multiStepAction); // wait until waiter is at kitchen
	    // print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
	    
	    // Remove the text from the plate area
	    c.plateArea.hideOrder();
	    c.plateArea.removeOrder();
	    
	    DoGoToTable(c.table, c.menu.getShortName(c.choice)); // animation
	    acquire(multiStepAction); // wait until the waiter is at table
	    // print("multiStepAction has " + multiStepAction.availablePermits() + " permits");
	    
	    c.c.msgHereIsYourFood();
	    
		DoLeaveCustomer();
	}
	
	private void clearTable(MyCustomer c) {
		// DoClearTable(c.table); // animation - disabled (for now?)
		// Do("Table " + c.table.tableNumber + " is free");
	    host.msgTableIsFree(c.table);
	}
	
	private void getBill(MyCustomer c) {
		Do("Getting " + c.c.getName() + "'s bill from cashier");
		DoGoToCashier(); // animation
		acquire(multiStepAction); // wait
		
		// Do("At cashier. Asking for bill.");
		cashier.msgDoneEating(c.c, this, new Menu().getPrice(c.choice));
		acquire(multiStepAction);
		
		// TODO: fix this hack
		Do("Bringing $" + df.format(c.bill) + " bill to " + c.c.getName());
		DoGoToTable(c.table); // animation
		acquire(multiStepAction); // wait
		
		c.c.msgHereIsBill(cashier, c.bill);
		
		DoLeaveCustomer(); // animation
	}

	private void askToGoOnBreak() {
		Do("Asking to go on break");
		host.msgCanIGoOnBreak(this);
	}

	private void goOnBreak() {
		Do("Going on break");

		DoGoOnBreak(); // animation
		
		/*
		timer.schedule(new TimerTask() {
			public void run() {
				msgEndBreak();
			}
		},
		breakDelay); //how long to wait before running task
		*/

		acquire(multiStepAction);
	}

	private void cannotGoOnBreak() {
		Do("Cannot go on break");
		DoGoOffBreak(); // animation
	}
	
	private void finishBreak() {
		Do("Off of break");
		DoGoOffBreak(); // animation
		host.msgOffBreak(this);
	}
	

	// The animation DoXYZ() routines
	
	private void DoGoToTable(Table table) {
		//Notice how we print "table" directly. Its toString method will do it.
		// Do("Going to " + table);
		waiterGui.DoGoToTable(table); 
	}
	
	private void DoGoToTable(Table table, String choice) {
		// Do("Bringing " + choice + " to " + table);
		waiterGui.DoGoToTable(table, choice);
	}
	
	private void DoGoToHost() {
		// Do("Going to host");
		waiterGui.DoGoToHost();
	}
	
	private void DoGoToCustWaitArea() {
		// Do("Going to customer waiting area");
		waiterGui.DoGoToCustWaitArea();
	}
		
	private void DoGoToCook() {
		// Do("Going to cook");
		waiterGui.DoGoToCook();
	}

	private void DoGoToCook(String choice) {
		// Do("Bringing order to cook");
		waiterGui.DoGoToCook(choice);
	}
	
	private void DoLeaveCustomer() {
		// Do("Leaving customer");
		waiterGui.DoLeaveCustomer();
	}
	
	private void DoGoOnBreak() {
		// Do("Going on break");
		waiterGui.DoGoOnBreak();
	}
	
	private void DoGoOffBreak() {
		// Do("Going off break");
		waiterGui.DoGoOffBreak();
	}
	
	private void DoGoToCashier() {
		// Do("Going to cashier");
		waiterGui.DoGoToCashier();
	}

	
	// Utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public boolean isOnBreak() {
		return bState == BreakState.ON_BREAK || bState == BreakState.BREAK_APPROVED;
	}
	
	/**
	 * Returns the MyCustomer corresponding to c, or null if none exists.
	 */
	private MyCustomer findCustomer(Customer c) {
		for (MyCustomer mc : customers) {
			if (mc.c == c) {
				return mc;
			}
		}
		return null;
	}
	
	/**
	 * Convenience for try-catch block around Semaphore.acquire().
	 */
	private void acquire(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int currentCustomersCount() {
		int i = 0;
		for (MyCustomer c : customers) {
			if (c.state != CState.DONE) {
				i++;
			}
		}
		return i;
	}
	
	enum CState {WAITING, SEATED, READY_TO_ORDER, ORDERED, OUT_OF_CHOICE,
			ORDER_READY, EATING, DONE_EATING, PAYING, LEAVING, DONE};
	
	private class MyCustomer {
		Customer c;
		Table table;
		CState state;
		String choice;
		Menu menu;
		double bill;
		GrillOrPlate plateArea;
		
		MyCustomer(Customer c, Table t, CState state) {
			this.c = c;
			this.table = t;
			this.state = state;
		}

	};
	
	public class Menu {
		
		List<Choice> choices = new ArrayList<Choice>();
		
		public Menu() {
			choices.add( new Choice("Steak",   "ST", 15.99) );
			choices.add( new Choice("Chicken", "CK", 10.99) );
			choices.add( new Choice("Salad",   "SA", 5.99) );
			choices.add( new Choice("Pizza",   "PZ", 8.99) );
		}
		
		public Choice get(int i) {
			return choices.get(i);
		}
		
		// by long name
		public Choice get(String s) {
			for (Choice c : choices) {
				if (c.name.equals(s)) {
					return c;
				}
			}
			return null;
		}
		
		public String getShortName(String name) {
			Choice c = get(name);
			return c.shortName;
		}
		
		public double getPrice(String name) {
			Choice c = get(name);
			return c.price;
		}

		public void remove(String name) {
			for (int i = 0; i < choices.size(); i++) {
				if (choices.get(i).name.equals(name)) {
					choices.remove(i);
				}
			}
		}
		
		public class Choice {
			final String name;
			final String shortName;
			double price;
			
			public Choice(String name, String shortName, double price) {
				this.name = name;
				// this.shortName = shortName;
				this.shortName = name.substring(0, 2);
				this.price = price;
			}
		}
		
	}

	@Override
	public int getShiftStartHour() {
		return shiftStartHour;
	}

	@Override
	public int getShiftStartMinute() {
		return shiftStartMinute;
	}

	@Override
	public int getShiftEndHour() {
		return shiftEndHour;
	}

	@Override
	public int getShiftEndMinute() {
		return shiftEndMinute;
	}

	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}
	
}

