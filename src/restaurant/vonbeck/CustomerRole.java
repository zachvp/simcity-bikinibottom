package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.vonbeck.gui.CustomerGui;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Customer;
import agent.Agent;
import agent.Role;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer {
	private static final long CHOICE_DELAY_MS = 6000;
	private static final int EAT_DELAY_MS = 5000;
	private static final int LEAVE_DELAY_MS = 5000;
	private String name;
	private String currentPlate = "";
	private int hungerLevel = 5;        // determines length of meal
	private int tableNumber = 0;
	private Map<String, Integer> foodList;
	private String foodToOrder;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	// agent correspondents
	private HostAgent host;
	private WaiterAgent waiter;
	private Cashier cashier;
	private int wallet = 0;
	private int debt = 0;

	private int priceToPay;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Eating, WalkingToCashier, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, gotFood, doneEating, standingNextToCashier, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(String name){
		super();
		this.name = name;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}

	// Getting name
	public String getCustomerName() {
		return name;
	}
	
	//Messages
	public void msgSitAtTable(int tableNum, WaiterAgent w, Map<String, Integer> menu) {
		Do("Received msgSitAtTable");
		event = AgentEvent.followHost;
		tableNumber = tableNum;
		waiter = w;
		foodList = menu;
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
	public void msgAnimationFinishedGoToCashier() {
		//from animation
		event = AgentEvent.standingNextToCashier;
		stateChanged();
	}
	
	public void msgHeresYourFood(String food) {
		currentPlate = food;
		event = AgentEvent.gotFood;
		stateChanged();
	}

	public void msgOutOfFood(String food) {
		foodList.remove(food);
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		
		stateChanged();
	}

	public void msgBill(int total, Cashier cashier2) {
		this.cashier = cashier2;
		priceToPay = total;
		wallet -= priceToPay;
		event = AgentEvent.doneEating;
		
		stateChanged();
	}
	
	public String getOrder() {
		Do("Ordering " + foodToOrder);
		customerGui.DoClearFood();
		String temp = foodToOrder;
		foodToOrder = null;
		return temp;
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
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			chooseFood();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.gotFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WalkingToCashier;
			walkToCashier();
			return true;
		}
		
		if (state == AgentState.WalkingToCashier && event == AgentEvent.standingNextToCashier){
			state = AgentState.Paying;
			pay();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions
	
	public void gotHungry() {//from animation
		Do("I'm hungry");
		
		boolean hasANumberName = true;
		try { 
	        wallet = Integer.parseInt(name);
	    } catch(NumberFormatException e) { 
	    	hasANumberName = false; 
	    }
		
		if (!hasANumberName) {
			wallet = 500+(int)(Math.random()*800);
		}
		
		wallet += debt;
		
		getCustomerGui().DoGoToWaitZone();
		
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	// Actions

	private void walkToCashier() {
		getCustomerGui().DoGoToCashier();
	}

	private void pay() {
		if (wallet >= 0) cashier.msgPay(this, priceToPay);
		else {
			int temp = -wallet;
			wallet = 0;
			debt = temp;
			Do("Hey, I purposly ordered something I don't have money to pay for, because that's just how I was born.");
			cashier.msgIDontHaveEnoughMoney(this, priceToPay, temp);
			Do("LOL. K, thanks.");
		}
		event = AgentEvent.donePaying;
		
		stateChanged();
	}

	private void askForBill() {
		Do("Can I have the bill?");
		waiter.msgReadyToPay(this, currentPlate);
		currentPlate = "";
	}

	private void chooseFood() {
		synchronized (this) {
			try {
				wait(CHOICE_DELAY_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<String> foodTooExpensive = new ArrayList<String>();
		
		for(Map.Entry<String, Integer> food : foodList.entrySet()) {
			if (food.getValue() > wallet) {
				foodTooExpensive.add(food.getKey());
			}
		}
		
		int lastDigitOfWallet = wallet - ((wallet/10)*10);
		
		/*
		 * Hack, people do not care about food prices if the number of
		 * cents in their wallet ends with 6
		 */
		if (lastDigitOfWallet != 6) for (String food : foodTooExpensive) {
			foodList.remove(food);
		}
		
		if (foodList.size() > 0) {
			int choiceNum = (int)(Math.random()*(foodList.size()));
			foodToOrder = (String) foodList.keySet().toArray()[choiceNum];
			customerGui.DoDisplayFood(foodToOrder,true);
			Do("I want to order!");
			waiter.msgIWantToOrder(this);
		} else {
			Do("There is no food that I can eat :(");
			state = AgentState.Paying;
			event = AgentEvent.donePaying;
		}
		
		stateChanged();
		
	}

	private void goToRestaurant() {
		Do("Going to restaurant");
		
		if (debt > 0) {
			Do("Hey I'm back to pay my debt, only because it is required of me by the assignment table left by the prophets.");
			cashier.msgPayDebt(this, debt);
			wallet -= debt;
		}
		
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		getCustomerGui().DoGoToSeat(tableNumber);
	}

	private void EatFood() {
		Do("Eating Food");
		customerGui.DoDisplayFood(currentPlate, false);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				Do("Done eating, cookie=" + cookie);
				customerGui.DoClearFood();
				askForBill();
				//isHungry = false;
				stateChanged();
			}
		},
		EAT_DELAY_MS);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		if (waiter != null) waiter.msgImLeaving(this);
		// TODO WARNING: This is not thread safe either :c
		else host.msgImTiredOfWaitingByeYourRestaurantSucks(this);
		getCustomerGui().DoExitRestaurant();
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
		setCustomerGui(g);
	}

	public CustomerGui getGui() {
		return getCustomerGui();
	}

	/**
	 * @return the customerGui
	 */
	public CustomerGui getCustomerGui() {
		return customerGui;
	}

	/**
	 * @param customerGui the customerGui to set
	 */
	public void setCustomerGui(CustomerGui customerGui) {
		this.customerGui = customerGui;
	}


}

