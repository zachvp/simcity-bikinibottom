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
	PersonAgent person;
	
	/* ----- Temporary Hacks ----- */
	//TODO: un-hack these
	private boolean hungry = true;
	
	/* ----- Rent Data ----- */
	double moneyOwed = 0;
	private PayRecipient payee;
	
	/* ----- Food Data ----- */
	private Map<String, Food> refrigerator = Collections.synchronizedMap(new HashMap<String, Food>());
	private Map<String, Integer> groceries = Collections.synchronizedMap(new HashMap<String, Integer>());
	private Food food = null; //the food the resident is currently eating
	private Timer timer = new Timer(); //used to cook food and time eat period
	private Map<String, Integer> cookTimes = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	/* ----- Constant Variables ----- */
	private final int EAT_TIME = 6; 
	
	/* ----- Class Data ----- */
	enum FoodState { COOKED };
	private class Food{
		String type;
		FoodState state;
		int amount, cookTime, low, capacity;
	}
	
	public ResidentRole(PersonAgent pa) {
		super();
		this.person = pa;
	}
	
	/* ----- Messages ----- */
	@Override
	public void msgPaymentDue(double amount) {
		log.add("Received message 'payment due'");
		this.moneyOwed = amount;
		stateChanged();
	}

	/* ----- Scheduler ----- */
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(moneyOwed > 0){
			makePayment();
		}
		if(food != null && food.state == FoodState.COOKED){
			eatFood();
		}
		//TODO: The conditions for the below event need to be modified
		if(hungry){
			for(Map.Entry<String, Food> entry : refrigerator.entrySet()){
				Food f = entry.getValue();
				if(f.amount > 0){
					cookFood(f);
				}
			}
		}
		return false;
	}
	
	/* ----- Actions ----- */
	private void makePayment(){
		log.add("Attempting to make payment.");
//		double money = person.getWallet().getCashOnHand();
		if(person.getWallet().getCashOnHand() >= moneyOwed){
			payee.msgHereIsPayment(moneyOwed, this);
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
		
		timer.schedule(new TimerTask() {
			public void run() {
				food = null;
				hungry = false;
				stateChanged();
			}
		},
		EAT_TIME * 1000);//how long to wait before running task
	}
	private void cookFood(Food f){
		log.add("Cooking food");
//		TODO: Animation details		
//		DoGoToRefrigerator();
		f.amount--;
		if(f.amount == f.low){
			groceries.put(f.type, f.capacity - f.low);
		}
//		DoGoToStove();
//		DoCooking(f.type);
		timer.schedule(new TimerTask() {
			public void run() {
				food = null;
				stateChanged();
			}
		},
		cookTimes.get(f) * 1000);
		f.state = FoodState.COOKED;
		log.add("Food is cooked.");
	}
	
	/* ----- Utility Functions ----- */
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
}