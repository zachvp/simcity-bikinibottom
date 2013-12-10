package restaurant.vdea;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.vdea.gui.*;
import restaurant.vdea.interfaces.*;

public class WaiterRole extends WorkRole implements Waiter{

	public List<MyCustomer> customers = new ArrayList<MyCustomer>();

	public enum CustomerState
	{none, waiting, seated, readyToOrder, ordered, reorder, waitingForFood, foodready, served, needCheck, waitingForCheck, leaving};	


	private String name;
	private boolean busy= false;
	private boolean onBreak, goingOnBreak = false;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atKitchen = new Semaphore(0,true);
	private Semaphore atDest = new Semaphore(0,true);

	private Cook cook;
	private Host host;
	private Cashier cashier;
	public WaiterGui waiterGui = null;
	
	private boolean offWork = false;

	public WaiterRole(Person person, CityLocation location) {
		super(person, location);
		this.name = super.getName();
	}
	
	@Override
	public void activate() {
		super.activate();
		offWork = false;
		waiterGui.DoGoHomePos();
	}

	public boolean isBusy(){
		return busy;
	}

	public void setBusy(boolean b){
		busy = b;
	}

	public int numOfCust(){
		return customers.size();
	}

	public void setHost(Host newHost){
		host = newHost;
	}
	public Host getHost(){
		return host;
	}

	public void setCook(Cook newCook){
		cook = newCook;
	}

	public void setCashier(Cashier newCashier){
		cashier = newCashier;
	}
	public boolean isNotOnBreak(){
		return !onBreak;
	}
	public boolean isOnBreak(){
		return onBreak;
	}

	//for host to check which waiter to pick
	public boolean isGoingOnBreak(){
		return goingOnBreak;
	}

	// MESSEGES
	public void goOnBreak(){ //from gui
		print("going on break");
		if(customers.size() == 0){
			print("went on break;");
			onBreak = true;
		}
		goingOnBreak = true;	//TODO else?
	}

	public void goOffBreak(){
		onBreak = false;
		goingOnBreak = false;
	}

	//from host
	public void msgSeatCustomer(Customer c, int table) {		
		c.setWaiter(this);
		customers.add(new MyCustomer(c,table, CustomerState.waiting, c.getChoice()));
		print("Customer added to my list");
		stateChanged();
	}

