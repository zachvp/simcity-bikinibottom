package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurant.vonbeck.gui.HostGui;
import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.gui.RestaurantVonbeckBuilding;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Waiter.BreakState;
import agent.Agent;
import agent.Role;
import agent.WorkRole;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends WorkRole {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int NWAITERS = 2;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private String name;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<CustomerRole> waitingCustomers
	= Collections.synchronizedList(new ArrayList<CustomerRole>());
	public List<WaiterRole> waiters = Collections.synchronizedList(new ArrayList<WaiterRole>());
	private CookRole cook;
	private Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	private CashierRole cashier;
	private boolean leavingWork = false;
	private HostGui hostGui;

	public HostRole(String name, RestaurantGui gui, 
			CookRole cook, CashierRole cashier,
			RestaurantVonbeckBuilding restaurantVonbeckBuilding) {
		super(restaurantVonbeckBuilding);

		this.name = name;
		
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		this.hostGui = new HostGui(this);
		gui.getAnimationPanel().addGui(hostGui);
		
		this.cook = cook;
		this.cashier = cashier;
	}
	
	public void activate() {
		super.activate();
		hostGui.DoGoToWork();
	}

	//Get name
	public String getMaitreDName() {
		return name;
	}
	
	//Messages

	public void msgIWantFood(CustomerRole cust) {
		synchronized (waitingCustomers) {
			waitingCustomers.add(cust);
		}
		stateChanged();
	}

	public void msgWantToGoOnBreak() {
		stateChanged();
	}

	public void msgImTiredOfWaitingByeYourRestaurantSucks(CustomerRole c) {
		synchronized (waitingCustomers) {
			waitingCustomers.remove(c);
		}
	}

	public void msgLeavingTable(Customer cust) {
		synchronized (tables) {
			for (Table table : tables) {
				if (table.getOccupant() == cust) {
					Do(cust + " leaving " + table);
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}



	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		//***FIX MULTIPLE LISTS
		
		


		if (!waiters.isEmpty()) {
			
			/*
			synchronized (waiters) {
				for (WaiterRole waiter : waiters) {
					if (waiter.getBreakState() == WaiterRole.BreakState.WantBreak) {
						for (WaiterRole waiter2 : waiters) {
							if (waiter2 != waiter
									&& waiter2.getBreakState() != BreakState.OnBreak) {
								waiter.msgCanGoOnBreak(true);
								return true;
							}
						}
						waiter.msgCanGoOnBreak(false);
						return true;
					}
				}
			}
			*/
			boolean customersEating = false;
			
			synchronized (tables) {
				for (Table table : tables) {
					if (!table.isOccupied()) {
						synchronized (waitingCustomers) {
							if (!waitingCustomers.isEmpty()) {
								table.setOccupant(waitingCustomers.get(0));
								waiterWithLessCustomers().msgHeWantsFood(
										waitingCustomers.get(0), table);
								waitingCustomers.remove(0);
								return true;
							}
						}
					} else customersEating = true;
				}
			}
			
			if (leavingWork && !customersEating && waitingCustomers.isEmpty()) {
				sendWorkersHome();
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void sendWorkersHome() {
		cook.msgGoHome();
		cashier.msgGoHome();
		for (WaiterRole w : waiters) {
			w.msgGoHome();
		}
		
		leavingWork = false;
		hostGui.DoLeaveWork();
		deactivate();
	}

	//Assorted functions
	
	private WaiterRole waiterWithLessCustomers() {
		WaiterRole freeWaiter;
		
		synchronized (waiters) {
			freeWaiter = waiters.get(0);
			for (int i = 1; i < waiters.size(); i++) {
				if (!freeWaiter.isActive() ||
						freeWaiter.getBreakState() == WaiterRole.BreakState.OnBreak
						|| (freeWaiter.customerCount() >= waiters.get(i)
								.customerCount() && waiters.get(i)
								.getBreakState() != WaiterRole.BreakState.OnBreak)) {
					freeWaiter = waiters.get(i);
				}
			}
		}
		
		if (!freeWaiter.isActive()) {
			try {
				throw new Exception("Customer need to be assigned a waiter,"
						+ " but there are none available.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return freeWaiter;
	}

	public void addWaiter(WaiterRole waiterAgent) {
		synchronized (waiters) {
			waiters.add(waiterAgent);
		}
		stateChanged();
	}

	public String getName() {
		return name;
	}

	public List<CustomerRole> getWaitingCustomers() {
		List<CustomerRole> response;
		
		synchronized (waitingCustomers) {
			response = new ArrayList<CustomerRole>(waitingCustomers);
		}
		
		return waitingCustomers;
	}

	public Collection<Table> getTables() {
		Collection<Table> response;
		
		synchronized (tables) {
			response = new ArrayList<Table>(tables);
		}
		return response;
	}

	@Override
	public boolean isAtWork() {
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		return false;
	}

	@Override
	public void msgLeaveWork() {
		leavingWork = true;
		stateChanged();
	}
	
}

