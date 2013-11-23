package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;
import agent.Role;
import bank.gui.LoanManagerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class LoanManagerRole extends Role implements LoanManager {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	LoanManagerGui loanManagerGui;
	
	class LoanTask {
		LoanTask(BankCustomer bc, double loanAmount) {
			this.bc = bc;
			this.loanAmount = loanAmount;
		}
		BankCustomer bc;
		double loanAmount;
	}
	
	List<LoanTask> loanTasks = new ArrayList<LoanTask>();
	
	public LoanManagerRole(PersonAgent person) {
		super(person);
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
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		lt.bc.msgLoanApproved(lt.loanAmount);
		loanTasks.remove(lt);
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
	

	
	public String toString() {
		return "customer " + getName();
	}



	




}

