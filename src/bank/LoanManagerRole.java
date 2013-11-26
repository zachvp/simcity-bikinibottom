package bank;

import gui.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.LoanManagerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class LoanManagerRole extends WorkRole implements LoanManager {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	LoanManagerGui loanManagerGui;
	
	private boolean endWorkShift = false;
	
	class LoanTask {
		LoanTask(BankCustomer bc, double loanAmount) {
			this.bc = bc;
			this.loanAmount = loanAmount;
		}
		BankCustomer bc;
		double loanAmount;
	}
	
	List<LoanTask> loanTasks = new ArrayList<LoanTask>();
	
	public LoanManagerRole(Person person, Building bank) {
		super(person, bank);
//		this.name = name;
		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgINeedALoan(BankCustomer bc, double loanAmount) {
		loanTasks.add(new LoanTask(bc, loanAmount));
		stateChanged();
	}
	public void msgLeaveWork() {
		endWorkShift = true;
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
		
		if(endWorkShift) {
			goOffWork();
			return true;
		}
		
		return false;
	}
	// Actions
	
	private void processLoan(LoanTask lt) {
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		lt.bc.msgLoanApproved(lt.loanAmount);
		loanTasks.remove(lt);
	}
	private void goOffWork() {
		doEndWorkDay();
		acquireSemaphore(active);
		
		this.deactivate();
		
	}
	
	//ANIMATION #####################
	public void msgAtDestination() {
		active.release();
	}
	private void doGoToDesk() {
		loanManagerGui.DoGoToDesk();
	}
	private void doGoToComputer() {
		loanManagerGui.DoGoToComputer();
	}
	private void doLeaveBank(){ //call at end of shift
		loanManagerGui.DoLeaveBank();
	}
	
	private void doEndWorkDay() {
		loanManagerGui.DoEndWorkDay();
	}
	
	public void setGui(LoanManagerGui lmg) {
		loanManagerGui = lmg;
	}
	

	// Accessors, etc.

	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
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



	




}

