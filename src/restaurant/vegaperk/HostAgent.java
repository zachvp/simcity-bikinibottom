package restaurant.vegaperk;

import agent.Agent;

import java.awt.Dimension;
import java.util.*;

import restaurant.vegaperk.interfaces.Customer;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 4;//a global for the number of tables.
	private static final int TABLECOLNUM = 2;
	private static final int TABLEROWNUM = 2;
	private static final int TABLESPACING = 100;
	
	int chooseWaiter = -1;//cycles through the list of waiters
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers =
			Collections.synchronizedList(new ArrayList<Customer>());
	private List<MyWaiter> waiters =
			Collections.synchronizedList(new ArrayList<MyWaiter>());
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	private Collection<Table> tables =
			Collections.synchronizedList(new ArrayList<Table>(NTABLES));
	//map stores an index and a dimension containing x & y coordinates
	public Map<Integer, Dimension> tableMap =
			Collections.synchronizedMap(new HashMap<Integer, Dimension>());

	private String name;

	public HostAgent(String name) {
		super();
		
		this.name = name;
		// make some tables
		synchronized(tables){
			for (int ix = 0; ix < NTABLES; ix++) {
				int x = (ix%TABLECOLNUM+1)*TABLESPACING;
				int y = (ix/TABLEROWNUM+1)*TABLESPACING;
				tables.add(new Table(ix, x, y));//how you add to a collections
				Dimension tableCoords = new Dimension(x,y);
				tableMap.put(ix, tableCoords);
			}
		}
	}

	/** Retrievers and setters */
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<Customer> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Map<Integer, Dimension> getTableMap() {
		return tableMap;
	}
	
	/** Messages from other agents */
	public void msgIWantFood(Customer c) {
		waitingCustomers.add(c);
		stateChanged();
	}
	public void msgTableIsFree(int t){
		for(Table table : tables){
			if(table.getTableID() == t){
				table.setUnoccupied();
				Do("Set table unoccupied");
			}
		}
		stateChanged();
	}
	public void msgIWantBreak(Waiter w){
		MyWaiter mw = findWaiter(w);
		Do("wants break");
		mw.state = WaiterState.REQUESTED_BREAK;
		stateChanged();
	}
	public void msgOffBreak(Waiter w){
		MyWaiter mw = findWaiter(w);
		Do("off break");
		mw.state = WaiterState.NONE;
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized(tables){
			for (Table table : tables) {
				if (!waitingCustomers.isEmpty() && !table.isOccupied() && !waiters.isEmpty()) {
					seatCustomer(waitingCustomers.get(0), table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		
		synchronized(waitingCustomers){
			for(Customer c : waitingCustomers){
				if(c.getName().equals("impatient")){
					tellCustomerNoTables(c);
					return true;
				}
			}
		}
		
		synchronized(waiters){
			for(MyWaiter waiter : waiters){
				if(waiter.state == WaiterState.REQUESTED_BREAK){
					Do("schedule break");
					tryToPutOnBreak(waiter);
					return true;
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	/** Actions. Implement the methods called in the scheduler. */
	private void seatCustomer(Customer customer, Table table) {
		findLeastBusyWaiter().Waiter.msgPleaseSeatCustomer(customer, table.getTableID());
		Do("seat at table " + table.getTableID());
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		stateChanged();
	}
	
	private void tellCustomerNoTables(Customer c){
		Do("Can't seat anywhere!");
		c.msgTablesAreFull();
		waitingCustomers.remove(c);
		stateChanged();
	}
	
	private void tryToPutOnBreak(MyWaiter w){
		Do("trying to put on break");
		for(MyWaiter mw : waiters){
			if(mw.state == WaiterState.NONE){
				w.state = WaiterState.ON_BREAK;
				w.Waiter.msgCanGoOnBreak();
				return;
			}
		}
		w.state = WaiterState.NONE;
		w.Waiter.msgDenyBreak();
	}

	/** Utility functions */
	public void addWaiter(Waiter w){
		MyWaiter mw = new MyWaiter(w.getName(), w);
		waiters.add(mw);
		w.msgHomePosition(waiters.size());
		stateChanged();
	}
	private MyWaiter findLeastBusyWaiter(){
		chooseWaiter++;
		while(waiters.get(chooseWaiter%waiters.size()).state == WaiterState.ON_BREAK){
			chooseWaiter++;
		}
		return waiters.get(chooseWaiter%waiters.size());
	}
	private MyWaiter findWaiter(Waiter w){
		synchronized(waiters){
			for(MyWaiter temp : waiters){
				if(temp.Waiter == w){
					return temp;
				}
			}
		}
		return null;
	}
	
	private enum WaiterState {NONE, REQUESTED_BREAK, ON_BREAK};
	private class MyWaiter{
		Waiter Waiter;
		WaiterState state;
		
		MyWaiter(String n, Waiter w){
			Waiter = w;
			state = WaiterState.NONE;
		}
	}

	private class Table {
		Customer occupiedBy;
		int tableID;
		int tableX;
		int tableY;

		Table(int tableNumber, int tx, int ty) {
			this.tableID = tableNumber;
			this.tableX = tx;
			this.tableY = ty;
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
			return "table " + tableID;
		}
		
		public int getTableCount(){
			return NTABLES;
		}
		public int getTableID(){
			return tableID;
		}
		public int getX(){
			return tableX;
		}
		public int getY(){
			return tableY;
		}
	}
}

