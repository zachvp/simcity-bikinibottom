package restaurant.vdea.test.mock;

import restaurant.vdea.HostRole.Table;
import restaurant.vdea.interfaces.*;


public class MockHost extends Mock implements Host{
	
	public EventLog log = new EventLog();

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgStayOrLeave(Customer cust, boolean stay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveLine(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Waiter pickWaiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assignWaiter(Customer customer, Table table, Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customerLeaves(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLineNum(Customer c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void restaurantFull(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atDest() {
		// TODO Auto-generated method stub
		
	}
	

}
