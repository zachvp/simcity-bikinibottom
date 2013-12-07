package restaurant.lucas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


import restaurant.lucas.interfaces.Cook;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Market;
import restaurant.lucas.interfaces.Waiter;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Cook Role
 * 
 * @author Jack Lucas
 */


public class CookRole extends WorkRole implements Cook {

	
public enum state {none, received, cooking, cooked, givenToWaiter};
public class Order
{
	Order(String choice, int table, Customer customer, Waiter w, state s) {
		Choice = choice;
		this.table = table;
		cust = customer;
		waiter = w;
		orderState = s;
	}
	Customer cust;
	int table;
	public String Choice;
	Waiter waiter;
	
	private state orderState;
	
};

public class Grill {
	Grill(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Order o = null;
	public int x;
	public int y;
	
}

public class PlateArea {
	PlateArea(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Order o=null;
	public int x;
	public int y;
}

List<Grill> grills = new ArrayList<Grill>();
List<PlateArea> plateAreas = new ArrayList<PlateArea>();

public static class Food
{
	Food(String type, int cookingTime, int amount, int threshold, int capacity, boolean enroute) {
		this.type = type;
		this.cookingTime = cookingTime;
		this.amount = amount;
		this.threshold = threshold;
		this.capacity = capacity;
		this.enroute = enroute;
		
	}
	
	String type;
	int cookingTime;
	int amount;
	int threshold;
	int capacity;
	boolean enroute;
}

private Map<String, Integer> cookingTimes;
private Map<String, Food> foods = new HashMap<String, Food>();

private List<String> deliveredFoods = new ArrayList<String>();
private List<Integer> deliveredAmounts = new ArrayList<Integer>();//used to transfer from message to updating foods action

int previousMarket = 0;

List<Order> Orders = Collections.synchronizedList(new ArrayList<Order>());
Timer CookTimer;

private List<Market> markets = new ArrayList<Market>();
//map<String, int> cookTimes; 



	@Override
	public void msgHereIsAnOrder(String choice, int tableNum,
			Customer customer, Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderCooked(int orderNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIDontHave(List<String> foodList, List<Integer> amountList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodDelivered(List<String> foodList, List<Integer> amountList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	

}