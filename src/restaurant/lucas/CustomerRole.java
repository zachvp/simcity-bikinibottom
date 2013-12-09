package restaurant.lucas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import restaurant.CustomerAgent.CustomerEvent;
import restaurant.lucas.interfaces.Cashier;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.interfaces.Person;
import agent.interfaces.Person.Wallet;

/**
 * Restaurant customer role.
 * 
 * @author Jack Lucas
 */
public class CustomerRole extends Role implements Customer {
	
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	int tableXCoord;
	int tableYCoord;
	Timer timer = new Timer();
	Random rnd = new Random();

	String choice = "";

	public class WaitPosition {
		private Semaphore occupied = new Semaphore(1);
		int x;
		int y;

		WaitPosition(int x, int y) {
			this.x = x;
			this.y =y;
		}

		public void tryAcquire(Semaphore s) {
			try {
				s.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//semaphore and position
	}

	static List<WaitPosition> waitPositions = new ArrayList<WaitPosition>();


	List<String> menu = new ArrayList<String>();
	Map<String, Double> menuMap = new HashMap<String, Double>();

	int choiceNumber =0;

	private CustomerGui customerGui;

	// agent correspondents
	private Waiter waiter;
	private HostAgent host;

	double myMoney = 23;
	double myBill = 0;
	double owedMoney = 0;
	boolean willLeaveIfFull = false;
	Cashier myCashier;


	//    private boolean isHungry = false; //hack for gui
	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Ordered, Eating, waitingForBill, paying, DoneEating, Leaving};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent
	{none, gotHungry, followWaiter, reachedTable, decidedOrder, askedForOrder, askedToReorder, receivedFood, receivedBill, receivedChange, doneEating, doneLeaving, restaurantFull};
	CustomerEvent event = CustomerEvent.none;


	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgRestarauntIsFull() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgFollowMeToTable(Waiter w, int x, int y, Map<String, Double> m) {
		this.waiter = w;
		for (Entry<String, Double> entry : m.entrySet()) {
		    String food = entry.getKey();
		    Double price = entry.getValue();
		    menuMap.put(food,  price);
		}
//		for(int i = 0; i<m.size(); i++ ) 
//		{
//			menu.add(m.get(i));//gives customer menu from waiter
//		}
		print("Following " + w.getName() + " to my table");
		tableXCoord = x;
		tableYCoord = y;
		event = CustomerEvent.followWaiter;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		Do("I've been asked for my order");
		event = CustomerEvent.askedForOrder;
		stateChanged();
	}

	@Override
	public void msgOutOfChoice(Map<String, Double> newMenu) {
		menuMap = newMenu;
		doDisplayChoice("?");
		event = CustomerEvent.askedToReorder;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedDecidingOrder() {
		event = CustomerEvent.decidedOrder;
		stateChanged();
	}

	@Override
	public void msgHereIsYourFood() {
		event = CustomerEvent.receivedFood;
		stateChanged();
	}

	@Override
	public void msgHereIsCheck(Cashier c, double check) {
		event = CustomerEvent.receivedBill;
		myCashier = c;
		myBill = check;
		stateChanged();
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		event = CustomerEvent.reachedTable;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}

	@Override
	public void msgHereIsChange(double change) {
		myMoney += change;
		event = CustomerEvent.receivedChange;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}


}