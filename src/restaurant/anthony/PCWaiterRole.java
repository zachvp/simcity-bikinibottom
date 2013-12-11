package restaurant.anthony;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.interfaces.Person;
import restaurant.anthony.RevolvingOrderList.Order;
import restaurant.anthony.RevolvingOrderList.OrderState;
import restaurant.anthony.WaiterRoleBase.CustomerState;
import restaurant.anthony.interfaces.Waiter;


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
			timerSet = true;
			Runnable command = new Runnable() {
				public void run(){
					synchronized(revolvingOrders) {
						for (int i = 0 ; i< MyCustomers.size(); i++){
							for(restaurant.anthony.RevolvingOrderList.Order o : revolvingOrders.orderList) {
								if(o != null && o.state == OrderState.COOKED && MyCustomers.get(i).order == o) {
									checkRevolvingOrderList(o);
								}
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
	
	private void checkRevolvingOrderList(restaurant.anthony.RevolvingOrderList.Order o) {
		waiterGui.GoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MyCustomer mc = findCustomerOrder(o);

		
		if(o.state == OrderState.OUT_OF_CHOICE) {
			this.tellCustomerOutOfFood(mc);
			return;
		}
		
		o.state = OrderState.PICKED_UP;
		//cook.msgIGetOrder(o.table-1);
		getFood(mc);

		//stateChanged();
	}
	
	private void tellCustomerOutOfFood(MyCustomer mc) {
		waiterGui.GoToTable(mc.t);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgEpicFail();
		waiterGui.DoLeaveCustomer(mc.t);
		for (int i =0; i<MyCustomers.size();i++){
			if (mc == MyCustomers.get(i)){
				MyCustomers.remove(i);
			}
		}
		return;
		
	}

	private MyCustomer findCustomerOrder(restaurant.anthony.RevolvingOrderList.Order o) {
		for(MyCustomer mc : MyCustomers) {
			if(mc.t == o.table) {
				return mc;
			}
		}
		return null;
	}
	
	public void setRevolvingOrders(RevolvingOrderList orderList) {
		this.revolvingOrders = orderList;
	}
	
	@Override
	protected void GoTakeOrder(MyCustomer c) {
		waiterGui.GoToTable(c.t);
		for (int i=0;i<MyCustomers.size();i++){
			if (MyCustomers.get(i) == c){
				print ("Ordered");
				MyCustomers.get(i).state = CustomerState.Ordered;
			}
		}
		
		
		
		
		waiterGui.GoToCookFromTable(c.t);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCook();
		
		Do("Customer choice " + c.choice);
		
		/*
		c.order = revolvingOrders.getNewOrder(c.choice, c.t, this, OrderState.NEED_TO_COOK);
		revolvingOrders.orderList.add(c.order);
		*/
		revolvingOrders.orderList.add(revolvingOrders.getNewOrder(c.choice, c.t, this, OrderState.NEED_TO_COOK));
		c.order = revolvingOrders.orderList.get(0);
		
		System.out.println(c.order.choice);
		
		
		stateChanged();
	}
	
	protected void GetOrder(){
		
	}

	protected void getFood(MyCustomer c) {

		
		waiterGui.GoToTable(c.t);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.c.HeresYourOrder(c.order);
		waiterGui.DoLeaveCustomer(c.t);
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void OrderIsReady(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoMoreFood(String choice, Order o) {
		// TODO Auto-generated method stub
		
	}
	
}