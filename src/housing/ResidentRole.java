package housing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import agent.mock.EventLog;
import agent.PersonAgent;
import agent.Role;

public class ResidentRole extends Role implements Resident {
	/** DATA */
	public EventLog log = new EventLog();
	/* ----- Person Data ----- */
	private PersonAgent person;
	
	/* ----- Temporary Hacks ----- */
	//TODO: un-hack these
	private boolean hungry = false;
	
	/* ----- Rent Data ----- */
	private double moneyOwed = 0;
	private PayRecipient payee;
	
	/* ----- Food Data ----- */
	private Map<String, Food> refrigerator = Collections.synchronizedMap(new HashMap<String, Food>(){
		{
			put("Krabby Patty", new Food("Krabby Patty", 2, 1, 4, 5));
		}
	});
	
	private Map<String, Integer> groceries = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Food food = null; //the food the resident is currently eating
	private Timer timer = new Timer(); //used to cook food and time eat period
	
	/* ----- Constant Variables ----- */
	private final int EAT_TIME = 6; 
	
	/* ----- Class Data ----- */
	enum FoodState { RAW, COOKED, COOKING };
	private class Food{
		String type;
		FoodState state;
		int amount, cookTime, low, capacity;
		
		private Food(String type, int amt, int low, int cap, int cookTime){
			this.type = type;
			this.amount = amt;
			this.low = low;
			this.capacity = cap;
			this.cookTime = cookTime;
			
			state = FoodState.RAW;
		}
	}
	
	public ResidentRole(PersonAgent pa) {
		super();
		this.setAgent(pa);
		this.person = pa;
	}
	
	/* ----- Messages ----- */
	@Override
	public void msgPaymentDue(double amount) {
		this.moneyOwed = amount;
		log.add("Received message 'payment due' amount is " + amount);
		stateChanged();
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
			for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
				Food f = entry.getValue();
				if(f.amount > 0){
					cookFood(f);
					return true;
				}
			}
		}
		return false;
	}
	
	/* ----- Actions ----- */
	private void makePayment(){
		log.add("Attempting to make payment. Cash amount is "
				+ person.getWallet().getCashOnHand());
//		double money = person.getWallet().getCashOnHand();
		double cash = person.getWallet().getCashOnHand();
		if(cash >= moneyOwed){
			payee.msgHereIsPayment(moneyOwed, this);
			cash -= moneyOwed;
		}
		else{
			payee.msgHereIsPayment(cash, this);
			cash = 0;
		}
//		double money = person.wallet.getCashOnHand();
//		money -= moneyOwed;
		moneyOwed = 0;
	}
	private void eatFood(){
		log.add("Eating food");
//		TODO: ANIMATION DETAILS
//		DoGoToStove();
//		acquire(performingTasks);
//		DoGoToTable();
//		acquire(performingTasks);
//		DoEatFood();
//		acquire(performingTasks);
		hungry = false;
		food = null;
		timer.schedule(new TimerTask() {
			public void run() {
				hungry = false;
				food = null;
				stateChanged();
			}
		},
		EAT_TIME * 1000);//how long to wait before running task
	}
	private void cookFood(Food f){
		log.add("Cooking food");
		food = f;
		refrigerator.remove(food);
//		TODO: Animation details		
//		DoGoToRefrigerator();
		f.amount--;
		food.state = FoodState.COOKING;
		if(f.amount == f.low){
			groceries.put(f.type, f.capacity - f.low);
		}
//		DoGoToStove();
//		DoCooking(f.type);
		food.state = FoodState.COOKED;
		log.add("Food is cooked.");
		timer.schedule(new TimerTask() {
			public void run() {
				timerDoneCooking();
			}
		},
		f.cookTime * 1000);
	}
	
	/* ----- Utility Functions ----- */
	private void timerDoneCooking(){
		food.state = FoodState.COOKED;
		log.add("Food is cooked.");
		stateChanged();
	}
	public boolean thereIsFoodAtHome(){
		for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
			if(entry.getValue().amount > 0){
				log.add("There is food at home.");
				return true;
			}
		}
		log.add("There is no food at home");
		return false;
	}
	
	/* --- Getters and Setters (mostly used for testing) --- */
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