package restaurant.anthony;

import agent.Agent;



import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 */
// Food
public class Food {

	String choice;
	int cookingTime;
	int amount;
	int amountLowThreshold = 5;
	int amountBuyThreshold = 40;
	double price;
	

	public Food(String name, int time, int number) {
		choice = name;
		cookingTime = time;
		amount = number;
	}

	Food(String name, double P) {
		choice = name;
		price = P;
	}

	public void foodConsumed() {
		amount--;

	}

}
