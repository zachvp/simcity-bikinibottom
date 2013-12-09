package restaurant.test.mock;

import restaurant.interfaces.*;

public class MockWaiter extends Mock implements Waiter {

	
	public Cashier cashier;
	public Cook cook;
	public Customer customer;
	public Customer customer2;
	public EventLog log = new EventLog();
	
	public MockWaiter(String name){
		super(name);
	}
	
	
	public void msgSeatCustomer(Customer customer, int tableNumber) {
		log.add(new LoggedEvent("Recieved SeatCustomer from Host. Table = " + tableNumber));
		System.out.println(getName() + ": Assigned customer " + customer.getName());
	}

	
	public void msgReadyToOrder(Customer customer) {
		log.add(new LoggedEvent("Recieved ReadyToOrder from Customer " + customer.getName()));
		System.out.println(getName() + ": Customer is ready to order");
		//customer.setState(CustomerState.none
		customer.msgWhatWouldYouLike();
	}

	
	public void msgHereIsMyOrder(Customer customer, String choice) {
		log.add(new LoggedEvent("Recieved HereIsMyOrder from customer. Choice = " + choice));
		System.out.println(getName() + ": took order of " + choice + " from " + customer.getName());
		//customer.setState(CustomerState.waitingForFood);
		//cook.msgThereIsAnOrder(this, choice, customer.table);
	}

	
	public void msgNotEnoughFood(String choice, int t) {
		log.add(new LoggedEvent("Recieved NotEnoughFood from Cook. Choice = " + choice));
		System.out.println(getName() + ": " + "Not enough " + choice);
		customer.msgPleaseReorder(choice);
	}

	
	public void msgOrderReady(String choice, int t) {
		log.add(new LoggedEvent("Recieved OrderReady from Cook. Choice = " + choice + " Table = " + t));
		System.out.println(getName() + ": Recieved  order" + choice + " for table = " + t);
		
	}

	
	public void msgCheckPlease(Customer customer) {
		log.add(new LoggedEvent("Recieved CheckPlease from customer"));
		
	}
	
	public void msgHereIsCheck(double check, int t) {
		log.add(new LoggedEvent("Received HereIsCheck from cashier. Total = "+ check));
		System.out.println(getName() + ": Recieved $"+ check +" bill from cashier. Delivering to customer.");
		if (t==1){
			customer.msgHereIsYourCheck(check);
		}
		else if (t==2){
			customer2.msgHereIsYourCheck(check);
		}
			
		
	}

	public void msgDoneAndPaying(Customer c) {
		log.add(new LoggedEvent("Received DoneAndPaying from customer. Can clear table."));
		System.out.println(getName() + ": Customer is leaving. Clearing table.");
	}

	
	public boolean isNotOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isGoingOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void setBusy(boolean b) {
		// TODO Auto-generated method stub
		
	}

	
	public int numOfCust() {
		// TODO Auto-generated method stub
		return 0;
	}
}
