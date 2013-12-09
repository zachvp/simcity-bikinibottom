package restaurant.lucas;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.lucas.gui.CookGui;
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


	private String name;
	public CookGui cookGui;
	private Semaphore active = new Semaphore(0, true);



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
		Do("I have received order");
		Orders.add(new Order(choice, tableNum, customer, w, state.received));
		//		w.doDisplayChoice("none");//TODO too much sharing
		stateChanged();

	}

	@Override
	public void msgOrderCooked(int orderNum) {
		Order o = Orders.get(orderNum);
		o.orderState = state.cooked;
		stateChanged();		
	}

	@Override
	public void msgIDontHave(List<String> foodList, List<Integer> amountList) {
		if(foodList.size() == 0) {
			Do("Order from market has everything");
			return;
		}
		else {
			markets.get(++previousMarket%markets.size()).msgLowOnFood(foodList, amountList, this);//hack, need way to pick next market
		}
		stateChanged();		
	}

	@Override
	public void msgFoodDelivered(List<String> foodList, List<Integer> amountList) {
		deliveredFoods = foodList;
		//		Do(deliveredFoods.get(0));
		deliveredAmounts = amountList;
		stateChanged();		
	}

	@Override
	public void msgNullifyPlateArea(int yPos) {
		for(PlateArea p : plateAreas) {
			if (p.y == yPos) {
				p.o = null;
			}
		}		
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

		synchronized(Orders) {
			for(int i = 0; i < Orders.size(); i++) {
				if(Orders.get(i).orderState == state.received){
					//				Do("first cook sched");
					CookOrder(Orders.get(i), i);
					//				Orders.get(i).orderState = state.cooking;
					return true;
				}
			}
		}

		synchronized(Orders) {
			for(Order o : Orders) {
				if(o.orderState == state.cooked) {
					OrderCooked(o);
					o.orderState = state.givenToWaiter; 
					return true;
				}
			}
		}

		if(!deliveredFoods.isEmpty()) {
			Do("CONDITON MeT");
			updateInventory(deliveredFoods, deliveredAmounts);
			return true;
		}




		return false;
	}

	
	private void CookOrder(Order o, final int orderNum) {
		
		Food f = foods.get(o.Choice);
		if(f.amount < f.threshold) { //need to order more from market
//			Do("TRYING to orDEr");
			checkInventory();//hack, need to choose
			
		}
		if(f.amount <= 0) {
			o.waiter.msgOutOfFood(o.table, o.Choice);
			Orders.remove(o);
			return;
		}
		for(Grill g : grills) {
			if (g.o==null) {
				goToFridge();
				acquireSemaphore(active);
				goToGrill(g);
				acquireSemaphore(active);
				g.o = o;
				break;
				//animate going to grill TODO
			}
		}
		doGoToIdle();
		
		//DoCooking();//animation
		CookTimer.schedule(new TimerTask() {
			//Object cookie = 1;
			public void run() {
				print("Done cooking ");
				msgOrderCooked(orderNum);
			}
		}, cookingTimes.get(o.Choice));
		f.amount--;
		o.orderState=state.cooking;
	}
	

	
	public void checkInventory() {//take market as param?
		
		List<String> lowFoodsNames = new ArrayList<String>();
		List<Integer> lowFoodsAmount = new ArrayList<Integer>();
		
		if((foods.get("Tofu").amount < foods.get("Tofu").threshold) && !foods.get("Tofu").enroute) {
			Food temp = foods.get("Tofu");
			lowFoodsNames.add(temp.type);
			lowFoodsAmount.add(temp.capacity - temp.amount);
		}
		if((foods.get("Rice").amount < foods.get("Rice").threshold) && !foods.get("Rice").enroute) {
			Food temp = foods.get("Rice");
			Do("A " + temp.capacity);
			Do("B " + foods.get("Rice").capacity + " " + foods.get("Tofu").capacity);
			lowFoodsNames.add(temp.type);
			lowFoodsAmount.add(temp.capacity - temp.amount);
		}
		if((foods.get("Sushi").amount < foods.get("Sushi").threshold) && !foods.get("Sushi").enroute) {
			Food temp = foods.get("Sushi");
			lowFoodsNames.add(temp.type);
			lowFoodsAmount.add(temp.capacity - temp.amount);
		}
		if((foods.get("Noodles").amount < foods.get("Noodles").threshold) && !foods.get("Noodles").enroute) {
			Food temp = foods.get("Noodles");
			lowFoodsNames.add(temp.type);
			lowFoodsAmount.add(temp.capacity - temp.amount);
		}
		
//		if(lowFoodsNames.size() > 0) {
			markets.get(previousMarket % markets.size()).msgLowOnFood(lowFoodsNames, lowFoodsAmount, this);
//		}
	}
	
	public void updateInventory(List<String> deliveredFoodsList, List<Integer> deliveredAmountsList) {
		Do("updating inventory");//NEVER CALLED
		for(int i = 0; i < deliveredFoodsList.size(); i++) {
			if(deliveredFoodsList.get(i).equals("Tofu")) {
				foods.get("Tofu").amount = deliveredAmountsList.get(i);//increase amount have with amount delivered
				
			}
			if(deliveredFoodsList.get(i).equals("Rice")) {
				Do("Received Rice " + deliveredAmountsList.get(i));
				foods.get("Rice").amount = deliveredAmountsList.get(i);//increase amount have with amount delivered
			
			}
			if(deliveredFoodsList.get(i).equals("Sushi")) {
				foods.get("Sushi").amount = deliveredAmountsList.get(i);//increase amount have with amount delivered
				
			}
			if(deliveredFoodsList.get(i).equals("Noodles")) {
				foods.get("Noodles").amount = deliveredAmountsList.get(i);//increase amount have with amount delivered
				
			}
		}
		deliveredFoods.clear();
		deliveredAmounts.clear();
		//update amount Cook has
	}

		
	private void OrderCooked(Order o) {
		for (Grill g: grills) {
			if(g.o == o) {
				
				
				goToGrill(g);
				acquireSemaphore(active);
				g.o = null;
				break;
				
				//animate go to this grill TODO
			}
		}
		doGoToIdle();
		for(PlateArea p: plateAreas) {
			if(p.o == null) {
				
				
				goToPlateArea(p);
				Dimension plateDim = new Dimension(p.x, p.y);
				o.waiter.msgOrderIsReady(o.cust, o.Choice, o.table, plateDim);
				acquireSemaphore(active);
				p.o = o;
				break;
				
				
				//animate go to plating area TODO
			
			}
		}
		doGoToIdle();
				o.orderState = state.givenToWaiter;
	}

//Animation
	public void goToGrill(Grill g) {
		cookGui.DoGoToGrill(g);
	}
	
	public void goToPlateArea(PlateArea p) {
		cookGui.DoGoToPlateArea(p);
	}
	
	public void doGoToIdle() {
		cookGui.DoGoIdle();
	}
	
	private void goToFridge() {
		cookGui.DoGoToFridge();
		
	}
	
	public void msgAtDestination() {
		active.release();
	}
	

//accessors, utilities
	private void acquireSemaphore(Semaphore s) {
		
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Grill> getGrills() {
		return grills;
	}
	
	public List<PlateArea> getPlateAreas() {
		return plateAreas;
	}



}