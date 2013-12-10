package restaurant.anthony.test.mock;


import java.util.List;

import restaurant.anthony.CookRole;
import restaurant.anthony.CustomerRole;
import restaurant.anthony.HostRole;
import restaurant.anthony.CashierRole.Check;
import restaurant.anthony.WaiterRoleBase.Menu;
import restaurant.anthony.WaiterRoleBase.Order;
import restaurant.anthony.gui.CustomerGui;
import restaurant.anthony.gui.WaiterGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;


public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public Customer customer;

	public MockWaiter(String name) {
		super(name);

	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMyCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void AskForPermission() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SitAtTable(CustomerRole cust, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HeresMyChoice(Customer cust, String CH) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OrderIsReady(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoMoreFood(String choice, Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AskForCheck(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HereIsCheck(Check ch) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Waiter : Received message HereIsCheck"));
		customer.HeresYourCheck(ch);
	}

	@Override
	public void msgAtTable(int tnumb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIdle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAtWaitingLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoBreak(boolean permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean IsOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(HostRole ho) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(CookRole co) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWaiterNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtExit() {
		// TODO Auto-generated method stub
		
	}




}
