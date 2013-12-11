package restaurant.anthony;

import CommonSimpleClasses.CityBuilding;
import agent.interfaces.Person;
import restaurant.anthony.RevolvingOrderList.OrderState;
import restaurant.anthony.WaiterRoleBase.MyCustomer;
import restaurant.anthony.RevolvingOrderList.Order;
import restaurant.anthony.interfaces.Cook;
import restaurant.anthony.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */
//The waiter is the agent we see seating customers and taking orders in the GUI
public class WaiterRole extends WaiterRoleBase implements Waiter {
	
	public WaiterRole(Person person, CityBuilding building) {
		super(person, building);
	}

	Cook cook = null;
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#OrderIsReady(restaurant.WaiterAgent.Order)
	 */
	@Override
	public void OrderIsReady(Order o) {
		o.state = OrderState.COOKED;
		for (int i =0 ; i <MyCustomers.size();i++){
			if (MyCustomers.get(i).t == o.table){
				MyCustomers.get(i).order = o;
			}
		}
		stateChanged();
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgNoMoreFood(java.lang.String, restaurant.WaiterAgent.Order)
	 */
	@Override
	public void msgNoMoreFood(String choice, Order o) {

		for (int i = 0; i < MyCustomers.size(); i++) {
			print("Going to Deliver the bad news to customer");
			if (MyCustomers.get(i).t == o.table) {
				MyCustomers.get(i).FailOrder = true;
				stateChanged();
				return;
			}
		}
	}
	
	
	/* --- Actions --- */
	
	protected void GoTakeOrder(MyCustomer c) {
		print ("GoTakeOrder");
		DoGoCustomer(c.c, c.t);
		//c.order = new Order(c.choice, c.t, this, OrderState.PENDING);
		for (int i=0;i<MyCustomers.size();i++){
			if (MyCustomers.get(i) == c){
				print ("Ordered");
				MyCustomers.get(i).state = CustomerState.Ordered;
			}
		}
		c.c.OrderGotIt();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.GoToCookFromTable(c.t);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.HeresTheOrder(c.choice, c.t, this);
	}

	protected void GetOrder() {
		waiterGui.DoLeaveCook();
		state = AgentState.returning;
		for (int i = 0; i < MyCustomers.size(); i++) {
			if (MyCustomers.get(i).order != null) {
				if (!MyCustomers.get(i).order.IsProcessed()) {
					Do("Give Order to Cook");
					cook.HeresTheOrder(MyCustomers.get(i).choice, MyCustomers.get(i).t, this);
				}
			}
		}
		for (int i = 0; i < MyCustomers.size(); i++) {
			if (MyCustomers.get(i).order != null) {
				if (MyCustomers.get(i).order.IsProcessed()
						&& MyCustomers.get(i).state == CustomerState.Ordered) {
					Do("Get Order from Cook");
					cook.msgIGetOrder(MyCustomers.get(i).t);
					
					state = AgentState.TakingFood;
					DeliveOrder(MyCustomers.get(i), MyCustomers.get(i).t);
					
					try {
						atTable.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return;
				}
			}
		}

		return;
	}	
	/* --- Actions --- */
	public void setCook(Cook c){
		cook = c;
	}
	

}

