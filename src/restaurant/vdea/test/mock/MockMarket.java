package restaurant.vdea.test.mock;

import java.util.List;

import restaurant.vdea.interfaces.*;
import restaurant.vdea.Food;


/**
 * A MockMarket class for testing the CashierAgent
 * 
 * @author Victoria Dea
 *
 */

public class MockMarket extends Mock implements Market{
	
	public EventLog log = new EventLog();
	
	public MockMarket(String name) {
		super(name);
	}


	public void msgBillPayment(double check, Cashier c) {
		log.add(new LoggedEvent("Received payment from cashier"));
		System.out.println(getName() + ":Payment received from cashier");
	}


	@Override
	public void msgNotEnough(double check, Cashier cash) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgOrderRequest(Cook c, List<Food> food, Cashier cash) {
		log.add(new LoggedEvent("Received order request from cook"));
		System.out.println(getName() + ":New order received from cook");
		double bill = 100;
		cash.msgMarketBill(this, bill);
		
	}
	
	

}
