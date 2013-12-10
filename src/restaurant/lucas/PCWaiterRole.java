package restaurant.lucas;

import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;
import restaurant.lucas.WaiterRoleBase.MyCustomer;
import restaurant.lucas.WaiterRoleBase.customerState;
import restaurant.lucas.interfaces.Waiter;

public class PCWaiterRole extends WaiterRoleBase implements Waiter {

	public PCWaiterRole(Person p, CityLocation c) {
		super(p, c);
		// TODO Auto-generated constructor stub
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
		//COOK STUFF TO BE UPDATED
		
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
		
		return false;
	}
	
}