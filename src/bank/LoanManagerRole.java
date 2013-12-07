package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.BankBuilding;
import bank.gui.LoanManagerGui;
import bank.interfaces.AccountManager;
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
	AccountManager accountManager;
	
	double paycheckAmount = 300;
	
	
	boolean atWork;
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
	
	int startHour;
	int startMinute;
	int endHour;
	int endMinute;
	
	public LoanManagerRole(Person person, CityLocation bank) {
		super(person, bank);
		atWork = false;
		

		startHour = ((BankBuilding) bank).getOpeningHour();
		startMinute = ((BankBuilding) bank).getOpeningMinute();
		endHour = ((BankBuilding) bank).getClosingHour();
		endMinute =((BankBuilding) bank).getClosingMinute();

		
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
		if(!atWork) {
			goToWork();
			return true;
		}
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
	private void goToWork() {
		atWork = true;
		doGoToDesk();
//		acquireSemaphore(active);
		
	}
	
	private void processLoan(LoanTask lt) {
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		lt.bc.msgLoanApproved(lt.loanAmount);
		accountManager.msgUpdateMoney(lt.loanAmount);
		loanTasks.remove(lt);

	}
	private void goOffWork() {
		addPaycheckToWallet();
		doEndWorkDay();
		acquireSemaphore(active);
		atWork = false;
		this.deactivate();
		
	}
	
	/*
	 * Called at end of work day
	 * adds to persons wallet
	 */
	private void addPaycheckToWallet() {
		this.getPerson().getWallet().addCash(paycheckAmount);
	}
	
	public void setAccountManager(AccountManager a){
		accountManager = a;
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
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}



	




}

