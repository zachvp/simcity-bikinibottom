package restaurant.vdea;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.vdea.gui.*;
import restaurant.vdea.interfaces.*;
import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.PersonAgent.HungerLevel;
import agent.interfaces.Person;

public class CustomerRole extends Role implements Customer {

	private String name;
	private int hungerLevel = 15;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int tableNumber;
	private Menu menu;
	private String choice;
	private String reorder;
	private double bill;
	private double cash = 20;	//TODO decide on how to instantiate money
	private boolean bad = false;
	private double debt = 0;
	// agent correspondents
	private Waiter waiter;
	private Host host;
	private Cashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, ReadingMenu, AskingToOrder, Ordered, DoneEating,  AskedForCheck, payingBill, Leaving};
	AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, restaurantFull, followHost, seated, doneReading, leaving, askedForOrder, askedToReorder, ordered, foodDelivered, doneEating, receivedCheck, paid, doneLeaving};
	AgentEvent event = AgentEvent.none;
	
	public CustomerRole(Person person, CityLocation location) {
		super(person, location);
		
		this.name = super.getName();
		
		if (name.equals("bad")){
			cash = 0;
			bad = true;
		}
		if (name.equals("poor")){
			cash = 8.50;
		}
		reorder = "";
	}
	/**
	 * Activate the CustomerRole, then get hungry.
	 */
	@Override
	public void activate() {
		super.activate();
		customerGui.setHungry();
		gotHungry();
	}
	public void setTableNumber(int t){
		tableNumber = t;
	}
	 
	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
	public void setCashier(Cashier cashier){
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	public String getChoice(){
		return choice;
	}
	
	public int getPosX(){
		return customerGui.getPosX();
	}
	public int getPosY(){
		return customerGui.getPosY();
	}
	public void updateLine(){
		int n = host.getLineNum(this);
		print("line num: "+n);
		customerGui.updateWait(n);
	}
	
	// Messages

		public void gotHungry() {//from animation
			print("I'm hungry");
			event = AgentEvent.gotHungry;
			stateChanged();
		}
		
		public void msgRestaurantFull(){
			event = AgentEvent.restaurantFull;
			stateChanged();
		}
		
		//from waiter
		public void msgFollowMeToTable(int table, Menu m){
			tableNumber = table;
			menu = m;
			event = AgentEvent.followHost;
			stateChanged();
		}
		
		//from waiter
		public void msgWhatWouldYouLike(){
			event = AgentEvent.askedForOrder;
			stateChanged();
		}
		
		public void msgPleaseReorder(String choice){
			event = AgentEvent.askedToReorder;
			reorder = choice;
			stateChanged();
		}
		
		//from waiter
		public void msgHereIsYourFood(){
			event = AgentEvent.foodDelivered;
			stateChanged();
		}
		
		//from waiter
		public void msgHereIsYourCheck(double check){
			bill = check;
			event = AgentEvent.receivedCheck;//paid;
			stateChanged();
		}
		
		//from cashier
		public void msgHereIsYourChange(double change){ //TODO do something with change?
			cash = change;
			event = AgentEvent.paid;
			stateChanged();
		}
		
		public void msgYouHaveDebt(double d){
			debt += d;
			event = AgentEvent.paid;
			stateChanged();
		}
		

		public void msgAnimationFinishedGoToSeat() {
			//from animation
			event = AgentEvent.seated;
			stateChanged();
		}
		public void msgAnimationFinishedLeaveRestaurant() {
			//from animation
			event = AgentEvent.doneLeaving;
			stateChanged();
		}
		
		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			//	CustomerAgent is a finite state machine

			if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
				state = AgentState.WaitingInRestaurant;
				goToRestaurant();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.restaurantFull){
				//state = AgentState.BeingSeated;
				decideToStay();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
				state = AgentState.BeingSeated;
				SitDown();
				return true;
			}
			
			if (state == AgentState.BeingSeated && event == AgentEvent.seated ){
				state = AgentState.ReadingMenu;
				readMenu();
				return true;
			}
			
			if (event == AgentEvent.leaving){
				//state = AgentState.leaving;
				leave();
				return true;
			}
			
			
			if (state == AgentState.ReadingMenu && event == AgentEvent.doneReading){
				state = AgentState.AskingToOrder;
				askToOrder();
				return true;
			}

			if (state == AgentState.AskingToOrder && event == AgentEvent.askedForOrder){
				state = AgentState.Ordered;
				orderFood();
				return true;
			}
			
			if (state == AgentState.Ordered && event == AgentEvent.askedToReorder){	
				reorderFood();
				return true;
			}
			
			if (state == AgentState.Ordered && event == AgentEvent.foodDelivered){
				state = AgentState.DoneEating;
				eatFood();
				return true;
			}
			if (state == AgentState.DoneEating && event == AgentEvent.doneEating){
				state = AgentState.AskedForCheck;
				askForCheck();
				return true;
			}
			if (state == AgentState.AskedForCheck && event == AgentEvent.receivedCheck){
				state = AgentState.payingBill;
				payBill();
				return true;
			}
			if (state == AgentState.payingBill && event == AgentEvent.paid){
				state = AgentState.Leaving;
				leaveTable();
				return true;
			}
			if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
				state = AgentState.DoingNothing;
				person.setHungerLevel(HungerLevel.FULL);
				deactivate();
				return true;
			}
			return false;
		}

		// Actions

		private void goToRestaurant() { //TODO fix lineNum
			Do("Going to restaurant");
			
			int lineNum = host.getLineNum(this);
			//print("line num: "+lineNum);
			
			customerGui.DoGoToWaitingArea(lineNum);
			host.msgIWantFood(this);
		}
		
		private void decideToStay(){
			boolean stay = true;
			if (name.equals("leave")){
				stay = false;
			}
			else{
				Random r = new Random();
				int r1 = r.nextInt(10);
				stay = r1%2==0;
			}
			if (stay){
				print("I will wait");
			}
			else
			{
				print("I'm going to leave");
				state = AgentState.DoingNothing;
				customerGui.DoExitRestaurant();
			}
			host.msgStayOrLeave(this, stay);
			event = AgentEvent.none;
		}

		private void SitDown() {
			Do("Being seated. Going to table");
			customerGui.DoGoToSeat(1, tableNumber);
			//choice = decideOnOrder();
			event = AgentEvent.seated;
			stateChanged();
		}
		
		private void readMenu(){
			if(name.equals("steak")){
				choice = "steak";
			}else{
				choice = decideOnOrder();
			}
			
			//Timer times the customer action of reading a menu
			timer.schedule(new TimerTask() {
				public void run() {
					if (choice.equals("nothing") && !bad){
						print("Food is too expensive. I am leaving");
						event = AgentEvent.leaving;
					}
					else{
					print("Done reading the menu");
					event = AgentEvent.doneReading;
					}
					stateChanged();
				}
			},
			6 * 1000);		//adjust time to read menu
			
		}
		
		private String decideOnOrder(){
			String decision = "";
			//if i have enough money for anything
			if (bad || cash > menu.mostExpensivePrice()){
				Random r = new Random();
				int randChoice = r.nextInt(menu.numOfItems());
				decision =  menu.choose(randChoice);
			}
			else{
				decision = menu.whatCanIAfford(cash);
			}
			return decision;
		}
		
		private void leave(){
			state = AgentState.DoingNothing;
			waiter.msgDoneAndPaying(this);
			customerGui.DoExitRestaurant();
		}
		
		private void askToOrder(){
			print("I'm ready to Order");
			waiter.msgReadyToOrder(this);
			customerGui.ordering(choice);
		}
		
		private void orderFood(){
			event = AgentEvent.ordered;
			print("I would like to order " + choice);
			waiter.msgHereIsMyOrder(this, choice);
		}
		
		private void reorderFood(){
			choice = menu.reorder(reorder, cash);
			print("new choice "+choice);
			if(choice.equals("nothing")){
				customerGui.ordering("nothing");
				print("Food is too expensive. I am leaving");
				event = AgentEvent.leaving;
				stateChanged();
			}
			else{
				event = AgentEvent.ordered;
				customerGui.ordering(choice);
				print("I would like to order " + choice); //reset choice in gui
				waiter.msgHereIsMyOrder(this, choice);
			}
		}

		private void eatFood() {
			customerGui.gotFood();
			print("Eating...");
			//Timer times the customer eating food
			timer.schedule(new TimerTask() {
				public void run() {
					print("Done eating " + choice);
					event = AgentEvent.doneEating;
					stateChanged();
				}
			},
			getHungerLevel() * 500);
			
			
			///--------------------------------------------\\\\\
			/*Do("Eating Food");
			//This next complicated line creates and starts a timer thread.
			//We schedule a deadline of getHungerLevel()*1000 milliseconds.
			//When that time elapses, it will call back to the run routine
			//located in the anonymous class created right there inline:
			//TimerTask is an interface that we implement right there inline.
			//Since Java does not all us to pass functions, only objects.
			//So, we use Java syntactic mechanism to create an
			//anonymous inner class that has the public method run() in it.*/
		}	
		
		private void askForCheck(){
			print("Check please!");
			waiter.msgCheckPlease(this);
		}
		
		private void payBill(){
			System.out.printf(name + ": Paying bill with $%.2f%n", cash);
			cashier.msgPayment(this, bill, cash);
			waiter.msgDoneAndPaying(this);
		}


		private void leaveTable() {
			System.out.printf(name + ": Leaving with $%.2f%n", cash);
			customerGui.DoExitRestaurant();
		}
		
		// Accessors, etc.

		public String getName() {
			return name;
		}
		
		public int getHungerLevel() {
			return hungerLevel;
		}

		public void setHungerLevel(int hungerLevel) {
			this.hungerLevel = hungerLevel;
			//could be a state change. Maybe you don't
			//need to eat until hunger lever is > 5?
		}

		public String toString() {
			return "customer " + getName();
		}

		public void setGui(CustomerGui g) {
			customerGui = g;
		}

		public CustomerGui getGui() {
			return customerGui;
		}

}
