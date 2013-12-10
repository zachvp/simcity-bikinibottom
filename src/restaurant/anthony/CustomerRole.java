package restaurant.anthony;

import restaurant.anthony.CashierRole.Check;
import restaurant.anthony.WaiterRole.Menu;
import restaurant.anthony.WaiterRole.Order;
import restaurant.anthony.gui.CustomerGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;
import CommonSimpleClasses.CityLocation;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.WorkRole;
import agent.interfaces.Person;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role implements Customer {
	
	
	private int hungerLevel = 5; // determines length of meal
	private int currentTable;
	private double money = 0;
	private double debt = 0;
	Menu menu = null;
	String choice;
	Timer timer = new Timer();
	Check check = null;
	private CustomerGui customerGui;
	
	private Semaphore atWaitingLine = new Semaphore(0,true);

	// agent correspondents
	private HostRole host;
	private Waiter waiter;
	private Cashier cashier;

	
	public enum AgentState {
		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, AboutToOrder, Ordered, Eating, DoneEating, ReadyForCheck, GoToCashier, NowLeave, Leaving
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {
		none, gotHungry, followWaiter, seated, doneChoosing, served, doneEating, PayingBill, doneLeaving, AtCashier
	};

	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 * @param gui
	 *            reference to the customergui so the customer can send it
	 *            messages
	 */
	public CustomerRole(Person person, CityLocation location) {
		super(person, location);
	}
	
	/**
	 * Activate the CustomerRole, then get hungry.
	 */
	@Override
	public void activate() {
		money = person.getWallet().getCashOnHand();
		super.activate();
		customerGui.setHungry();
	}


	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	@Override
	public void setHost(HostRole host) {
		this.host = host;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setWaiter(restaurant.WaiterAgent)
	 */
	@Override
	public void setWaiter(Waiter wa) {
		this.waiter = wa;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.CashierAgent)
	 */
	@Override
	public void setCashier(Cashier ca) {
		this.cashier = ca;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return person.getName();
	}

	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	@Override
	public void gotHungry() {// from animation
		print("I'm hungry and I have $" + money);
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgYouAreInLine (int x){
		customerGui.InLine(x);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgSitAtTable(int, restaurant.WaiterAgent, restaurant.WaiterAgent.Menu)
	 */
	@Override
	public void msgSitAtTable(int i, Waiter wa, Menu M) {
		print("Received msgSitAtTable");
		event = AgentEvent.followWaiter;
		currentTable = i;
		waiter = wa;
		menu = M;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgOrderFail(restaurant.WaiterAgent.Menu)
	 */
	@Override
	public void msgOrderFail(Menu M) {
		print("Get The Bad News");
		menu = M;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#OrderGotIt()
	 */
	@Override
	public void OrderGotIt() {
		state = AgentState.Ordered;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#HeresYourOrder(restaurant.WaiterAgent.Order)
	 */
	@Override
	public void HeresYourOrder(Order o) {
		if (o.process == true) {
			print("Served and I am happy now");
			state = AgentState.Eating;
			EatFood();
			stateChanged();
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#HeresYourCheck(restaurant.CashierAgent.Check)
	 */
	@Override
	public void HeresYourCheck(Check ch) {
		check = ch;
		event = AgentEvent.PayingBill;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#HereIsYourChange(double)
	 */
	@Override
	public void HereIsYourChange(double change) {
		print("I got my change :" + change);
		if (change > 0)
			person.getWallet().setCashOnHand(change);
		else
			person.getWallet().setCashOnHand(0);
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#HereIsYourDebt(double)
	 */
	@Override
	public void HereIsYourDebt(double de) {
		debt = de;
		print("I got my debt :" + debt);
	}

	public void msgGoToAssignedSpace(int x){
		customerGui.GoToAssignedSpace(x);
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	@Override
	public void msgAnimationFinishedGoToSeat() {
		// from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToCashier()
	 */
	@Override
	public void msgAnimationFinishedGoToCashier() {
		event = AgentEvent.AtCashier;
		stateChanged();
	}

	public void atWaitingLine(){
		atWaitingLine.release();
	}
	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.Seated;
			ChooseFood();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.doneChoosing) {
			OrderFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.ReadyForCheck;
			ReadyForCheck();
			return true;
		}

		if (state == AgentState.ReadyForCheck && event == AgentEvent.PayingBill) {
			state = AgentState.GoToCashier;
			GoToCashier();
			return true;
		}

		if (state == AgentState.GoToCashier && event == AgentEvent.AtCashier) {
			state = AgentState.NowLeave;
			PayBill();
			return true;
		}

		if (state == AgentState.NowLeave && event == AgentEvent.AtCashier) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}

		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			this.deactivate();
			person.setHungerLevel(HungerLevel.FULL);
			// no action
			return true;
		}
		return false;
	}

	// Actions
	private void GoToCashier() {
		customerGui.DoGoToCashier();
	}

	private void PayBill() {
		print("in PayBill");
		cashier.Payment(check, money, this);
		customerGui.FinishCheck();
		state = AgentState.NowLeave;
		stateChanged();
	}

	private void goToRestaurant() {
		Do("Going to restaurant");
		if (debt != 0) {
			print("Pay debt for the previous meal");
			cashier.PayDebt(debt);
			money = money - debt;
			debt = 0;
		}
		customerGui.GoToWaitingLine();
		try {
			atWaitingLine.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		host.msgIWantFood(this);// send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		//host.msgIAmGoingToSeat();
		customerGui.DoGoToSeat(currentTable);// hack; only one table
		
	}

	private void ChooseFood() {
		Do("Thinking what to order.");
		print("I have that much " + money + " of Money");

		timer.schedule(new TimerTask() {
			public void run() {

				if (money < 9.99) {
					double rand = Math.random() * 2;
					int decision = (int) rand;
					System.out.println(decision);

					if (decision == 0) {
						// no money to pay but order it anyway
						print("I dun hv money but im going to eat anyway");
						String SelectedFood = menu.Menulist.get((int) (Math
								.random() * menu.Menulist.size())).name;
						choice = SelectedFood;
						Do(SelectedFood + " is selected");
						event = AgentEvent.doneChoosing;
						stateChanged();
						return;

					} else {
						// leaveTable
						leaveTable();
						state = AgentState.DoingNothing;
						event = AgentEvent.none;
						return;
					}
				}
				// Random to get the first decision of food
				int i = (int) (Math.random() * menu.number);
				// Compare if the customer has enough money
				while (menu.Menulist.get(i).price > money) {
					print("Not enough money for eating "
							+ menu.Menulist.get(i).name);
					// Eliminate the possibility of that food
					menu.Menulist.remove(i);
					menu.number--;
					// Random pick decision again
					i = (int) (Math.random() * menu.number);

				}
				// normal case
				String SelectedFood = menu.Menulist.get(i).name;
				choice = SelectedFood;
				Do(SelectedFood + " is selected");
				event = AgentEvent.doneChoosing;
				// isHungry = false;
				stateChanged();
			}
		}, 2000);
	}

	private void OrderFood() {
		Do("Ask waiter to come");
		waiter.HeresMyChoice(this, choice);
		customerGui.Food(choice);
		state = AgentState.AboutToOrder;
		stateChanged();
	}

	private void EatFood() {
		Do("Eating Food");
		// This next complicated line creates and starts a timer thread.
		// We schedule a deadline of getHungerLevel()*1000 milliseconds.
		// When that time elapses, it will call back to the run routine
		// located in the anonymous class created right there inline:
		// TimerTask is an interface that we implement right there inline.
		// Since Java does not all us to pass functions, only objects.
		// So, we use Java syntactic mechanism to create an
		// anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;

			public void run() {
				print("Done eating, cookie=" + cookie);
				customerGui.FinishFood();
				event = AgentEvent.doneEating;
				// isHungry = false;
				stateChanged();
			}
		}, 5000);// getHungerLevel() * 1000);//how long to wait before running
					// task
	}

	private void ReadyForCheck() {
		waiter.AskForCheck(this);
	}

	private void leaveTable() {
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return person.getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		// could be a state change. Maybe you don't
		// need to eat until hunger lever is > 5?
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	@Override
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	@Override
	public CustomerGui getGui() {
		return customerGui;
	}
}
