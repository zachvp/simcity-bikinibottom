package restaurant.vegaperk.backend;

import CommonSimpleClasses.CityBuilding;
import agent.interfaces.Person;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */
//The waiter is the agent we see seating customers and taking orders in the GUI

// TODO step 4: extend the old waiter role from the new base class  
public class WaiterRole extends WaiterRoleBase implements Waiter {
	
	public WaiterRole(Person person, CityBuilding building) {
		super(person, building);
	}

	// TODO Move the data and messages referencing the cook into this Waiter
	CookRole cook = null;
	
	/** Messages from cook */
	public void msgOrderDone(String choice, int t){
		for(MyCustomer mc : customers){
			if(mc.table == t){
				mc.state = MyCustomerState.FOOD_READY;
			}
		}
		stateChanged();
	}
	public void msgOutOfChoice(String choice, int t){
		for(MyCustomer mc : customers){
			if(mc.table == t){
				mc.state = MyCustomerState.OUT_OF_CHOICE;
			}
		}
		stateChanged();
	}
	
	
	/* --- Actions --- */
	// TODO implement abstract takeOrder
	protected void takeOrder(MyCustomer c){
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
		
		Do("choice "+c.choice);
		cook.msgHereIsOrder(this, c.choice, c.table);
		waiterGui.toggleHoldingOrder();
		stateChanged();
	}
	
	// TODO implement abstract getFood()
	protected void getFood(MyCustomer c){
		if(c.state == MyCustomerState.LEAVING){
			return;
		}
		
		DoGoToCook();
		waitForInput();
		
		cook.msgGotFood(c.table);
		
		waiterGui.setOrderName(c.choice);
		waiterGui.toggleHoldingOrder();
		
		DoGoToTable(c.table);
		waitForInput();
		
		c.c.msgHereIsYourFood();
		waiterGui.toggleHoldingOrder();
	}
	
	/* --- Actions --- */
	public void setCook(CookRole c){
		cook = c;
	}
}

