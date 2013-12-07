package restaurant.lucas;

import restaurant.lucas.interfaces.Cashier;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Market;
import restaurant.lucas.interfaces.Waiter;
import restaurant.lucas.test.mock.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;


import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Cashier Role
 * 
 * @author Jack Lucas
 */
public class CashierRole extends WorkRole implements Cashier {

	public EventLog log = new EventLog();

	@Override
	public void msgComputeBill(Customer c, String choice, Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyPayment(Customer c, double cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRequestPayment(Market m, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	// private CashierGui cashierGui = null;
	
	
}

