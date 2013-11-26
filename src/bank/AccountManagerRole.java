package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.AccountManagerGui;
import bank.gui.BankBuilding;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;


/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class AccountManagerRole extends WorkRole implements AccountManager {
	private String name;
	static int currentIdNum = 1000; //starts at 1000 and increments TODO make this and accountmap static
	List<Teller> tellers = new ArrayList<Teller>();
	List<Task> tasks = new ArrayList<Task>();
	
	private boolean endWorkShift = false;
	
	private Semaphore active = new Semaphore(0, true);
	private AccountManagerGui accountManagerGui;
	
	private boolean atWork;
	
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
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
//		
//		System.out.println("HIYA");
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
		doGoToDesk();
		acquireSemaphore(active);
		atWork = true;
	}
	
	public void hackAddAccount(BankCustomer bc, double deposit, int testAccountId) {
        Account newAccount = new Account(bc, deposit);
        accountMap.put(testAccountId, newAccount);
       // t.t.msgNewAccountVerified(t.bc, currentIdNum);
       // tasks.remove(t);
        //currentIdNum++; 
	}
	
	private void verifyNewAccount(Task t) {
		Do("verifying new account");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
        Account newAccount = new Account(t.bc, t.deposit);
        accountMap.put(currentIdNum, newAccount);
        t.t.msgNewAccountVerified(t.bc, currentIdNum);
        tasks.remove(t);
        currentIdNum++;  
	}
	
	private void deposit(Task t) {
		Do("deposit");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		Account temp = accountMap.get(t.accountId);
		temp.amount += t.deposit;
		t.t.msgDepositSuccessful(t.bc, t.accountId, t.deposit);
		tasks.remove(t);
	}

	private void withdraw(Task t) {
		Do("withdraw");
		doGoToComputer();
		acquireSemaphore(active);
		doGoToDesk();
		acquireSemaphore(active);
		Account temp = accountMap.get(t.accountId);
		temp.amount -= t.withdrawal;
		t.t.msgWithdrawSuccessful(t.bc, t.accountId, t.withdrawal);
		tasks.remove(t);
	}
	
	private void goOffWork() {
		addPaycheckToWallet();
		doEndWorkDay();
		acquireSemaphore(active);
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
	
	private void doEndWorkDay() {
		accountManagerGui.DoEndWorkDay();
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
	
	@Override
	public int getShiftStartHour() {
		return startHour;
	}


	@Override
	public int getShiftStartMinute() {
		return startMinute;
	}


	@Override
	public int getShiftEndHour() {
		return endHour;
	}


	@Override
	public int getShiftEndMinute() {
		return endMinute;
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

