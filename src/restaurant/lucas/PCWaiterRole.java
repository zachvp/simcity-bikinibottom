package restaurant.lucas;

import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;
import restaurant.lucas.interfaces.Waiter;

public class PCWaiterRole extends WaiterRoleBase implements Waiter {

	public PCWaiterRole(Person p, CityLocation c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean pickAndExecuteAnAction() {

		return false;
	}
	
}