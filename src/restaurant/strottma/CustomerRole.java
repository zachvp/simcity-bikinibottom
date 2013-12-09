package restaurant.strottma;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.strottma.WaiterRole.Menu;
import restaurant.strottma.gui.CustomerGui;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.SingletonTimer;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.interfaces.Person;
import agent.interfaces.Person.Wallet;

/**
 * Restaurant customer role.
 * 
 * @author Erik Strottmann
 */
public class CustomerRole extends Role implements Customer {
	private int hungerLevel = 10; // determines length of meal in seconds
	private int decideOrderDelay = 2; // how long it takes to decide an order, in seconds
	private int sayOrderDelay = 1; // how long it takes to order, in seconds
	Timer timer = SingletonTimer.getInstance();
	private CustomerGui customerGui;
	DecimalFormat df = new DecimalFormat("#.##");
	
	Semaphore multiStepAction = new Semaphore(0, true);
	
	public static int WS_COUNT = 5;
	public static int WS_X_OFFSET = 100-100; // how far the whole area is offset
	public static int WS_Y_OFFSET = 114;
	public static int WS_X_SPACING = 10; // how far each spot is separated from the last
	public static int WS_Y_SPACING = 10;
	public static int WS_WIDTH = 20;
	public static int WS_HEIGHT = 20;
	private static List<WaitSpot> waitSpots = new ArrayList<WaitSpot>() {
		{
			for (int i = 0; i < WS_COUNT; i++) {
				add(new WaitSpot(WS_X_OFFSET,
						WS_Y_OFFSET + i * (WS_HEIGHT + WS_Y_SPACING)));
			}
		}
	};
	
	private WaitSpot waitSpot;
	
	private String choice;
	private String choiceShort;
	
	private int tableX = 0;
	private int tableY = 0;

	// role correspondents
	private HostRole host;
	private Waiter waiter;
	private Cashier cashier;
	
	// money
	// double money; // now uses PersonAgent's money
	double bill;
	
	// received from waiter
	private Menu menu;

	// private boolean isHungry = false; // hack for gui
	public enum CustomerState {DOING_NOTHING, WAITING_IN_RESTAURANT,
			BEING_SEATED, SEATED, READY_TO_ORDER, ORDERED, EATING,
			WAITING_FOR_BILL, PAYING, LEAVING};
	private CustomerState state = CustomerState.DOING_NOTHING; // The start state

	public enum CustomerEvent {NONE, GOT_HUNGRY, FOLLOW_WAITER, REACHED_TABLE,
				DECIDED_ORDER, ASKED_FOR_ORDER, OUT_OF_CHOICE, RECEIVED_FOOD,
				DONE_EATING, RECEIVED_BILL, RECEIVED_CHANGE, DONE_LEAVING, NO_ROOM};
	private CustomerEvent event = CustomerEvent.NONE;
	
	public static final String C_NAME_IMPATIENT = "Impatient";
	public static final String C_NAME_REORDER = "Chicken";
	public static final String C_NAME_POOR = "Poor";
	public static final String C_NAME_FLAKE = "Flake";
	public static final String C_NAME_CHEAP = "Cheap";

	/**
	 * Constructor for CustomerRole class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(Person person, CityLocation location){
		super(person, location);
		System.out.println("Created CustomerRole for " + person.getName());
		
		person.getWallet().setCashOnHand(20);
		if (getName().equals(C_NAME_POOR))  { person.getWallet().setCashOnHand(0); }
		if (getName().equals(C_NAME_FLAKE)) { person.getWallet().setCashOnHand(0); }
		if (getName().equals(C_NAME_CHEAP)) { person.getWallet().setCashOnHand(6); }
		this.bill = 0;
	}
	
	/**
	 * Activate the CustomerRole, then get hungry.
	 */
	@Override
	public void activate() {
		super.activate();
		gotHungry();
	}

	/**
	 * hack to establish connection to Host.
	 */
	public void setHost(HostRole host) {
		this.host = host;
	}
	
	// Messages

	public void gotHungry() { // from gui
		print("I'm hungry");
		event = CustomerEvent.GOT_HUNGRY;
		// person.setHungerLevel(HungerLevel.HUNGRY);
		stateChanged();
	}

