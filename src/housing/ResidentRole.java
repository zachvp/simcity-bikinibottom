package housing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import housing.interfaces.Resident;
import agent.Role;

public class ResidentRole extends Role implements Resident {
	double moneyOwed = 0;
	Map<Resident, Double> landlordDropbox = Collections.synchronizedMap(new HashMap<Resident, Double>());
	
	Map<String, Food> refrigerator;
	Food food; //the food the resident is currently eating
	Timer timer; //used to cook food and time eat period
	Map<String, Integer> cookTimes;
	
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
			
		}
		return false;
	}
	
	/** Actions */
	private void makePayment(){
		landlordDropbox.put(this, moneyOwed);
//		money -= moneyOwed;
		moneyOwed = 0;
	}
}
