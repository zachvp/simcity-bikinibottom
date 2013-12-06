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
	
	enum State {enteredBank, stopped, robbing, leaving, done};
	
	State state;
	
	public RobberRole(Person person) {
		super(person);

		


		
//		this.name = name;
		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
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
		//dogotoguard
		sg.msgIAmRobbingTheBank(this);
	}
	private void robBank(){
		//gotosg, push, gotoam
		am.msgGiveMeTheMoney(this, 300.0);//TODO determine way to calculate real amount
	}
	private void leaveBank(){
		//gotobankexit
		sg.msgRobberLeavingBank(this);
		state = State.done;
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
//	public void setGui(LoanManagerGui lmg) {
//		loanManagerGui = lmg;
//	}
	
	
	

	// Accessors, etc.

	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	



	




}

