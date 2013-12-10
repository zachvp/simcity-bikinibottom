package restaurant.vegaperk.backend;

import restaurant.vegaperk.backend.WaiterRoleBase.Menu;
import restaurant.vegaperk.gui.CustomerGui;
import restaurant.vegaperk.gui.CustomerGui.OrderState;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Customer;
import restaurant.vegaperk.interfaces.Waiter;
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.gui.Gui;
import agent.interfaces.Person;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer {
	private String name;
	private String choice;
	private int HUNGER_LEVEL = 6;// determines length of meal
	
	// sets time delays
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	private CustomerGui gui;
	
	@SuppressWarnings("serial")
	private static List<WaitZone> waitZones = new ArrayList<WaitZone>(){
		{
			for(int i = 0; i < 14; i++){
				add(new WaitZone(30, 20 + i*30));
			}
		}
	};
	
	//so the customer knows where to go
	private int tableX;
	private int tableY;

	// agent correspondents
	private HostRole host;
	private Waiter waiter;
	private Cashier cashier;
	private WaitZone myWaitZone;
	
	private double money;
	private double bill;
	
	private Menu menu;

	//State and event enumerations
	private enum CustomerState { 
		DOING_NOTHING,
		WAITING_IN_RESTAURANT,
		BEING_SEATED,
		SEATED,
		READY_TO_ORDER,
		ORDERING,
		ORDERED,
		EATING,
		DONE_EATING,
		WAITING_FOR_BILL,
		PAYING,
		LEAVING
	};
	
	private enum CustomerEvent {
		NONE,
		GOT_HUNGRY,
		FOLLOW_WAITER,
		SEATED,
		ASKED_FOR_ORDER,
		EAT,
		DONE_EATING,
		RECEIVED_BILL,
		RECEIVED_CHANGE,
		DONE_LEAVING,
		CHOOSE_FOOD,
		OUT_OF_FOOD,
		NO_ROOM
	};
	
	CustomerEvent event = CustomerEvent.NONE;
	private CustomerState state = CustomerState.DOING_NOTHING;//The start state

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(Person person, CityBuilding building){
		super(person, building);
		this.name = super.getName();
		
		bill = 0.00;
	}

	/* --- Messages from other agents --- */
	// from host
	public void gotHungry() {//from animation
		Do("I'm hungry");
		
		money = person.getWallet().getCashOnHand();
		event = CustomerEvent.GOT_HUNGRY;
		stateChanged();
	}
	public void msgTablesAreFull(){
		Do("Can't sit anywhere!");
		event = CustomerEvent.NO_ROOM;
		stateChanged();
	}
	
	// from waiter
	@Override
	public void msgSitAtTable(Waiter w, Menu m, int x, int y) {
		waiter = w;
		menu = m;
		tableX = x;
		tableY = y;
		event = CustomerEvent.FOLLOW_WAITER;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		event = CustomerEvent.CHOOSE_FOOD;
		stateChanged();
	}
	
	public void msgHereIsYourFood(){
		event = CustomerEvent.EAT;
		gui.orderState = OrderState.SERVED;
		stateChanged();
	}
	
	public void msgOutOfChoice(String c){
		menu.m.remove(c);
		event = CustomerEvent.OUT_OF_FOOD;
		Do("can't get "+c);
		stateChanged();
	}
	
	// from Cashier
	public void msgHereIsCheck(double check, Cashier cash){
		cashier = cash;
		event = CustomerEvent.RECEIVED_BILL;
		bill += check;
		stateChanged();
	}
	
	public void msgHereIsChange(double change){
		if(change > 0){
			money += change;
		}else{
			bill -= change;
		}
		event = CustomerEvent.RECEIVED_CHANGE;
		stateChanged();
	}
	
	// from customer gui
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = CustomerEvent.SEATED;
		stateChanged();
	}
	
	// from animation 
	public void msgAnimationFinishedLeaveRestaurant() {
		event = CustomerEvent.DONE_LEAVING;
		stateChanged();
	}

	/* --- Scheduler --- */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
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
		
		if(state == CustomerState.WAITING_IN_RESTAURANT && event == CustomerEvent.NO_ROOM){
			state = CustomerState.LEAVING;
			leaveTable();
			return true;
		}
		
		if (state == CustomerState.BEING_SEATED && event == CustomerEvent.SEATED){
			state = CustomerState.SEATED;
			readyToOrder();
			return true;
		}
		
		if(state == CustomerState.SEATED && event == CustomerEvent.CHOOSE_FOOD){
			state = CustomerState.ORDERING;
			chooseFood();
			return true;
		}
		
		if (state == CustomerState.ORDERING && event == CustomerEvent.ASKED_FOR_ORDER){
			state = CustomerState.ORDERED;
			hereIsMyOrder();
			return true;
		}
		
		if(state == CustomerState.ORDERED && event == CustomerEvent.EAT){
			state = CustomerState.EATING;
			EatFood();
			return true;
		}
		
		if (state == CustomerState.EATING && event == CustomerEvent.DONE_EATING){
			state = CustomerState.WAITING_FOR_BILL;
			readyToPay();
			return true;
		}
		if (state == CustomerState.WAITING_FOR_BILL && event == CustomerEvent.RECEIVED_BILL){
			state = CustomerState.PAYING;
			pay();
			return true;
		}
		
		if (state == CustomerState.PAYING && event == CustomerEvent.RECEIVED_CHANGE){
			state = CustomerState.LEAVING;
			leaveTable();
			return true;
		}
		
		if(state == CustomerState.ORDERED && event == CustomerEvent.OUT_OF_FOOD){
			state = CustomerState.SEATED;
			readyToOrder();
			return true;
		}
		
		if (state == CustomerState.LEAVING && event == CustomerEvent.DONE_LEAVING){
			state = CustomerState.DOING_NOTHING;
			//no action
			return true;
		}
		
		return false;
	}

	/** Actions executed from the scheduler */
	private void goToRestaurant() {
		Do("Going to restaurant");
		for(WaitZone wz : waitZones){
			if(wz.tryAcquire()){
				myWaitZone = wz;
				host.msgIWantFood(this);//send our instance, so he can respond to us
				gui.DoGoWait(wz.x, wz.y);
				return;
			}
		}
		event = CustomerEvent.NO_ROOM;
		Do("By the beard of Zeus it's crowded!");
	}

	private void sitDown() {
		Do("Being seated. Going to table");
		myWaitZone.releaseZone();
		myWaitZone = null;
		gui.DoGoToSeat(tableX, tableY);
	}
	
	private void chooseFood(){
		gui.setChoice("");
		gui.orderState = OrderState.NONE;
		Runnable command = new Runnable() {
			public void run(){
				event = CustomerEvent.ASKED_FOR_ORDER;
				stateChanged();
			}
		};
		
		// resident role will deactivate after the delay below
		schedule.scheduleTaskWithDelay(command, HUNGER_LEVEL * Constants.MINUTE);
	}

	private void readyToOrder(){
//		chooseFood();
		Do("Ready to order");
		waiter.msgReadyToOrder(this);
	}
	
	private void hereIsMyOrder(){
		List<String> keys = new ArrayList<String>(menu.m.keySet());
		Collections.shuffle(keys);
		choice = keys.get(0).toString();
		Do("Ordered " + choice);
		
		gui.orderState = OrderState.DECIDED;
		gui.setChoice(choice);
		
		waiter.msgHereIsMyOrder(this, choice);
	}
	private void EatFood() {
		Do("Eating Food");
		
		Runnable command = new Runnable() {
			public void run(){
				event = CustomerEvent.DONE_EATING;
				person.getWallet().setCashOnHand(money);
				person.setHungerLevel(HungerLevel.FULL);
				stateChanged();
			}
		};
		
		// resident role will deactivate after the delay below
		schedule.scheduleTaskWithDelay(command, HUNGER_LEVEL * Constants.MINUTE);
	}
	private void readyToPay(){
		waiter.msgIAmDoneEating(this);
	}
	private void pay(){
		cashier.msgHereIsPayment(this, money);
		money -= bill;
		if(money < 0){
			money = 0.00;
		}
	}

	private void leaveTable() {
		Do("Leaving.");

		if(waiter != null){
			waiter.msgCustomerLeavingTable(this);
		}
		
		gui.DoExitRestaurant();
		
		deactivate();
	}

	/** Utilities */
	
	/** Classes */
	private static class WaitZone{
		private Semaphore position = new Semaphore(1, true);
		int x;
		int y;
		
		WaitZone(int dx, int dy){
			x = dx;
			y = dy;
		}
		
		public boolean tryAcquire(){
			return position.tryAcquire();
		}
		public void releaseZone(){
			position.release();
		}
	}
	
	/** Accessors and setters */
	public String getCustomerName() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return HUNGER_LEVEL;
	}

	public void setHungerLevel(int hungerLevel) {
		this.HUNGER_LEVEL = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		this.gui = g;
	}

	public Gui getGui() {
		return gui;
	}
	
	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostRole host) {
		this.host = host;
	}
	
	@Override
	public void Do(String msg) {
		Do(AlertTag.RESTAURANT, msg);
	}
}