package restaurant.vegaperk.backend;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.interfaces.Person;
import restaurant.vegaperk.backend.RevolvingOrderList.OrderState;
import restaurant.vegaperk.backend.WaiterRoleBase.MyCustomerState;
import restaurant.vegaperk.interfaces.Waiter;


/**
 * Restaurant Waiter Agent
 */
//The waiter is the agent we see seating customers and taking orders in the GUI
public class PCWaiterRole extends WaiterRoleBase implements Waiter {
	// prevents the scheduler fro repeatedly running the time delay
	private boolean timerSet = false;
	
	// schedules the time delay
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	//shared order list
	private RevolvingOrderList revolvingOrders;
	
	private final int CHECK_REVOLVING_LIST_TIME = 5;
	
	public PCWaiterRole(Person person, CityBuilding building) {
		super(person, building);
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		super.pickAndExecuteAnAction();
		
		if(!timerSet) {
			Runnable command = new Runnable() {
				public void run(){
					checkRevolvingOrderList();
				}
			};
			timerSet = true;
			
			schedule.scheduleTaskWithDelay(command,
			CHECK_REVOLVING_LIST_TIME * Constants.MINUTE);
			return true;
		}
		
		return false;
	}
	
	private void checkRevolvingOrderList() {
		DoGoToCook();
		waitForInput();
	}
	
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
		// TODO Auto-generated method stub
		
	}
	
}