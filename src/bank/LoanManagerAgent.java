package bank;

import agent.Agent;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;
import bank.interfaces.Teller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class LoanManagerAgent extends Agent implements LoanManager {
	private String name;
	
	class LoanTask {
		LoanTask(BankCustomer bc, double loanAmount) {
			this.bc = bc;
			this.loanAmount = loanAmount;
		}
		BankCustomer bc;
		double loanAmount;
	}
	
	List<LoanTask> loanTasks = new ArrayList<LoanTask>();
	
	LoanManagerAgent(String name) {
		this.name = name;
		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgINeedALoan(BankCustomer bc, double loanAmount) {
		loanTasks.add(new LoanTask(bc, loanAmount));
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		if(!loanTasks.isEmpty()) {
			processLoan(loanTasks.get(0));
			return true;
		}
		
		return false;
	}
	// Actions
	
	private void processLoan(LoanTask lt) {
		lt.bc.msgLoanApproved(lt.loanAmount);
		loanTasks.remove(lt);
	}
	

	// Accessors, etc.

	

	
	public String toString() {
		return "customer " + getName();
	}



	



//	@Override
//	public void msgFollowMeToTable(Waiter w, int x, int y, Map<String, Double> m) {
//		// TODO Auto-generated method stub
//		
//	}
}

