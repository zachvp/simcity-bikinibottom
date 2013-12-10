package restaurant.anthony;

import agent.Agent;
import agent.Role;
import restaurant.anthony.WaiterRoleBase.Item;
import restaurant.anthony.WaiterRoleBase.Order;
import restaurant.anthony.gui.HostGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Market;
import restaurant.anthony.Food;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Market Agent
 */
// Market Agent
public class MarketRole extends Role implements Market {

	private double money =0;
	Timer timer = new Timer();
	public String name;
	private CookRole cook;
	private Cashier cashier;
	// public List<Order> MyOrders= new ArrayList<Order>();
	private List<Food> ShoppingList = null;
	boolean deliver = false;
	private List<Delivery> DeliverList;
	Map<String, Food> foods = new HashMap<String, Food>();
	{

		Food Chicken = new Food("Chicken", 4000, (int) (Math.random() * 2000));
		Food Pizza = new Food("Pizza", 3000, (int) (Math.random() * 2000));
		Food Steak = new Food("Steak", 5000, (int) (Math.random() * 2000));
		Food Salad = new Food("Salad", 2000, (int) (Math.random() * 2000));
		foods.put(("Chicken"), Chicken);
		foods.put("Steak", Steak);
		foods.put("Pizza", Pizza);
		foods.put("Salad", Salad);
	}
	Map<String, Double> Pricelist = new HashMap<String, Double>();
	{
		Pricelist.put(("Chicken"), 15.99);
		Pricelist.put("Steak", 8.99);
		Pricelist.put("Pizza", 10.99);
		Pricelist.put("Salad", 5.99);
	}

	public MarketRole(String name) {
		super();

		this.name = name;

	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * public List getMyOrders() { return MyOrders; }
	 */

	// Messages
	/* (non-Javadoc)
	 * @see restaurant.Market#BuyFood(java.util.List, restaurant.CookAgent)
	 */
	@Override
	public void BuyFood(List<Food> shoppingList2, CookRole co) {
		print("Got the message from cook, going to give food to him");
		cook = co;
		ShoppingList = shoppingList2;
		DeliverList = Collections.synchronizedList(new ArrayList<Delivery>());
		deliver = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#HeresTheMoney(double)
	 */
	@Override
	public void HeresTheMoney(double payment) {
		print ("I got the money from Cashier");
		money = money + payment;
		
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		if (deliver) {
			DeliverTheFood();
			return true;
		}
		return false;
	}

	// Actions

	private void DeliverTheFood() { // final?

		deliver = false;
		print("Delivering The Food");
		// OrderList is the ShoppingList
		for (int i = 0; i < ShoppingList.size(); i++) {
			print("Working on Order : " + ShoppingList.get(i).choice);
			// Processing the current Food (in the market)
			Food currentFood = foods.get(ShoppingList.get(i).choice);

			if (currentFood.amount == 0) {
				// No Food at all for this particular food
				// print ("No food for " + currentFood.choice);
				DeliverList.add(new Delivery(currentFood.choice, 0, false));

			}

			else if (currentFood.amount >= ShoppingList.get(i).amount) {
				// There is enough food for this market to fulfill the
				// shoppinglist request
				// print ("It is enough food to fulfill for " +
				// currentFood.choice + " " + currentFood.amount);
				DeliverList.add(new Delivery(currentFood.choice,
						ShoppingList.get(i).amount, true));
				currentFood.amount = currentFood.amount
						- ShoppingList.get(i).amount;
			} else {
				// When there is not enough food for this market to fulfill the
				// shoppinglist request
				// print ("Not enough food for " + currentFood.choice);
				DeliverList.add(new Delivery(currentFood.choice,
						currentFood.amount, false));
				currentFood.amount = 0;
			}

		}
		CalculateTotalMoney(DeliverList);
		cook.Orderfulfillment(DeliverList, this);
		return;

	}

	private void CalculateTotalMoney(List<Delivery> DeliverList){
		print ("Now I am calcluating the TotalMoney");
		double CurrentTotalPrice = 0;
		synchronized(DeliverList){
		for (int i =0;i<DeliverList.size();i++){
			double CurrentItemPrice = Pricelist.get(DeliverList.get(i).choice);
			int CurrentItemAmount = DeliverList.get(i).amount;
			
			CurrentTotalPrice = CurrentTotalPrice + (CurrentItemPrice * (double) CurrentItemAmount);
			print ("Total price for " + DeliverList.get(i).choice + " is " + (CurrentItemPrice * (double) CurrentItemAmount));
		}
		}
		print ("Asking Cashier to pay for the ingredients : " + CurrentTotalPrice);
		cashier.HeresTheIngredientPrice(CurrentTotalPrice, this);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier ca) {
		cashier = ca;
	}
	
	public class Delivery {
		String choice;
		int amount;
		boolean fulfilled;

		Delivery(String name, int number, boolean fulfillment) {
			choice = name;
			amount = number;
			fulfilled = fulfillment;
		}

	}

	

}
