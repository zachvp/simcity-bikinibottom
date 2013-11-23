package bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;
import agent.WorkRole;
import bank.gui.AccountManagerGui;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;


/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class AccountManagerRole extends WorkRole implements AccountManager {
	private String name;
	int currentIdNum = 1000; //starts at 1000 and increments
	List<Teller> tellers = new ArrayList<Teller>();
	List<Task> tasks = new ArrayList<Task>();
	
	private boolean endWorkShift = false;
	
	private Semaphore active = new Semaphore(0, true);
	private AccountManagerGui accountManagerGui;
	
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
	Map<Integer, Account> accountMap = new HashMap<Integer, Account>();
	
//	////////
	
	public AccountManagerRole(PersonAgent person){
		super(person);
//		this.name = name;
		
	}
		

	/**
	 * hack to establish connection to Host agent.
	 */


	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgLeaveWork() {
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
			doEndWorkDay();
			acquireSemaphore(active);
			this.deactivate();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setGui(AccountManagerGui a) {
		accountManagerGui = a;
	}
	
	// Accessors, etc.

	
//	private MyCustomer findCustomer(BankCustomer b) {
//		for(MyCustomer mc :myCustomers) {
//			if(mc.bankCustomer == b){
//				return mc;
//			}
//		}
//		return null;
//	}

	
	public String toString() {
		return "customer " + getName();
	}


	@Override
	public int getShiftStartHour() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getShiftStartMinute() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getShiftEndHour() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getShiftEndMinute() {
		// TODO Auto-generated method stub
		return 0;
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

