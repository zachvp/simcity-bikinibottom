package restaurant.anthony.test.mock;


import restaurant.anthony.HostRole;
import restaurant.anthony.CashierRole.Check;
import restaurant.anthony.WaiterRole.Menu;
import restaurant.anthony.WaiterRole.Order;
import restaurant.anthony.gui.CustomerGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;


public class MockCustomer extends Mock implements Customer {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

	public  void setHost(HostRole host){}

	public  void setWaiter(Waiter wa){}

	public  void setCashier(Cashier ca){cashier = ca;}

	public String getCustomerName(){return "Customer Name";}
	
	public  String getName(){return "Customer Name";}

	public  int getHungerLevel(){return 150;}

	public  void setHungerLevel(int hungerLevel){}

	public  String toString(){return "String";}

	public  void setGui(CustomerGui g){}

	public  CustomerGui getGui(){return null;}

	public  void gotHungry(){}

	public  void msgSitAtTable(int i, Waiter wa, Menu M){}

	public  void msgOrderFail(Menu M){}

	public  void OrderGotIt(){}

	public  void HeresYourOrder(Order o){}

	public  void HeresYourCheck(Check ch){
		
		log.add(new LoggedEvent("Received HeresYourCheck from cashier. Total = "+ ch.getPrice()));
		cashier.Payment(ch, 100, this);

	}

	public  void HereIsYourChange(double change){
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
	}

	public  void HereIsYourDebt(double de){
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ de));
	}

	public  void msgAnimationFinishedGoToSeat(){}

	public  void msgAnimationFinishedLeaveRestaurant(){}

	public  void msgAnimationFinishedGoToCashier(){}

	@Override
	public void msgYouAreInLine(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atWaitingLine() {
		// TODO Auto-generated method stub
		
	}


	
	
	/*
	 * 
	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
		
		
	}

	 */


}