	//from customer
	public void msgReadyToOrder(Customer cust) {
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.setState(CustomerState.readyToOrder);
				stateChanged();
			}
		}		
	}

	//from customer
	public void msgHereIsMyOrder(Customer cust, String myChoice) {
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.setChoice(myChoice);
				c.setState(CustomerState.ordered);
				busy = false;
				stateChanged();
			}
		}
	}

	//from cook
	public void msgOrderReady(String choice, int t) {
		for (MyCustomer c: customers) {
			if (c.table == t) {
				c.setState(CustomerState.foodready);
				stateChanged();
			}
		}
	}

	//from cook
	public void msgNotEnoughFood(String choice, int t){
		for (MyCustomer c: customers) {
			if (c.table == t) {
				c.setState(CustomerState.reorder);
				stateChanged();
			}
		}
	}

	//from customer
	public void msgCheckPlease(Customer cust){	//was readyToLeave
		for (MyCustomer c: customers) {
			if (c.c == cust) {
				c.setState(CustomerState.needCheck);//leaving);
				stateChanged();
			}
		}
	}

	//from cashier
	public void msgHereIsCheck(double check, int t){	//give check customer
		for (MyCustomer c: customers) {
			if (c.table == t) {
				c.toPay(check);
				print("got check from cashier");
				c.setState(CustomerState.waitingForCheck);
				stateChanged();
			}
		}
	}

	//from customer
	public void msgDoneAndPaying(Customer cust){	//was readyToLeave
		try{
			for (MyCustomer c: customers) {
				if (c.c == cust) {
					c.setState(CustomerState.leaving);
					stateChanged();
				}
			}
		}
		catch(Exception e){}
	}

	//from animation
	public void msgAtTable() {
		//print("table released");
		atTable.release();// = true;
		stateChanged();
	}

	//from animation
	public void msgAtHome(){
		if (busy){
			busy = false;
			//stateChanged();
		}
	}

	//from animation
	public void msgAtKitchen(){
		//print("kitchen released");
		atKitchen.release();
		stateChanged();				
	}

	public void atDest(){
		atDest.release();
	}
	

	@Override
	public boolean isAtWork() {
		return isActive() && isNotOnBreak();
	}

	@Override
	public void msgLeaveWork() {
		offWork = true;
		
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		try{
			for (MyCustomer c : customers) {
				if (c.getState() == CustomerState.waiting){
					seatCustomer(c);//the action
				}
				if (c.getState() == CustomerState.readyToOrder){
					takeOrder(c);
				}
				if (c.getState() == CustomerState.ordered) {
					sendToKitchen(c);
				}
				if (c.getState() == CustomerState.reorder){	
					retakeOrder(c);
				}
				if (c.getState() == CustomerState.foodready){
					deliverOrder(c);
				}
				if (c.getState() == CustomerState.needCheck){
					getCheck(c);
				}
				if (c.getState() == CustomerState.waitingForCheck){
					deliverCheck(c);
				}
				if (c.getState() == CustomerState.leaving) {
					clearTable(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e){}
		if (offWork) { 
			deactivate();
			waiterGui.DoLeave();
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer customer) { //TODO
		/*	waiterGui.DoGoToKitchen();
				try {
					atKitchen.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int home = -20;
				waiterGui.setXPos(home);
				waiterGui.setYPos(home);
		 */
		waiterGui.DoGoToCust(customer.c.getPosX(),customer.c.getPosY());
		print("Seating " + customer.c.getName());
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Takes customer to table and gives menu
		host.msgLeaveLine(customer.c);
		customer.c.msgFollowMeToTable(customer.table, new Menu());
		GoToTable(customer);
		waiterGui.DoLeaveCustomer();
		customer.setState(CustomerState.seated);
	}

	//helper method
	private void GoToTable(MyCustomer customer){
		waiterGui.DoGoToTable(customer.table);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void takeOrder(MyCustomer customer){
		GoToTable(customer);
		customer.setState(CustomerState.none);
		print("What would you like?");
		customer.c.msgWhatWouldYouLike();
	}

	private void sendToKitchen(MyCustomer customer){
		print("going to kitchen");
		waiterGui.DoGoToKitchen();
		//if(host.sen || customers.size()>1){
		atTable.release();
		//}

		try {
			atKitchen.acquire();
			atTable.acquire(); //TODO<-------
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();//returns home, so makes waiter not busy

		print("Order for the kitchen");
		customer.setState(CustomerState.waitingForFood);
		cook.msgThereIsAnOrder(this, customer.choice, customer.table);
	}

	//Asks customer to reorder
	private void retakeOrder(MyCustomer customer){
		GoToTable(customer);
		customer.setState(CustomerState.none);
		print("Sorry, we are out of " + customer.choice + ". What else would you like?");
		customer.c.msgPleaseReorder(customer.choice);
	}

	private void deliverOrder(MyCustomer customer){	//called when cook responds with cooked food
		waiterGui.DoGoToKitchen();
		print("going to kitchen");
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}
		cook.gotFood(customer.table);

		Do("Delivering order");
		waiterGui.delivering(customer.choice);//adds food image
		waiterGui.DoGoToTable(customer.table);	//move

		//waiterGui.DoServe(customer.c, customer.table);	// will use

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}

		waiterGui.DoLeaveCustomer();
		waiterGui.delivered();
		customer.c.msgHereIsYourFood();


		customer.setState(CustomerState.served);
	}

	private void getCheck(MyCustomer customer){
		GoToTable(customer);
		print("I'll be right back with your check!");
		waiterGui.DoLeaveCustomer();
		customer.setState(CustomerState.none);
		cashier.msgComputeBill(this, customer.c, customer.choice, customer.table);
	}

	private void deliverCheck(MyCustomer customer){
		print("going to table to deliver check");
		GoToTable(customer);
		print("Here is your check");
		customer.setState(CustomerState.none);
		customer.c.msgHereIsYourCheck(customer.bill);
		waiterGui.DoLeaveCustomer();
	}

	private void clearTable(MyCustomer customer){
		GoToTable(customer);
		print("Clearing table " + customer.table);
		host.msgTableIsFree(customer.table);
		customers.remove(customer);
		waiterGui.DoLeaveCustomer();

		//if they have no one left to serve and they are supposed 
		//to go on break
		if(customers.size() == 0 && goingOnBreak){ 
			onBreak = true;
			print("Going on Break");
		}
	}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	private class MyCustomer {
		Customer c;
		int table;
		String choice;
		protected CustomerState state;
		double bill;


		MyCustomer(Customer cust, int t, CustomerState s, String ch) {
			c = cust;
			table = t;
			state = s;
			choice = ch;
		}

		public void setState(CustomerState s){
			state = s;
		}

		public CustomerState getState(){
			return state;
		}

		public void setChoice(String c){
			choice  = c;
		}

		public void toPay(double check){
			bill = check;
		}

	}

	public class Bill {


		public Bill(Cashier ca, Customer cust, int tabeNum, double price){

		}
	}

}
