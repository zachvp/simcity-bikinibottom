package housing;

import housing.gui.LayoutGui;
import housing.gui.ResidentRoleGui;
import housing.interfaces.Dwelling;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mock.EventLog;
import mock.MockScheduleTaskListener;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.Constants.Condition;
import agent.PersonAgent;
import agent.Role;

/**
 * ResidentRole is a more abstract class that can be extended by
 * either a home owner or an apartment tenant. Both share the core functionality
 * of monthly payments (rent or mortgage) and household duties such as cooking
 * and maintenance.
 * @author Zach VP
 *
 */

public class ResidentRole extends Role implements Resident {
	/* --- DATA --- */
	
	// test data
	public EventLog log = new EventLog();
	MockScheduleTaskListener listener = new MockScheduleTaskListener();
	
	// used to create time delays and schedule events
	private ScheduleTask schedule = new ScheduleTask();
	
	// state for tasks. The Role will deactivate if it is not performing any tasks.
	// used to determine when the role should terminate and transition to a city role
	enum TaskState { NONE, DOING_TASK }
	TaskState task = TaskState.NONE;
	
	// graphics
	private ResidentGui gui = new ResidentRoleGui(this);
	
	// TODO: this will be set true by the 
	private boolean hungry = false;
	
	// rent data
	private double oweMoney = 0;
	private PayRecipient payee;
	private Dwelling dwelling;
	
	// food data
	private Map<String, Food> refrigerator = Collections.synchronizedMap(new HashMap<String, Food>(){
		{
			put("Krabby Patty", new Food("Krabby Patty", 1, 0, 4, 10));
		}
	});
	
	private Map<String, Integer> groceries = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Food food = null;// the food the resident is currently eating
	
	// constants
	private final int EAT_TIME = 5; 
	private final int IMPATIENCE_TIME = 5;
	
	/* ----- Class Data ----- */
	/**
	 * Food is kept in the refrigerator and encapsulates all
	 * the relevant data needed for inventory management.
	 */
	enum FoodState { RAW, COOKED, COOKING };
	private class Food{
		String type;
		FoodState state;
		int amount, cookTime, low, capacity;
		
		private Food(String type, int amount, int low, int capacity, int cookTime){
			this.type = type;
			this.amount = amount;
			this.low = low;
			this.capacity = capacity;
			this.cookTime = cookTime;
			state = FoodState.RAW;
		}
	}
	
	/* --- Constructor --- */
	public ResidentRole(PersonAgent agent, CityLocation residence) {
		super(agent, residence);
	}
	
	/* ----- Messages ----- */
	@Override
	public void msgPaymentDue(double amount) {
		this.oweMoney = amount;
		Do("Received message 'payment due' amount is " + amount);
		stateChanged();
	}
	
	@Override
	public void msgDwellingFixed(){
		Do("Received message 'dwelling fixed'");
		dwelling.setCondition(Condition.GOOD);
	}
	
	// sent from the gui when it has reached the target destination
	public void msgAtDestination() {
		doneWaitingForInput();
	}
	

	/* ----- Scheduler ----- */
	@Override
	public boolean pickAndExecuteAnAction() {
		
		if(food != null && food.state == FoodState.COOKED) {
			eatFood();
			return true;
		}
		
		if(dwelling.getCondition() == Condition.POOR ||
				dwelling.getCondition() == Condition.BROKEN) {
			if(dwelling.getWorker() != null){
				callMaintenenceWorker();
				return true;
			}
		}
		
		if(oweMoney > 0 && person.getWallet().getCashOnHand() > 0) {
			tryToMakePayment();
			return true;
		}
		
		//TODO: The conditions for the below event need to be modified
		if(hungry) {
			synchronized(refrigerator) {
				for(Map.Entry<String, Food> entry : refrigerator.entrySet()) {
					Food f = entry.getValue();
					if(f.amount > 0){
						cookFood(f);
						return true;
					}
				}
			}
		}
		
		// idle behavior
		DoJazzercise();
		
		// set a delay. If the timer expires, then the resident has taken care of business
		// at home and is free to roam the streets
		Runnable command = new Runnable() {
			public void run(){
				deactivate();
			}
		};
		
		// schedule a delay for food consumption
		listener.taskFinished(schedule);
		schedule.scheduleTaskWithDelay(command, IMPATIENCE_TIME * Constants.MINUTE);
		
		return false;
	}
	
