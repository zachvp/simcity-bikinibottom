package housing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import housing.gui.ResidentGui;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import agent.mock.EventLog;
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
	/** DATA */
	public EventLog log = new EventLog();
	
	/* ----- Person Data ----- */
	private ResidentGui gui;
	
	/* ----- Temporary Hacks ----- */
	//TODO: un-hack these
	private boolean hungry = true;
	
	/* ----- Rent Data ----- */
	private double moneyOwed = 0;
	private PayRecipient payee;
	
	/* ----- Food Data ----- */
	private Map<String, Food> refrigerator = Collections.synchronizedMap(new HashMap<String, Food>(){
		{
			put("Krabby Patty", new Food("Krabby Patty", 1, 0, 4, 5));
		}
	});
	
	private Map<String, Integer> groceries = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Food food = null; //the food the resident is currently eating
	private Timer timer = new Timer();
	
	/* ----- Constant Variables ----- */
	private final int EAT_TIME = 6; 
	
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
	
	public ResidentRole(PersonAgent agent) {
		super(agent);
	}
	
	/* ----- Messages ----- */
	@Override
	public void msgPaymentDue(double amount) {
		this.moneyOwed = amount;
		log.add("Received message 'payment due' amount is " + amount);
		stateChanged();
	}
	
	public void msgAtDest(){
		doneWaitingForInput();
	}

	/* ----- Scheduler ----- */
	@Override
	public boolean pickAndExecuteAnAction() {
		if(moneyOwed > 0){
			makePayment();
			return true;
		}
		if(food != null && food.state == FoodState.COOKED){
			eatFood();
			return true;
		}
		//TODO: The conditions for the below event need to be modified
		if(hungry){
			synchronized(refrigerator){
				for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
					Food f = entry.getValue();
					if(f.amount > 0){
						cookFood(f);
						return true;
					}
				}
			}
		}
//		DoJazzercise();
		Do("Nothing to do");
		return false;
	}
	
	/* ----- Actions ----- */
	private void makePayment(){
		log.add("Attempting to make payment. Cash amount is "
				+ person.getWallet().getCashOnHand());
		double cash = person.getWallet().getCashOnHand();
		if(cash >= moneyOwed){
			payee.msgHereIsPayment(moneyOwed, this);
			cash -= moneyOwed;
		}
		else{
			payee.msgHereIsPayment(cash, this);
			cash = 0;
		}
		moneyOwed = 0;
	}
	
	private void eatFood(){
		DoGoToStove();
		waitForInput();
		DoSetFood(food.type);
		DoGoToTable();
		waitForInput();
		log.add("Eating food");
		hungry = false;
		food = null;
		timer.schedule(new TimerTask() {
			public void run() {
				DoSetFood("");
				stateChanged();
			}
		},
		EAT_TIME * 1000);
	}
	
	private void cookFood(Food f){
		log.add("Cooking food");
		food = f;
		DoGoToRefrigerator();
		waitForInput();
		DoSetFood(food.type);
		DoGoToStove();
		waitForInput();
		DoSetFood("");
		f.amount--;
		food.state = FoodState.COOKING;
		if(f.amount == f.low){
			groceries.put(f.type, f.capacity - f.low);
		}
		/* --- Include for testing because JUnit doesn't recognize timers ---
		DoCooking(f.type);
		food.state = FoodState.COOKED;
		log.add("Food is cooked.");
 		*/
		timer.schedule(new TimerTask() {
			public void run() {
				timerDoneCooking();
			}
		},
		f.cookTime * 1000);
	}
	
	/* --- Animation Routines --- */
	private void DoGoToStove(){
		log.add("Going to stove.");
		gui.DoGoToStove();
	}
	
	private void DoGoToTable(){
		log.add("Going to table.");
		gui.DoGoToTable();
	}
	
	private void DoGoToRefrigerator(){
		log.add("Going to refrigerator.");
		gui.DoGoToRefrigerator();
	}
	
	private void DoSetFood(String type){
		gui.setFood(type);
	}
	
	private void DoJazzercise(){
		gui.DoJazzercise();
	}
	
	/* ----- Utility Functions ----- */
	private void timerDoneCooking(){
		food.state = FoodState.COOKED;
		log.add("Food is cooked.");
//		stateChanged();
	}
	
	public boolean thereIsFoodAtHome(){
		synchronized(refrigerator){
			for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
				if(entry.getValue().amount > 0){
					log.add("There is food at home.");
					return true;
				}
			}
		}
		log.add("There is no food at home");
		return false;
	}
	
	private void acquire(Semaphore sem){
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* --- Getters and Setters (mostly used for unit testing) --- */
	public void setGui(ResidentGui gui){
		this.gui = gui;
	}
	
	public PayRecipient getPayee() {
		return payee;
	}

	public void setPayee(PayRecipient payee) {
		this.payee = payee;
	}

	public double getMoneyOwed() {
		return moneyOwed;
	}

	public void setMoneyOwed(double moneyOwed) {
		this.moneyOwed = moneyOwed;
	}

	public PersonAgent getPerson() {
		return person;
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
}