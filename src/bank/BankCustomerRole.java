package bank;

import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import bank.gui.BankCustomerGui;
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
public class BankCustomerRole extends Role implements BankCustomer {
	private String name;
	
	private Semaphore active = new Semaphore(0, true);
	
	BankCustomerGui bankCustomerGui;
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BankCustomerRole(PersonAgent person){
		super(person);
		this.name = name;
		state = State.waiting;

//		myCash.amount = initialMoney;
//		myCash.capacity = initialMoney + 20;
//		myCash.threshold = initialMoney -20;
		

	}
	private int accountId = -1;//if -1, has not been assigned
	
	class Cash {
		Cash() {
			
		}
		Cash(double amount, double threshold, double capacity, double cashInAccount) {
			this.amount = amount;
			this.threshold = threshold;
			this.capacity = capacity;
			this.cashInAccount = cashInAccount;
		}
		double amount;
		double threshold;
		double capacity;
		double cashInAccount;
	}
	
	Cash myCash = new Cash();// = new Cash(50, 30, 70, 0);
	public enum State {waiting, openingAccount, depositing, withdrawing, gettingLoan, atLoanManager, leaving, enteredBank};
	enum Event {gotToTeller, accountOpened, depositSuccessful, withdrawSuccessful, sentToLoanManager, loanCompleted, goingToTeller};
	
	State state;
	Event event;
	
	private double cashAdjustAmount = 0;
	double loanAmount = 0;
	private LoanManager loanManager;
	Teller teller;
	int tellerXLoc;
	

	/**
	 * hack to establish connection to Host agent.
	 */


	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation

	}
	
	public void msgGoToTeller(int xLoc) {
		event = Event.goingToTeller;
		tellerXLoc = xLoc;
		stateChanged();
	}
	
	public void msgGotToTeller() {
//		Do("msggottoteller");
		System.out.println("HI");
		event = Event.gotToTeller;
		stateChanged();
	}
	
	public void msgAccountOpened(int accountIdNumber) {
		event = Event.accountOpened;
		this.setAccountId(accountIdNumber);
		stateChanged();
	}
	
	public void msgDepositSuccessful(double depositAmount) {
		setCashAdjustAmount((depositAmount * -1));
		event = Event.depositSuccessful;
		stateChanged();
		
	}
	
	public void msgWithdrawSuccessful(double withdrawAmount) {
		setCashAdjustAmount(withdrawAmount);
		event = Event.withdrawSuccessful;
		stateChanged();
	}
	
	public void msgSpeakToLoanManager(LoanManager lm) {
		setLoanManager(lm);
		event = Event.sentToLoanManager;
	}
	
	public void msgLoanApproved(double amount) {
		loanAmount = amount;
		event = Event.loanCompleted;
		stateChanged();
	}
	
	public void msgAtDestination() {
		System.out.println("Made it");
		active.release();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		System.out.println("entered scheduler");
		if(state == State.enteredBank && event ==Event.goingToTeller) {
			goToTeller(tellerXLoc);
			return true;
		}
		if(state == State.waiting && event == Event.gotToTeller) {
//			Do("sup");
			speakToTeller();
			
			return true;
		}
		if(state == State.openingAccount && event == Event.accountOpened) {
			leaveBank();
			return true;
		}
		if(state == State.depositing && event == Event.depositSuccessful) {
			leaveBank();
			return true;
		}
		if(state == State.withdrawing && event == Event.withdrawSuccessful) {
			leaveBank();
			return true;
		}
		if(state==State.gettingLoan && event == Event.sentToLoanManager){
			askForLoan();
			return true;
		}
		if(state==State.atLoanManager && event == Event.loanCompleted) {
			leaveBank();
			return true;
		}
		

		
		return false;
	}

	// Actions
	private void goToTeller(int xLoc) {
		doGoToTeller(xLoc);
		acquireSemaphore(active);
		System.out.println("YO");
		state = State.waiting;
		msgGotToTeller();//TODO HACK
	}
	
	private void speakToTeller() {
		System.out.println("speaking to teller");
		if(accountId == -1) {//have not been assigned accountID yet
			teller.msgIWantToOpenAccount(this, myCash.amount * .2);
			state = State.openingAccount;
			setCashAdjustAmount(-10);//TODO for testing
//			Do("made it here");
			return;
		}
		if(myCash.amount < myCash.threshold && myCash.cashInAccount > myCash.threshold){
			System.out.println("I'm withdrawing");
			teller.msgWithdrawMoney(this, accountId, myCash.threshold);//TODO for testing
			state= State.withdrawing;
			return;
		}
		if(myCash.amount>myCash.capacity) {
			System.out.println("Im depositing");
			teller.msgDepositMoney(this, accountId, myCash.threshold); //TODO for testing
			state = State.depositing;
			return;
		}
		if(myCash.amount < myCash.threshold && myCash.cashInAccount<myCash.threshold) {
			System.out.println("Im getting Loan");
			teller.msgINeedALoan(this);
			state= State.gettingLoan;
			return;
		}
	}
	
	private void leaveBank() {
		myCash.amount += getCashAdjustAmount();
		myCash.amount += loanAmount;
		myCash.cashInAccount -= getCashAdjustAmount();
		setCashAdjustAmount(0);
		loanAmount = 0;
		state = State.leaving;
	}
	
	private void askForLoan() {
		System.out.println("asking for loan from loanmanager");
		getLoanManager().msgINeedALoan(this, myCash.threshold);
		state = State.atLoanManager;
	}

	//animation
	private void doGoToTeller(int xLoc) {
		bankCustomerGui.DoGoToTeller(xLoc);
	}
	
	
	// Accessors, etc.

	public void setGui(BankCustomerGui b) {
		bankCustomerGui = b;
	}
	
	public void setTeller(Teller t) {//TODO this is a hack
		teller = t;
	}
	public double getWalletAmount() {
		return myCash.amount;
	}
	public void setWalletAmount(double amount) {
		myCash.amount = amount;
	}
	public double getWalletCapacity() {
		return myCash.capacity;
	}
	public double getWalletThreshold() {
		return myCash.threshold;
	}
	public void setMoney(int money) {
		myCash.amount = money;
		myCash.capacity = money+20;
		myCash.threshold = money-20;
	}

	public void setAccountAmount(double money) {
		myCash.cashInAccount = money;
	}
	
	public double getAccountAmount() {
		return myCash.cashInAccount;
	}
	
	public State getState() {
		return state;
	}

	public String toString() {
		return "customer " + getName();
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public LoanManager getLoanManager() {
		return loanManager;
	}

	public void setLoanManager(LoanManager loanManager) {
		this.loanManager = loanManager;
	}

	public double getCashAdjustAmount() {
		return cashAdjustAmount;
	}

	public void setCashAdjustAmount(double cashAdjustAmount) {
		this.cashAdjustAmount = cashAdjustAmount;
	}
	
	public void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




//	@Override
//	public void msgFollowMeToTable(Waiter w, int x, int y, Map<String, Double> m) {
//		// TODO Auto-generated method stub
//		
//	}
}