	public void msgFollowMeToTable(Waiter w, Menu m, int tableX, int tableY) { // from waiter
		this.waiter = w;
		this.menu = m;
		this.tableX = tableX;
		this.tableY = tableY;
		event = CustomerEvent.FOLLOW_WAITER;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() { // from gui
		event = CustomerEvent.REACHED_TABLE;
		stateChanged();
	}
	
	public void msgAnimationFinishedDecidingOrder() { // from gui
		// TODO currently this is called in decideOrder instead of gui
		event = CustomerEvent.DECIDED_ORDER;
		multiStepAction.release();
		stateChanged();
	}
	
	public void msgAnimationFinishedOrdering() { // from gui
		// TODO currently this is called in giveOrder instead of gui
		multiStepAction.release();
	}
	
	public void msgWhatWouldYouLike() { // from waiter
		event = CustomerEvent.ASKED_FOR_ORDER;
		stateChanged();
	}

	public void msgOutOfChoice(Menu m) { // from waiter
		this.menu = m;
		event = CustomerEvent.OUT_OF_CHOICE;
		stateChanged();
	} 
	
	public void msgHereIsYourFood() { // from waiter
		event = CustomerEvent.RECEIVED_FOOD;
		stateChanged();
	}
	
	public void msgDoneEating() { // from eatFood timer
		event = CustomerEvent.DONE_EATING;
		person.setHungerLevel(HungerLevel.FULL);
		stateChanged();
	}
	
	@Override
	public void msgHereIsBill(Cashier cashier, double bill) {
		this.cashier = cashier;
		this.bill = bill;
		this.event = CustomerEvent.RECEIVED_BILL;
		this.stateChanged();
	}
	
	@Override
	public void msgHereIsChange(double change) {
		Wallet wallet = person.getWallet();
		wallet.setCashOnHand(wallet.getCashOnHand() + change);
		this.event = CustomerEvent.RECEIVED_CHANGE;
		this.stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() { // from gui
		event = CustomerEvent.DONE_LEAVING;
		stateChanged();
	}
	
	public void msgNoRoom() { // from waiter
		if (getName().equals(C_NAME_IMPATIENT)) {
			event = CustomerEvent.NO_ROOM;
			stateChanged();
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		//	CustomerRole is a finite state machine
		

		if (state == CustomerState.DOING_NOTHING && event == CustomerEvent.GOT_HUNGRY ){
			state = CustomerState.WAITING_IN_RESTAURANT;
			goToRestaurant();
			return true;
		}
		if (state == CustomerState.WAITING_IN_RESTAURANT && event == CustomerEvent.FOLLOW_WAITER ){
			state = CustomerState.BEING_SEATED;
			sitDown();
			return true;
		}
		if (state == CustomerState.WAITING_IN_RESTAURANT && event == CustomerEvent.NO_ROOM) {
			state = CustomerState.LEAVING;
			leaveBecauseNoRoom();
			return true;
		}
		if (state == CustomerState.BEING_SEATED && event == CustomerEvent.REACHED_TABLE
			|| state == CustomerState.ORDERED && event == CustomerEvent.OUT_OF_CHOICE){
			state = CustomerState.SEATED;
			decideOrder();
			return true;
		}
		if (state == CustomerState.SEATED && event == CustomerEvent.DECIDED_ORDER) {
			state = CustomerState.READY_TO_ORDER;
			callWaiter();
			return true;
		}
		if (state == CustomerState.READY_TO_ORDER && event == CustomerEvent.ASKED_FOR_ORDER) {
			state = CustomerState.ORDERED;
			giveOrder();
			return true;
		}
		if (state == CustomerState.ORDERED && event == CustomerEvent.RECEIVED_FOOD) {
			state = CustomerState.EATING;
			eatFood();
			return true;
		}
		if (state == CustomerState.EATING && event == CustomerEvent.DONE_EATING){
			readyToPay();
			state = CustomerState.WAITING_FOR_BILL;
			return true;
		}
		if (state == CustomerState.WAITING_FOR_BILL && event == CustomerEvent.RECEIVED_BILL) {
			pay();
			state = CustomerState.PAYING;
			return true;
		}
		if (state == CustomerState.PAYING && event == CustomerEvent.RECEIVED_CHANGE) {
			state = CustomerState.LEAVING;
			leaveTable();
			return true;
		}
		if (state == CustomerState.LEAVING && event == CustomerEvent.DONE_LEAVING){
			state = CustomerState.DOING_NOTHING;
			deactivate();
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		for (WaitSpot ws : waitSpots) {
			if (ws.tryAcquire()) {
				// There's a spot to wait at, so go there.
				waitSpot = ws;
				customerGui.DoWaitAtSpot(ws.getX(), ws.getY());
				host.msgIWantFood(this); // send our instance, so he can respond to us
				return;
			}
		}
		// There's no spot to wait at!
		event = CustomerEvent.NO_ROOM;
	}
	
	private void leaveBecauseNoRoom() {
		customerGui.DoExitRestaurant(); // animation
		Do("This restaurant is packed! I'm out of here.");
	}

	private void sitDown() {
		// Allow another customer to wait at this spot
		waitSpot.release();
		waitSpot = null;
		customerGui.DoGoToSeat(tableX, tableY); // hack; Role shouldn't know coords
	}
	
	private void decideOrder() {
		Do("Deciding what to order...");
		customerGui.DoDecideOrder(); // animation - disabled (for now?)
		
		// Wait a few seconds "before" deciding
		chooseFood();
		
		timer.schedule(new TimerTask() {
			public void run() {
				// TODO: maybe call this method in gui
				msgAnimationFinishedDecidingOrder();
			}
		},
		decideOrderDelay * 1000);//how long to wait before running task
		
		try {
			multiStepAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void callWaiter() {
		Do("Ready to order");
		customerGui.DoCallWaiter(); // animation - disabled for now
		waiter.msgIAmReadyToOrder(this);
	}
	
	private void giveOrder() {
		customerGui.DoGiveOrder(choiceShort);
		
		// Wait a few seconds before ordering
		timer.schedule(new TimerTask() {
			public void run() {
				// TODO: maybe call this method in gui
				msgAnimationFinishedDecidingOrder();
			}
		},
		sayOrderDelay * 1000);//how long to wait before running task
		
		try {
			multiStepAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Do("Ordered " + choice);
		waiter.msgHereIsMyChoice(this, choice);
	}

	private void eatFood() {
		Do("Eating " + choice);
		customerGui.DoEatFood(choiceShort);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				msgDoneEating();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void readyToPay() {
		Do("Done eating. Ready to pay.");
		customerGui.DoCallWaiter(); // animation
		waiter.msgDoneEating(this);
	}
	
	private void pay() {
		// customerGui.DoPay(); // animation - disabled for now
		Wallet wallet = person.getWallet();
		if (person.getWallet().getCashOnHand() >= bill) {
			// TODO: fix this hack
			Do("Paying $" + df.format(bill));
			
			// pay: no exact change, use full bills
			wallet.setCashOnHand(wallet.getCashOnHand() - Math.ceil(bill));
			 
			cashier.msgHereIsPayment(this, Math.ceil(bill));
		} else {
			Do("I can't afford this meal...");
			cashier.msgHereIsPayment(this, wallet.getCashOnHand());
			wallet.setCashOnHand(0);
		}
		
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeaving(this);
		customerGui.DoExitRestaurant();
		
		Wallet wallet = person.getWallet();
		if (getName().equals(C_NAME_FLAKE)) {
			wallet.setCashOnHand(wallet.getCashOnHand() + 50);
		}
	}

	// Utilities
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	private void chooseFood() {
		if (menu.choices.size() == 0) {
			Do("This restaurant has no food. I'm out of here!");
			this.state = CustomerState.LEAVING;
			this.leaveTable();
			return;
		}
		
		if (getName().equals(C_NAME_POOR)) {
			Do("This restaurant is too expensive... I'm out of here!");
			this.state = CustomerState.LEAVING;
			this.leaveTable();
			return;
		}
		
		if (getName().equals(C_NAME_CHEAP)) {
			for (Menu.Choice c : menu.choices) {
				if (c.name.equals("Salad")) {
					choice = "Salad";
					choiceShort = menu.get(choice).shortName;
					return;
				}
			}
			Do("I can't afford anything but Salad... I'm out of here!");
			this.state = CustomerState.LEAVING;
			this.leaveTable();
			return;
		}
		
		if (getName().equals(C_NAME_REORDER)) {
			for (Menu.Choice c : menu.choices) {
				if (c.name.equals(C_NAME_REORDER)) {
					choice = C_NAME_REORDER;
					choiceShort = menu.get(choice).shortName;
					return;
				}
			}
		}
		
		// Return a random item in menu.choices
		int choiceNum = (int) (Math.random() * menu.choices.size());
		choice = menu.choices.get(choiceNum).name;
		choiceShort = menu.choices.get(choiceNum).shortName;
	}

	public Menu getMenu() {
		return this.menu;
	}
	
	private static class WaitSpot {
		private Semaphore sem = new Semaphore(1, true);
		private int x;
		private int y;
		
		public WaitSpot(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean tryAcquire() { return sem.tryAcquire(); }
		public void release() { sem.release(); }
		
		public int getX() { return x; }
		public int getY() { return y; }
		public void setX(int y) { this.x = x; }
		public void setY(int y) { this.y = y; }
		
	}
	
}

