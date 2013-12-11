package restaurant.vegaperk.backend;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.interfaces.Person;
import restaurant.vegaperk.backend.RevolvingOrderList.Order;
import restaurant.vegaperk.backend.RevolvingOrderList.OrderState;
import restaurant.vegaperk.interfaces.Waiter;


/**
 * Restaurant Waiter Agent
 */
//The waiter is the agent we see seating customers and taking orders in the GUI
public class PCWaiterRole extends WaiterRoleBase implements Waiter {
	// prevents the scheduler fro repeatedly running the time delay
	private boolean timerSet = false;
	
	// TODO schedules the time delay
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	// TODO shared order list that cook also holds
	private RevolvingOrderList revolvingOrders;
	
	private final int CHECK_REVOLVING_LIST_TIME = 5;
	
	public PCWaiterRole(Person person, CityBuilding building) {
		super(person, building);
	}
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO call the base pick and execute
		super.pickAndExecuteAnAction();
		
		// TODO just like the cook, check the revolving order list based on a time interval
		if(!timerSet) {
			timerSet = true;
			Runnable command = new Runnable() {
				public void run(){
					synchronized(revolvingOrders) {
						for(Order o : revolvingOrders.orderList) {
							if(findCustomerOrder(o) != null && o.state == OrderState.FINISHED) {
								checkRevolvingOrderList(o);
							}
						}
					}
					stateChanged();
					timerSet = false;
				}
			};
			
			schedule.scheduleTaskWithDelay(command,
			CHECK_REVOLVING_LIST_TIME * Constants.MINUTE);
			return true;
		}
		
		return false;
	}
	
	// TODO unique to PC waiter, called from the one scheduled action in the overloaded
	// scheduler
	private void checkRevolvingOrderList(Order o) {
		DoGoToCook();
		waitForInput();
		
		MyCustomer mc = findCustomerOrder(o);
		if(mc == null){
			DoGoWait();
			return;
		}
		
		if(o.state == OrderState.OUT_OF_CHOICE) {
			this.tellCustomerOutOfFood(mc);
			return;
		}
		
		o.state = OrderState.PICKED_UP;
		getFood(mc);
	}
	
	private MyCustomer findCustomerOrder(Order o) {
		for(MyCustomer mc : customers) {
			if(mc.table == o.table) {
				return mc;
			}
		}
		return null;
	}
	
	// TODO set the list
	public void setRevolvingOrders(RevolvingOrderList orderList) {
		this.revolvingOrders = orderList;
	}
	
	// TODO implement abstract methods
	@Override
	protected void takeOrder(MyCustomer c) {
		DoGoToTable(c.table);
		waitForInput();
		
		c.c.msgWhatWouldYouLike();
		waitForInput();
		
		if(c.state == MyCustomerState.LEAVING){
			return;
		}
		
		waiterGui.toggleHoldingOrder();
		DoGoToCook();
		waitForInput();
		
		Do("Customer choice " + c.choice);
		
		revolvingOrders.addOrder(c.choice, c.table, this, OrderState.NEED_TO_COOK);
		
		waiterGui.toggleHoldingOrder();
		stateChanged();
	}

	@Override
	protected void getFood(MyCustomer c) {
		if(c.state == MyCustomerState.LEAVING){
			return;
		}
		
		waiterGui.setOrderName(c.choice);
		waiterGui.toggleHoldingOrder();
		
		Do("Going to table");
		DoGoToTable(c.table);
		waitForInput();
		
		c.c.msgHereIsYourFood();
		waiterGui.toggleHoldingOrder();
	}
	
}