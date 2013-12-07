package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.BankBuilding;
import bank.gui.LoanManagerGui;
import bank.gui.RobberGui;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;
import bank.interfaces.Robber;
import bank.interfaces.SecurityGuard;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class RobberRole extends Role implements Robber {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	
	SecurityGuard sg;
	AccountManager am;
	
	RobberGui robberGui;
	
	enum State {enteredBank, stopped, robbing, leaving, done, waitingForAcoountManager};
	
	State state;
	
	public RobberRole(Person person, CityLocation bank) {
		super(person, bank);

		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgGoToSecurityGuard(SecurityGuard s) {
		sg = s;
		state = State.enteredBank;
		stateChanged();
	}
	
	public void msgAttemptToStop(AccountManager am, boolean isSuccessful){
		if(isSuccessful){
			
			stateChanged();
			return;
		}
		this.am = am;
		state = State.robbing;
		stateChanged();
	}
	
	public void msgGiveMoneyToRobber(double amount) {
		this.getPerson().getWallet().addCash(amount);
		state = State.leaving;
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(state == State.enteredBank) {
			approachGuard();
			return true;
		}
		if(state == State.robbing){
			robBank();
			return true;
		}
		if(state == State.stopped) {
			leaveBank();
			return true;
		}
		if(state == State.leaving) {
			leaveBank();
			return true;
		}
		return false;
	}
	
	// Actions
	private void approachGuard() {
		robberGui.DoGoToSecurityGuard();
		acquireSemaphore(active);
		
		sg.msgIAmRobbingTheBank(this);
	}
	private void robBank(){
		robberGui.DoGoToAccountManager();
		acquireSemaphore(active);
		
		state = State.waitingForAcoountManager;
		//gotosg, push, gotoam
		am.msgGiveMeTheMoney(this, 300.0);//TODO determine way to calculate real amount
	}
	private void leaveBank(){
		sg.msgRobberLeavingBank(this);
		robberGui.DoLeaveBank();
		acquireSemaphore(active);
		
		state = State.done;
		this.deactivate();
	}
	
	/*
	 * Called at end of work day
	 * adds to persons wallet
	 */
	private void addPaycheckToWallet() {
//		this.getPerson().getWallet().addCash(paycheckAmount);
	}
	
	//ANIMATION #####################
	public void msgAtDestination() {
		active.release();
	}
//	private void doGoToDesk() {
//		loanManagerGui.DoGoToDesk();
//	}
//	private void doGoToComputer() {
//		loanManagerGui.DoGoToComputer();
//	}
//	private void doLeaveBank(){ //call at end of shift
//		loanManagerGui.DoLeaveBank();
//	}
//	
//	private void doEndWorkDay() {
//		loanManagerGui.DoEndWorkDay();
//	}
//	
	public void setGui(RobberGui rg) {
		robberGui = rg;
	}
	
	
	

	// Accessors, etc.

	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	



	




}

