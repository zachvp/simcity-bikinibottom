package bank;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.AccountManagerGui;
import bank.gui.BankBuilding;
import bank.gui.InfoPanel;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.Teller;


/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class AccountManagerRole extends WorkRole implements AccountManager {
	private String name;
	static int currentIdNum = 1000; //starts at 1000 and increments 
	List<Teller> tellers = new ArrayList<Teller>();
	List<Task> tasks = new ArrayList<Task>();
	
	private boolean endWorkShift = false;
	
	InfoPanel infoPanel;
	
	private Semaphore active = new Semaphore(0, true);
	private AccountManagerGui accountManagerGui;
	
	private boolean atWork;
	
	double moneyInBank = 100000;
	double paycheckAmount = 300;
	
	enum TaskState {newAccount, pendingDeposit, pendingWithdraw};
	class Task {
		Task(Teller t, BankCustomer bc, TaskState state, int accountId, double deposit, double withdrawal){
			this.t = t;
			this.bc = bc;
			this.state = state;
			this.accountId = accountId;
			this.deposit = deposit;
			this.withdrawal = withdrawal;
		}
		Teller t;
		BankCustomer bc;
		int accountId;
		double deposit;
		double withdrawal;
		TaskState state;

	}
	class Account {
		Account(BankCustomer bc, double amount) {
			this.bc = bc;
			this.amount = amount;
		}
		BankCustomer bc;
		double amount;
	}
	static Map<Integer, Account> accountMap = Collections.synchronizedMap(new HashMap<Integer, Account>());//make STATIC
	
	enum robberState {asking, done};
	class MyRobber {
		MyRobber(Robber r, double stealAmount, robberState s){
			this.r = r;
			this.stealAmount = stealAmount;
			this.state = s;
		}
		Robber r;
		double stealAmount;
		robberState state;
	}
	List<MyRobber> robbers = new ArrayList<MyRobber>();
	
	int startHour;
	int startMinute;
	int endHour ;
	int endMinute ;
//	////////
	
	public AccountManagerRole(Person person, CityLocation bank) {
		super(person, bank);
		atWork = false;
		
		
		startHour = ((BankBuilding) bank).getOpeningHour();
		startMinute = ((BankBuilding) bank).getOpeningMinute();
		endHour = ((BankBuilding) bank).getClosingHour();
		endMinute =((BankBuilding) bank).getClosingMinute();

	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgLeaveWork() {//Called by securityguard to send all employees out once all customers have been served
		endWorkShift = true;
		stateChanged();
	}
	
	public void msgOpenNewAccount(Teller t, BankCustomer bc, double initialDeposit) {
		tasks.add(new Task(t, bc, TaskState.newAccount, 0, initialDeposit, 0));
		stateChanged();
	}

	public void msgDepositMoney(Teller t, BankCustomer bc, int accountId, double amount) {
		tasks.add(new Task(t, bc, TaskState.pendingDeposit, accountId, amount, 0));
		stateChanged();
	}

	public void msgWithdrawMoney(Teller t, BankCustomer bc, int accountId, double amount) {
		tasks.add(new Task(t, bc, TaskState.pendingWithdraw, accountId, 0, amount));
		stateChanged();
	}
	
	public void msgGiveMeTheMoney(Robber r, double amount) {
		robbers .add(new MyRobber(r, amount, robberState.asking));
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
//		
		for(MyRobber m : robbers) {
			if(m.state == robberState.asking){
				giveMoneyToRobber(m);
				return true;
			}
		}
		
		if(!atWork) {
			goToWork();
			return true;
		}
		
		for(Task t: tasks) {
			if(t.state == TaskState.newAccount){
				verifyNewAccount(t);
				return true;
			}
		}
		
		for(Task t: tasks) {
			if(t.state == TaskState.pendingDeposit){
				deposit(t);
				return true;
			}
		}
		
		for(Task t: tasks) {
			if(t.state == TaskState.pendingWithdraw){
				withdraw(t);
				return true;
			}
		}
		
		if(endWorkShift) {
			goOffWork();
			return true;
		}
		
		return false;
	}
	// Actions
	public void goToWork() {
		doGoToWork();
//		acquireSemaphore(active);
		atWork = true;
	}
	
	public void hackAddAccount(BankCustomer bc, double deposit, int testAccountId) {
        Account newAccount = new Account(bc, deposit);
        accountMap.put(testAccountId, newAccount);
       // t.t.msgNewAccountVerified(t.bc, currentIdNum);
       // tasks.remove(t);
        //currentIdNum++; 
	}
	
	private void giveMoneyToRobber(MyRobber mr) {
		Do(AlertTag.BANK, "getting robbed");
		mr.state = robberState.done;
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		mr.r.msgGiveMoneyToRobber(mr.stealAmount);
		moneyInBank -= mr.stealAmount;
		
		infoPanel.updateInfoPanel();
		
	}
	
	private void verifyNewAccount(Task t) {
		Do(AlertTag.BANK, "verifying new account");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
        Account newAccount = new Account(t.bc, t.deposit);
        moneyInBank += t.deposit;
        accountMap.put(currentIdNum, newAccount);
        t.t.msgNewAccountVerified(t.bc, currentIdNum);
        tasks.remove(t);
        currentIdNum++;  
        
        infoPanel.updateInfoPanel();
	}
	
	private void deposit(Task t) {
		Do(AlertTag.BANK, "deposit");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		Account temp = accountMap.get(t.accountId);
		temp.amount += t.deposit;
		moneyInBank += t.deposit;
		t.t.msgDepositSuccessful(t.bc, t.accountId, t.deposit);
		tasks.remove(t);
		
		infoPanel.updateInfoPanel();
	}

	private void withdraw(Task t) {
		Do(AlertTag.BANK, "withdraw");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		Account temp = accountMap.get(t.accountId);
		temp.amount -= t.withdrawal;
		moneyInBank -= t.withdrawal;
		t.t.msgWithdrawSuccessful(t.bc, t.accountId, t.withdrawal);
		tasks.remove(t);
		
		infoPanel.updateInfoPanel();
	}
	
	private void goOffWork() {
		addPaycheckToWallet();
		doEndWorkDay();
//		acquireSemaphore(active);
		this.deactivate();
		atWork = false;
	}
	
	/*
	 * Called at end of work day
	 * adds to persons wallet
	 */
	private void addPaycheckToWallet() {
		this.getPerson().getWallet().addCash(paycheckAmount);
	}
	
	//ANIMATION
	private void doGoToWork() {
		accountManagerGui.DoGoToFrontDesk();
		acquireSemaphore(active);
		accountManagerGui.DoGoToRightOfFrontDesk();
		acquireSemaphore(active);
		accountManagerGui.DoGoToBackDesk();
		acquireSemaphore(active);
		accountManagerGui.DoGoToDesk();
		acquireSemaphore(active);
	}
	
	private void doEndWorkDay() {//TODO check this
		accountManagerGui.DoGoToBackDesk();
		acquireSemaphore(active);
		accountManagerGui.DoGoToRightOfFrontDesk();
		acquireSemaphore(active);
		accountManagerGui.DoGoToFrontDesk();
		acquireSemaphore(active);
		accountManagerGui.DoLeaveBank();
		acquireSemaphore(active);
		
	}
	
	public void msgAtDestination() {
		active.release();
	}
	
	private void doGoToDesk() {
		accountManagerGui.DoGoToDesk();
	}
	private void doGoToComputer(){
		accountManagerGui.DoGoToComputer();
	}
	private void doLeaveBank() {
		accountManagerGui.DoLeaveBank();
	}
	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setGui(AccountManagerGui a) {
		accountManagerGui = a;
	}
	
	// Accessors, etc.
	
//	public String getName() {
//		return this.getPerson().getName();
//	}

	
//	private MyCustomer findCustomer(BankCustomer b) {
//		for(MyCustomer mc :myCustomers) {
//			if(mc.bankCustomer == b){
//				return mc;
//			}
//		}
//		return null;
//	}

	public double getMoneyInBank() {
		return moneyInBank;
	}
	
	public void setInfoPanel(InfoPanel i) {
		infoPanel = i;
	}
	
	@Override
	public void Do(String str) {
		Do(AlertTag.BANK, str);
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

