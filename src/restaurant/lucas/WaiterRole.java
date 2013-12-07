package restaurant.lucas;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Waiter Role
 * 
 * @author Jack Lucas
 */
public class WaiterRole extends WorkRole implements Waiter {

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Customer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSetBreakBit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(int tableNum, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(Customer c, String choic, int table, Dimension p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToPay(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Customer c, double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
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
	
}