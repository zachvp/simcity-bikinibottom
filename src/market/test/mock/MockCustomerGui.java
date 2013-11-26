package market.test.mock;

import java.awt.Graphics2D;

import market.interfaces.Customer;
import market.interfaces.CustomerGuiInterfaces;


public class MockCustomerGui extends Mock implements CustomerGuiInterfaces {

	public EventLog log = new EventLog();
	Customer customer; 
	
	public MockCustomerGui(Customer c) {
		super("Mock CustomerGui");
		customer = c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBuying() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBuying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToFrontDesk() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the message and go to FrontDesk now "));
		customer.msgAnimationFinishedGoToCashier();
	}

	@Override
	public void DoExitMarket() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the message and leave market now "));
		customer.msgAnimationFinishedLeaveMarket();
		
	}

	

}