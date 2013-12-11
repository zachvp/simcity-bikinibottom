package restaurant.strottma.test.mock;

import restaurant.strottma.HostRole.Table;
import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Cook.GrillOrPlate;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;

public class MockWaiter implements Waiter {

	public EventLog log = new EventLog();
	public Cashier cashier;
	
	public MockWaiter(String name) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsBill(Customer customer, double bill) {
		// TODO Auto-generated method stub
		log.add("Received msgHereIsBill from cashier. Customer: " + customer + ", bill: " + bill);
	}

	@Override
	public void msgOutOfChoice(Customer customer, Table table, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Customer customer, Table table, String choice, GrillOrPlate plateArea) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaving(Customer customert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

}
