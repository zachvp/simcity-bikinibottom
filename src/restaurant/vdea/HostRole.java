package restaurant.vdea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityBuilding;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.vdea.interfaces.*;

public class HostRole extends WorkRole{
	static final int NTABLES = 4;
	public List<Customer> waitingCustomers	= Collections.synchronizedList(new ArrayList<Customer>());
	public List<Waiter> waiters = new ArrayList<Waiter>();		
	public List<Customer> inLine	= Collections.synchronizedList(new ArrayList<Customer>());
	public Collection<Table> tables;
	public boolean sen = false;
	private String name;
	private Semaphore atDest = new Semaphore(0, true);

	public enum AgentState
	{none, tablesFull, assigningCustomer, leave, leaveLine}
	private AgentState state = AgentState.none;


	public HostRole(Person person, CityBuilding building) {
		super(person, building);
		this.name = super.getName();
		
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public List getWaiters() {
		return waiters;
	}

	public Collection getTables() {
		return tables;
	}


	public void addWaiter(Waiter newWaiter){
		waiters.add(newWaiter);
		stateChanged();
	}

	public int getLineNum(Customer c){ //TODO get lineNum
		int i=0;
		for (Customer a: inLine){
			if (a == c){
				return i;
			}
			else{
				i++;
			}
		}
		return i;
	}

	public void atDest(){
		atDest.release();
	}

	//allow break IFF at least 1 waiter can work
	public boolean allowBreak(){
		int workingWaiters = 0;
		for (Waiter w: waiters){
			if(w.isNotOnBreak() || !w.isGoingOnBreak()){
				workingWaiters++;
			}
		}
		return workingWaiters > 1;
	}


	// Messages

	public void msgIWantFood(Customer cust) {			
		if (allTablesFull()){
			state = AgentState.tablesFull;
		}
		else{
			state = AgentState.assigningCustomer;
		}
		waitingCustomers.add(cust);
		inLine.add(cust);
		stateChanged();
	}

	//from customer
	public void msgStayOrLeave(Customer cust, boolean stay){
		for (Customer c: waitingCustomers){
			if (c == cust){
				if(!stay){
					//waitingCustomers.remove(c);
					state = AgentState.leave;
				}
				else{
					state = AgentState.assigningCustomer;
				}
			}
		}

	}

	public void msgLeaveLine(Customer cust){
		for (Customer c: inLine){
			if(c==cust){
				state = AgentState.leaveLine;
				stateChanged();
			}
		}
	}

	//from waiter
	public void msgTableIsFree(int t) {
		for (Table table : tables) {
			if (table.tableNumber == t) {
				table.setUnoccupied();
				stateChanged();
			}
		}	
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */

	protected boolean pickAndExecuteAnAction() {	
		sen = sen ? sen : !waitingCustomers.isEmpty() && waiters.isEmpty();
		//if there is an unoccupied table in tables
		// && there is a customer in waitingCustomers
		// 	then assignWaiter(waitingCustomers.get(0), table, waiter);
		//print("state: " + state);
		synchronized(inLine){
			for(Customer i: inLine){
				if (state == AgentState.leaveLine){
					leaveLine(i);
					return true;
				}
			}
		}

		synchronized(waitingCustomers){
			for (Customer c: waitingCustomers){
				if(state == AgentState.tablesFull){
					restaurantFull(c);
				}
				if(state == AgentState.assigningCustomer){
					for (Table table: tables){
						if(!table.isOccupied() && !waiters.isEmpty()){
							Waiter least = pickWaiter();
							least.setBusy(true);
							assignWaiter(c, table, least);//the action
							return true;
						}
					}
				}
				if(state == AgentState.leave){
					customerLeaves(c);
					return true;
				}
			}
		}


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.

	}	

	public void restaurantFull(Customer c){
		print("The restaurant is full. Would you like to wait?");
		c.msgRestaurantFull();
	}
	
	
	
	//returns the waiter with the smallest number of customers who are not on break
	public Waiter pickWaiter(){	
		//print("pick waiter");
		List<Waiter> workingWaiters = new ArrayList<Waiter>();
		for(Waiter waiter: waiters){
			if(!waiter.isGoingOnBreak()){
				workingWaiters.add(waiter);
				//print("adding waiter " + waiter);
			}
		}
		
		Waiter result = workingWaiters.get(0);
		int min = result.numOfCust();
		int test;
		
		for(Waiter waiter: workingWaiters){
			test = waiter.numOfCust();
			if(test < min){
				min = test;
				result = waiter;
			}
		}
		return result;
	}

	// Actions

	public void assignWaiter(Customer customer, Table table, Waiter waiter) {
		//state = AgentState.none;
		print("Welcome " +customer.getName() +". Your waiter tonight will be " + waiter.getName());
		table.setOccupant(customer);
		/*try{
			atDest.acquire(); //TODO semaphore needed?
		}catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		waiter.msgSeatCustomer(customer, table.getTableNumber());
		waitingCustomers.remove(customer);
	}


	public void leaveLine(Customer c){ //TODO leave line
		inLine.remove(c);
		for (Customer a: inLine){
			//a.updateLine();
		}
	}

	public void customerLeaves(Customer c){
		waitingCustomers.remove(c);
		leaveLine(c);
	}
	
	public boolean allTablesFull(){
		int count = 0;
		for(Table t: tables){
			if(t.isOccupied()){
				count++;
			}
		}
		return count == tables.size();
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

	public class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		int getTableNumber(){
			return tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		/*CustomerAgent getOccupant() {
			return occupiedBy;
		}*/

		void setUnoccupied() {
			occupiedBy = null;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}
