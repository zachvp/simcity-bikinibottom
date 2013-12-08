package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Waiter.BreakState;
import agent.Agent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int NWAITERS = 2;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private String name;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<CustomerAgent> waitingCustomers
	= Collections.synchronizedList(new ArrayList<CustomerAgent>());
	private List<WaiterAgent> waiters = Collections.synchronizedList(new ArrayList<WaiterAgent>());
	private CookAgent cook;
	private Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	public HostAgent(String name, RestaurantGui gui, CookAgent cook) {
		super();

		this.name = name;
		
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		this.cook = cook;
	}

	//Get name
	public String getMaitreDName() {
		return name;
	}
	
	//Messages

	public void msgIWantFood(CustomerAgent cust) {
		synchronized (waitingCustomers) {
			waitingCustomers.add(cust);
		}
		stateChanged();
	}

	public void msgWantToGoOnBreak() {
		stateChanged();
	}

	public void msgImTiredOfWaitingByeYourRestaurantSucks(CustomerAgent c) {
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
			synchronized (waiters) {
				for (WaiterAgent waiter : waiters) {
					if (waiter.getBreakState() == WaiterAgent.BreakState.WantBreak) {
						for (WaiterAgent waiter2 : waiters) {
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

			synchronized (tables) {
				for (Table table : tables) {
					if (!table.isOccupied()) {
						synchronized (waitingCustomers) {
							if (!waitingCustomers.isEmpty()) {
								table.setOccupant(waitingCustomers.get(0));
								waiterWithLessCustomers().msgHeWantsFood(
										waitingCustomers.get(0), table);//the action
								waitingCustomers.remove(0);
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}

			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	//Assorted functions
	
	private WaiterAgent waiterWithLessCustomers() {
		WaiterAgent freeWaiter;
		
		synchronized (waiters) {
			freeWaiter = waiters.get(0);
			for (int i = 1; i < waiters.size(); i++) {
				if (freeWaiter.getBreakState() == WaiterAgent.BreakState.OnBreak
						|| (freeWaiter.customerCount() >= waiters.get(i)
								.customerCount() && waiters.get(i)
								.getBreakState() != WaiterAgent.BreakState.OnBreak)) {
					freeWaiter = waiters.get(i);
				}
			}
		}
		
		return freeWaiter;
	}

	public void addWaiter(WaiterAgent waiterAgent) {
		synchronized (waiters) {
			waiters.add(waiterAgent);
		}
		stateChanged();
	}

	public String getName() {
		return name;
	}

	public List<CustomerAgent> getWaitingCustomers() {
		List<CustomerAgent> response;
		
		synchronized (waitingCustomers) {
			response = new ArrayList<CustomerAgent>(waitingCustomers);
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
	
}

