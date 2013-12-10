package restaurant.lucas;

import java.awt.Dimension;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.interfaces.Person;
import restaurant.lucas.WaiterRoleBase.MyCustomer;
import restaurant.lucas.WaiterRoleBase.customerState;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;

public class PCWaiterRole extends WaiterRoleBase implements Waiter {

	OrderWheel orderWheel;
	
	boolean timeToCheckOrderWheel= false;
	private ScheduleTask schedule = ScheduleTask.getInstance();
	int CHECK_ORDER_WHEEL_TIME;
	
	public PCWaiterRole(Person p, CityLocation c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	//MESSAGES

	private void checkOrderWheel() {
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(!atWork) {
			goToWork();
			return true;
		}
		
		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.waiting) {
					seatCustomer(c.customer, c.table);
					c.state = customerState.seated;
					return true;
				}
			}
		}
		//COOK STUFF BELOW
		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) { 
				if(c.state == customerState.readyToOrder) {
					takeOrder(c);
					c.state = customerState.ordered;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.needToReorder) {
					reTakeOrder(c);
					c.state = customerState.seated;
					return true;
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.orderIsReady) {
					deliverOrder(c.customer, c.table, c.choice);
					c.state=customerState.eating;
					return true;
				}
			}
		}
		//END COOK STUFF
		
		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.doneEating) {
					getCheck(c);
					c.state = customerState.paying;
					return true;//TODO added return true
				}
			}
		}

		synchronized(myCustomers) {
			for(MyCustomer c : myCustomers) {
				if(c.state == customerState.leaving) {
					callHost(c);
					c.state=customerState.done; 
					return true;
				}	
			}
		}
		
		if(endWorkDay) {
			endWorkDay();
			return true;
		}
		
		if(timeToCheckOrderWheel) {
			timeToCheckOrderWheel = false;
			Runnable command = new Runnable() {
				@Override
				public void run() {
					checkOrderWheel();
				}
			};
			schedule.scheduleTaskWithDelay(command, CHECK_ORDER_WHEEL_TIME * Constants.MINUTE);
			return true;
			//does not return true to call sched again
		}
		
		return false;
	}

	@Override
	public void takeOrder(MyCustomer mc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliverOrder(Customer cust, int table, String choice) {
		// TODO Auto-generated method stub
		
	}
	
	//utils
	
	public void setOrderWheel(OrderWheel o) {
		orderWheel = o;
	}


	
}