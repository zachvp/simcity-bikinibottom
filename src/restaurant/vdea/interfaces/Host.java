package restaurant.vdea.interfaces;

import restaurant.vdea.HostRole.Table;

/**
 * Host interface
 * @author Victoria Dea
 *
 */
public interface Host {
	
	public void msgIWantFood(Customer cust);
	
	public void msgStayOrLeave(Customer cust, boolean stay);
	
	public void msgLeaveLine(Customer cust);
	
	public void msgTableIsFree(int t);
	
	public Waiter pickWaiter();
	
	public void assignWaiter(Customer customer, Table table, Waiter waiter);
	
	public void customerLeaves(Customer c);

	//public void addWaiter(Waiter newWaiter);
	
	public int getLineNum(Customer c);
	
	//public boolean allowBreak();	
	
	public void restaurantFull(Customer c);
	
	public void atDest();

	
	
	
	
	
	
	
}
