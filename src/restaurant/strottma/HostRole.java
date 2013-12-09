package restaurant.strottma;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.strottma.gui.HostGui;
import restaurant.strottma.interfaces.Customer;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Host Role
 * 
 * @author Erik Strottmann
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends WorkRole {
	static final int NTABLES = 4; // a global for the number of tables.
	static final int TABLE_SPACING = 80;
	static final int TABLE_X_OFFSET = 440-400; // how far right and down to bump all tables
	static final int TABLE_Y_OFFSET = 130;
	static final int TABLES_PER_COLUMN = 2;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers
			= Collections.synchronizedList(new ArrayList<Customer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private boolean offWork = false;
	
	private HostGui hostGui = null;
	
	private Semaphore atTable = new Semaphore(0,true);
	
	public HostRole(Person person, CityLocation location) {
		super(person, location);
		
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 0; ix < NTABLES; ix++) {
			int x = (ix / TABLES_PER_COLUMN) * TABLE_SPACING + TABLE_X_OFFSET;
			int y = (ix % TABLES_PER_COLUMN) * TABLE_SPACING + TABLE_Y_OFFSET;
			tables.add(new Table(ix, x, y));//how you add to a collections
		}
	}
	
	@Override
	public void activate() {
		super.activate();
		offWork = false;
	}

	public List<Customer> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection<Table> getTables() {
		return tables;
	}
	
	
	// Messages

	public void msgIWantFood(Customer cust) { // from customer
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgTableIsFree(Table t) { // from waiter
		Do(AlertTag.RESTAURANT, "Table " + t.tableNumber + " is free");
		t.setOccupant(null);
		stateChanged();
	}

	public void msgCanIGoOnBreak(WaiterRole w) { // from waiter
		MyWaiter mw = findWaiter(w);
		mw.state = WState.WANT_BREAK;
		stateChanged();
	}
	
	public void msgOffBreak(WaiterRole w) { // from waiter
		MyWaiter mw = findWaiter(w);
		mw.state = WState.NORMAL;
	}
	
	@Override
	public void msgLeaveWork() {
		offWork = true;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		synchronized (waiters) {
			for (MyWaiter w : waiters) {
				if (w.state == WState.WANT_BREAK) {
					if (workingWaitersCount() > 1) {
						approveBreak(w);
						w.state = WState.BREAK_APPROVED;
						return true;
	
					} else {
						denyBreak(w);
						w.state = WState.NORMAL;
						return true;
					}
				}
			}
		}
		
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						MyWaiter leastBusy = leastBusyWaiter();
						if (leastBusy != null) {
							seatCustomer(waitingCustomers.get(0), table, leastBusy.w);//the action
							return true;//return true to the abstract agent to reinvoke the scheduler.
						}
					}
				}
			}
		}
		
		synchronized (waitingCustomers) {
			for (Customer c : waitingCustomers) {
				if (c.getName().equals(CustomerRole.C_NAME_IMPATIENT)) {
					c.msgNoRoom();
					waitingCustomers.remove(c);
					return true;
				}
			}
		}
		
		if (offWork) { deactivate(); }
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(Customer c, Table t, WaiterRole w) {
		// c.msgSitAtTable(t.x, t.y); // now in waiter
		// Do("Asking waiter to seat customer " + c.getName());
		/*
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// backAtStart = false;
		t.setOccupant(c);
		waitingCustomers.remove(c);
		w.msgSitAtTable(c, t);
	}

	private void approveBreak(MyWaiter w) {
		Do(w.w.getName() + "'s break approved");
		w.w.msgBreakApproved();
	}

	private void denyBreak(MyWaiter w) {
		Do(w.w.getName() + "'s break denied");
		w.w.msgBreakDenied();
	}

	// The animation DoXYZ() routines

	//utilities
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private MyWaiter findWaiter(WaiterRole w) {
		for (MyWaiter mw : waiters) {
			if (mw.w == w) {
				return mw;
			}
		}
		return null;
	}

	private int workingWaitersCount() {
		int i = 0;
		for (MyWaiter w : waiters) {
			if (w.state != WState.BREAK_APPROVED && w.state != WState.ON_BREAK) {
				i++;
			}
		}
		return i;
	}
	
	public MyWaiter leastBusyWaiter() {
		if (waiters.isEmpty()) {
			return null;
		}
		MyWaiter leastBusy = null;
		for (MyWaiter w : waiters) {
			if (w.state == WState.NORMAL // && w.w.isAvailable()
					&& (leastBusy == null || w.w.getHowBusy() < leastBusy.w.getHowBusy())) {
				leastBusy = w;
			}
		}
		return leastBusy;
	}
	public void addWaiter(WaiterRole w) {
		Do(AlertTag.RESTAURANT, "Added " + w);
		
		// assign a wait position
		w.setWaitY(130 + 30*this.waiters.size());
		
		this.waiters.add(new MyWaiter(w));
		stateChanged();
	}

	private enum WState {NORMAL, WANT_BREAK, BREAK_APPROVED, ON_BREAK};

	private class MyWaiter {
		WaiterRole w;
		WState state;

		MyWaiter(WaiterRole w) {
			this.w = w;
			this.state = WState.NORMAL;
		}
	}

	public static class Table {
		Customer occupiedBy;
		int tableNumber;
		int x, y;

		Table(int tableNumber, int x, int y) {
			this.tableNumber = tableNumber;
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		// TODO maybe hosts can go on breaks in v3
		return false;
	}
	
}

