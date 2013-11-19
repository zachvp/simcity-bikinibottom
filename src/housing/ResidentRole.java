package housing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import housing.interfaces.Resident;
import agent.Role;

public class ResidentRole extends Role implements Resident {
	/** Rent Data */
	double moneyOwed = 0;
	Map<Resident, Double> landlordDropbox = Collections.synchronizedMap(new HashMap<Resident, Double>());
	
	/** Food Data */
	Map<String, Food> refrigerator;
	Food food; //the food the resident is currently eating
	Timer timer = new Timer(); //used to cook food and time eat period
	Map<String, Integer> cookTimes = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	/** Constant Variables */
	private final int EAT_TIME = 6; 
	
	/** Class Data */
	enum FoodState { COOKED };
	class Food{
		String type;
		FoodState state;
		int amount, cookTime, low, capacity;
	}
	
	public ResidentRole() {
		super();
	}
	
	/** Messages */
	@Override
	public void msgPaymentDue(double amount, Map<Resident, Double> d) {
		this.moneyOwed = amount;
		this.landlordDropbox = d;
	}

	/** Scheduler */
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(moneyOwed > 0){
			makePayment();
		}
		if(food != null && food.state == FoodState.COOKED){
			eatFood();
		}
		return false;
	}
	
	/** Actions */
	private void makePayment(){
		landlordDropbox.put(this, moneyOwed);
//		money -= moneyOwed;
		moneyOwed = 0;
	}
	private void eatFood(){
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
				stateChanged();
			}
		},
		EAT_TIME * 1000);//how long to wait before running task
	}
}
