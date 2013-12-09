package restaurant.vegaperk.backend;

import java.util.ConcurrentModificationException;

import CommonSimpleClasses.CityBuilding;
import agent.interfaces.Person;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */
//The waiter is the agent we see seating customers and taking orders in the GUI
public class WaiterRole extends WaiterRoleBase implements Waiter {
	
	public WaiterRole(Person person, CityBuilding building) {
		super(person, building);
	}

	CookRole cook = null;
	
	public void setCook(CookRole c){
		cook = c;
	}
	
	
	public boolean pickAndExecuteAnAction() {
		try{
			for (MyCustomer c : customers) {
				if (c.state==MyCustomerState.WAITING) {
					c.state = MyCustomerState.SEATED;
					seatCustomer(c, c.table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
			for(MyCustomer c : customers){
				if(c.state==MyCustomerState.READY_TO_ORDER){
					c.state = MyCustomerState.ORDERED;
					takeOrder(c);
					return true;
				}
			}
			for(MyCustomer c : customers){
				if(c.state==MyCustomerState.FOOD_READY){
					c.state = MyCustomerState.SERVED;
					getFood(c);
					return true;
				}
			}
			for(MyCustomer c : customers){
				if(c.state==MyCustomerState.LEAVING){
					c.state = MyCustomerState.DONE;
					tellHostFreeTable(c.table);
					return true;
				}
			}
			for(MyCustomer c : customers){
				if(c.state==MyCustomerState.OUT_OF_CHOICE){
					c.state = MyCustomerState.SEATED;
					tellCustomerOutOfFood(c);
					return true;
				}
			}
			for(MyCustomer c : customers){
				if(c.state==MyCustomerState.DONE_EATING){
					c.state = MyCustomerState.PAYING;
					getCheck(c);
				}
				if(breakState == BreakState.OFF_BREAK){
					breakState = BreakState.NONE;
					goOffBreak();
					return true;
				}
			}	
			if(breakState == BreakState.GOING_ON_BREAK){
				boolean hasCustomers = false;
				for(MyCustomer c : customers){
					if(c.state != MyCustomerState.DONE){
						hasCustomers = true;
					}
				}
				if(hasCustomers == false){
					breakState = BreakState.ON_BREAK;
					goOnBreak();
				}
				return true;
			}
		}
			
		catch(ConcurrentModificationException e){
			return true;
		}
		
		goWait();
		return false;
	}
	
	
	
	private void takeOrder(MyCustomer c){
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
	
	private void getFood(MyCustomer c){
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
	
	private void DoGoToCook(){
		waiterGui.DoGoToCook();
	}

}