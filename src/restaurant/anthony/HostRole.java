package restaurant.anthony;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.ScheduleTask;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.anthony.CustomerRole.AgentState;
import restaurant.anthony.gui.HostGui;
import restaurant.anthony.interfaces.Cook;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
// A host agent that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class HostRole extends WorkRole {
	static final int NTABLES = 3;// a global for the number of tables.
	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	public List<CustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<CustomerRole>());
	public List<Table> tables;
	
	ScheduleTask task = ScheduleTask.getInstance();
	private Semaphore atHome = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);

	public List<WaiterRoleBase> Waiters = Collections.synchronizedList(new ArrayList<WaiterRoleBase>());
	public Cook cook;
	public CashierRole cashier;
	private int assignedSpace = 0;

	public enum AgentState {
		Idle, OffWork, NotAtWork
	};
	
	//public enum AgentEvent {AtWork, NotAtWork};

	private AgentState state = AgentState.NotAtWork;
	//private AgentEvent event = AgentEvent.NotAtWork;
	
	public HostGui hostGui = null;

	public HostRole(Person person, CityLocation location) {
		super(person, location);

		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));// how you add to a collections
		}
		
		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgLeaveWork();
			
			}
		};
		
		
		
		
		int hour = super.getShiftEndHour();
		int minute = super.getShiftEndMinute();
		
		task.scheduleDailyTask(command, hour, minute);
	}

	public String getMaitreDName() {
		return person.getName();
	}

	public String getName() {
		return person.getName();
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public List getTables() {
		return tables;
	}

	// Messages
	public void AtWork() {
		state = AgentState.Idle;
		//event = AgentEvent.AtWork;
		atHome.release();
		stateChanged();
		
	}

	
	public void msgIAmTakingCust(){
		if (waitingCustomers.size() != 0){
			for (int i=0;i<waitingCustomers.size();i++){
						waitingCustomers.get(i).msgYouAreInLine(i);
			}
		}
		
	}
	
	public void RequestABreak(Waiter waiter) {
		int B = 0;
		if (Waiters.size() == 1) {
			// No Break
			//print("No Break, only one waiter");
			waiter.GoBreak(false);
			return;
		}
		synchronized(Waiters){
		for (int i = 0; i < Waiters.size(); i++) {
			if (Waiters.get(i).IsOnBreak()) {
				B++;
			}
		}
		}
		synchronized(Waiters){
		if (B == Waiters.size() - 1) {
			// Only 1 waiter is active
			//print("No Break only 1 waiter is active");
			waiter.GoBreak(false);
			return;
		}
		}
		// Go Break!
		waiter.GoBreak(true);
		//print("Permission Given to " + waiter.getName());
		return;
	}

	public void msgIWantFood(CustomerRole cust) {
		//print("Customer : " + cust.getName() + " wants food");
		waitingCustomers.add(cust);
		
		//Experimenting
			cust.msgYouAreInLine(waitingCustomers.size()-1);
			
		stateChanged();
	}

	public void msgTableIsClear(int t) {
		tables.get(t - 1).setUnoccupied();
		stateChanged();
	}
	
	public void msgAtHome(){
		state = AgentState.Idle;
		atHome.release();
		stateChanged();
	}
	
	@Override
	public void msgLeaveWork() {
		state = AgentState.OffWork;
		stateChanged();
		
	}
	
	public void AtExit(){
		atExit.release();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		if (state == AgentState.NotAtWork){
			GoToWork();
			return true;
		}
		/*
		 * Think of this next rule as: Does there exist a table and customer, so
		 * that table is unoccupied and customer is waiting. If so seat him at
		 * the table.
		 */

		/*
		 * **Algorithm for Assigning Waiting Customer to waiter** First of all,
		 * Check if the WaitingCustomer List and WaiterList are empty or not
		 * Pick the first waiter among the waiter list Check if there is an
		 * unoccupied table Assign the Next WaitingCustomer to the waiter who
		 * has the smallest group of customers (By keeping track of the lowest
		 * customer number's waiter and compare the whole list of waiter until
		 * we find the waiter with the lowest number of customers)
		 */
		WaiterRoleBase w = null;
		//if (state == AgentState.Idle){
			while (!waitingCustomers.isEmpty())

				if (!Waiters.isEmpty()) {

					// Find The Waiter that is not on Break
					synchronized(Waiters){
						for (int i = 0; i < Waiters.size(); i++) {
							if (w == null && !Waiters.get(i).IsOnBreak()) {
								w = Waiters.get(i);
								// print (w.getName() + " Is selected first");
							}
						}
					}

					for (Table table : tables) {
						if (!table.isOccupied()) {
							synchronized(Waiters){
								for (int i = 0; i < Waiters.size(); i++) {
									if (!Waiters.get(i).IsOnBreak() && Waiters.get(i).isAtWork()) {
										if (w.MyCustomers.size() < Waiters.get(i).MyCustomers
												.size())
											continue;
										else {
											w = Waiters.get(i);
											continue;
										}

									}
								}
							}
							// print (w.getName() + " Is finally selected");
							waitingCustomers.get(0).msgGoToAssignedSpace(assignedSpace%3);
							assignedSpace++;
							waitingCustomers.get(0).setWaiter(w);
							w.SitAtTable(waitingCustomers.get(0), table.tableNumber);// the
							// action
							table.occupiedBy = waitingCustomers.get(0);

							waitingCustomers.remove(waitingCustomers.get(0));
							return true;// return true to the abstract agent to
							// reinvoke the scheduler.

						}
					}
				}
		//}
		
		if (state == AgentState.OffWork && waitingCustomers.size() == 0){
			OffWork();
		}

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions
	private void OffWork(){
		DoMsgAllWorkersOffWork();
		hostGui.GoToExit();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.deactivate();
		state = AgentState.NotAtWork;
	}
	
	private void DoMsgAllWorkersOffWork(){
		for (int i=0;i<Waiters.size();i++){
			Waiters.get(i).msgLeaveWork();
		}
		cashier.msgLeaveWork();
		cook.msgLeaveWork();
	}
	
	private void GoToWork(){
		hostGui.GoToWork();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void AddWaiter(WaiterRoleBase Wa) {
		Waiters.add(Wa);
		stateChanged();
	}

	// utilities
	public boolean AllTablesOccupied(){
		int tempSize = 0;
		for (int i=0;i<tables.size();i++){
			if (tables.get(i).isOccupied()){
				tempSize++;
				if (tempSize == tables.size()){
					return true;
				}
			}
		}
		return false;
		
	}

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
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
		// TODO Auto-generated method stub
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	public void setCook(Cook co){
		cook = co;
	}
	
	public void setCashier(CashierRole ca){
		cashier = ca;
	}

	

	
}
