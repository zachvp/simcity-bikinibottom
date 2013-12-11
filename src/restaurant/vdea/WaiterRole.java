package restaurant.vdea;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
import restaurant.vdea.gui.*;
import restaurant.vdea.interfaces.*;

public class WaiterRole extends WaiterRoleBase implements Waiter{


	private Cook cook;
	//public WaiterGui waiterGui = null;
	

	public WaiterRole(Person person, CityLocation location) {
		super(person, location);
		this.name = super.getName();
	}
	

	public void setCook(Cook newCook){
		cook = newCook;
	}

	// Actions

	protected void sendToKitchen(MyCustomer customer){
		print("going to kitchen");
		waiterGui.DoGoToKitchen();
		//if(host.sen || customers.size()>1){
		atTable.release();
		//}

		try {
			atKitchen.acquire();
			atTable.acquire(); //TODO<-------
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();//returns home, so makes waiter not busy

		print("Order for the kitchen");
		customer.setState(CustomerState.waitingForFood);
		cook.msgThereIsAnOrder(this, customer.choice, customer.table);
	}

	

	protected void deliverOrder(MyCustomer customer){	//called when cook responds with cooked food
		waiterGui.DoGoToKitchen();
		print("going to kitchen");
		try {
			atKitchen.acquire();
		} catch (InterruptedException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}
		cook.gotFood(customer.table);

		Do("Delivering order");
		waiterGui.delivering(customer.choice);//adds food image
		waiterGui.DoGoToTable(customer.table);	//move

		//waiterGui.DoServe(customer.c, customer.table);	// will use

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}

		waiterGui.DoLeaveCustomer();
		waiterGui.delivered();
		customer.c.msgHereIsYourFood();


		customer.setState(CustomerState.served);
	}





}