	/* ----- Actions ----- */
	private void tryToMakePayment() {
		Do("Attempting to make payment. Cash amount is "
				+ person.getWallet().getCashOnHand());
		
		// used for easy comparison
		double cash = person.getWallet().getCashOnHand();
		
		if(cash >= oweMoney) {
			payee.msgHereIsPayment(oweMoney, this);
			cash -= oweMoney;
			person.getWallet().setCashOnHand(cash);
			oweMoney = 0;
		}
		else if(cash > 0) {
			payee.msgHereIsPayment(cash, this);
			oweMoney -= cash;
			cash = 0;
			person.getWallet().setCashOnHand(cash);
			Do("Not enough money to cover full cost. Cash now " + cash);
			// TODO person.needToGoToBank()
		}
		else {
			Do("Don't have any money to pay my dues.");
			// TODO person.needToGoToBank()
		}
	}
	
	private void eatFood() {
		hungry = false;
		
		// pick up the food
		DoGoToStove();
		waitForInput();
		
		// display the food being carried
		DoSetFood(food.type);
		
		// got sit at table and eat
		DoGoToTable();
		waitForInput();
		Do("Eating food " + food.type);
		
		// set a timer for eating
		Runnable command = new Runnable() {
			public void run(){
				DoSetFood("");
				food = null;
				doneWaitingForInput();
				stateChanged();
				DoJazzercise();
			}
		};
		
		// schedule a delay for food consumption
		listener.taskFinished(schedule);
		schedule.scheduleTaskWithDelay(command, EAT_TIME * Constants.MINUTE);
		waitForInput();
		
		// the cook/eat food task has completed, so the role is free to deactivate
		task = TaskState.NONE;
	}
	
	private void cookFood(Food f) {
		// begin the cook/eat food task. The role will not be allowed to deactivate
		// until the task is complete
		task = TaskState.DOING_TASK;
		
		Do("Cooking food");
		food = f;
		
		// retrieve food from refrigerator
		DoGoToRefrigerator();
		waitForInput();
		
		// carry food from fridge to stove
		DoSetFood(food.type);
		DoGoToStove();
		waitForInput();
		
		// place food on stove
		DoSetFood("");
		f.amount--;
		food.state = FoodState.COOKING;
		
		// add to grocery list if the food item is low
		if(f.amount == f.low) {
			Do("Adding " + f.type + " to grocery list");
			groceries.put(f.type, f.capacity - f.low);
		}
		
		// set a timer with a delay using method from abstract Role class
		Runnable command = new Runnable(){
			public void run(){
				timerDoneCooking();
			}
		};
		
		// cook the food for the proper time
		listener.taskFinished(schedule);
		schedule.scheduleTaskWithDelay(command, food.cookTime * Constants.MINUTE);
	}
	
	private void callMaintenenceWorker(){
		Do("This house needs fixing! Calling a maintenance worker.");
		dwelling.getWorker().msgFileWorkOrder(dwelling);
		dwelling.setCondition(Condition.BEING_FIXED);
	}
	
	/* --- Animation Routines --- */
	private void DoGoToStove() {
		Do("Going to stove.");
		gui.DoGoToStove();
	}
	
	private void DoGoToTable() {
		Do("Going to table.");
		gui.DoGoToTable();
	}
	
	private void DoGoToRefrigerator() {
		Do("Going to refrigerator.");
		gui.DoGoToRefrigerator();
	}
	
	private void DoSetFood(String type) {
		gui.setFood(type);
	}
	
	private void DoJazzercise() {
		gui.DoJazzercise();
	}
	
	/* ----- Utility Functions ----- */
	private void timerDoneCooking() {
		food.state = FoodState.COOKED;
		Do("Food is cooked.");
		stateChanged();
	}
	
	public boolean thereIsFoodAtHome() {
		synchronized(refrigerator){
			for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
				if(entry.getValue().amount > 0){
					Do("There is food at home.");
					return true;
				}
			}
		}
		Do("There is no food at home");
		return false;
	}
	
	/* --- Getters and Setters (mostly used for unit testing) --- */
	public void setGui(ResidentGui gui2) {
		this.gui = gui2;
	}
	
	public PayRecipient getPayee() {
		return payee;
	}

	public void setPayee(PayRecipient payee) {
		this.payee = payee;
	}

	public double getMoneyOwed() {
		return oweMoney;
	}

	public void setMoneyOwed(double moneyOwed) {
		this.oweMoney = moneyOwed;
	}
	
	public void setPerson(PersonAgent person) {
		this.person = person;
	}

	public boolean isHungry() {
		return hungry;
	}

	public void setHungry(boolean hungry) {
		this.hungry = hungry;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Map<String, Integer> getGroceries() {
		return groceries;
	}

	public void setGroceries(Map<String, Integer> groceries) {
		this.groceries = groceries;
	}

	public Map<String, Food> getRefrigerator() {
		return refrigerator;
	}

	public void setRefrigerator(Map<String, Food> refrigerator) {
		this.refrigerator = refrigerator;
	}

	public void setDwelling(Dwelling dwelling) {
		this.dwelling = dwelling;
	}

	public Dwelling getDwelling() {
		return dwelling;
	}
}