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
public class TellerAgent extends Agent implements Teller {
	private String name;
	
	AccountManager accountManager;
	public enum CustomerState {waiting, openingAccount, accountVerified, depositing, withdrawing, depositVerified, withdrawVerified, askedForLoan, waitingForAccountManager};
	public class MyCustomer {
		MyCustomer(BankCustomer bc, CustomerState s, double d, double w, int accountId) {
			setBankCustomer(bc);
			state = s;
			setDeposit(d);
			withdrawal = w;
			this.accountId = accountId;
			
		}
		
		public double getWithdrawal() {
			return withdrawal;
		}
		public BankCustomer getBankCustomer() {
			return bankCustomer;
		}
		public void setBankCustomer(BankCustomer bankCustomer) {
			this.bankCustomer = bankCustomer;
		}
		public double getDeposit() {
			return deposit;
		}
		public void setDeposit(double deposit) {
			this.deposit = deposit;
		}
		private BankCustomer bankCustomer;
		CustomerState state;
		private double deposit;
		double withdrawal;
		int accountId;
	}
	private List<MyCustomer> myCustomers= new ArrayList<MyCustomer>();
	LoanManager loanManager;
	
	public TellerAgent(String name){
		super();
		this.name = name;
		


	}
		

	/**
	 * hack to establish connection to Host agent.
	 */


	public String getCustomerName() {
		return name;
	}
	// Messages
	public void msgIWantToOpenAccount(BankCustomer bc, double initialDeposit) {
		getMyCustomers().add(new MyCustomer(bc, CustomerState.openingAccount, initialDeposit, 0, 0));
		stateChanged();
	}
	
	public void msgDepositMoney(BankCustomer bc, int accountId, double depositAmount) {
		getMyCustomers().add(new MyCustomer(bc, CustomerState.depositing, depositAmount, 0, accountId));
		stateChanged();
	}
	
	public void msgWithdrawMoney(BankCustomer bc, int accountId, double withdrawAmount) {
		getMyCustomers().add(new MyCustomer(bc, CustomerState.withdrawing, 0, withdrawAmount, accountId));
		stateChanged();
	}
	
	public void msgNewAccountVerified(BankCustomer bc, int accountId) {
		MyCustomer mc = findCustomer(bc);
		mc.accountId = accountId;
		mc.state= CustomerState.accountVerified;
		stateChanged();
	}
	
	public void msgDepositSuccessful(BankCustomer bc, int accountId, double depositAmount) {
		MyCustomer mc = findCustomer(bc);
		mc.state = CustomerState.depositVerified;
		stateChanged();
	}
	
	public void msgWithdrawSuccessful(BankCustomer bc, int accountId, double withdrawAmount) {
		MyCustomer mc = findCustomer(bc);
		mc.state = CustomerState.withdrawVerified;
		stateChanged();
	}
	
	public void msgINeedALoan(BankCustomer bc) {
		getMyCustomers().add(new MyCustomer(bc, CustomerState.askedForLoan, 0, 0, 0));
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
//		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.openingAccount) {
				openNewAccount(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.accountVerified) {
				accountOpened(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.depositing) {
				deposit(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.depositVerified) {
				depositCompleted(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.withdrawing) {
				withdraw(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.withdrawVerified){
				withdrawCompleted(mc);
				return true;
			}
		}
		
		for(MyCustomer mc : getMyCustomers()) {
			if(mc.state == CustomerState.askedForLoan) {
				sendToLoanManager(mc);
				return true;
			}
		}
		return false;
	}
	// Actions
	
	private void openNewAccount(MyCustomer mc) {
		accountManager.msgOpenNewAccount(this, mc.getBankCustomer(), mc.getDeposit());
	    mc.state = CustomerState.waitingForAccountManager;
	}
	
	private void accountOpened(MyCustomer mc) {
		mc.getBankCustomer().msgAccountOpened(mc.accountId);
		getMyCustomers().remove(mc);
	}
	
	private void deposit(MyCustomer mc) {
		accountManager.msgDepositMoney(this, mc.getBankCustomer(), mc.accountId, mc.getDeposit());
		mc.state= CustomerState.waitingForAccountManager;
	}
	
	private void depositCompleted(MyCustomer mc) {
		mc.getBankCustomer().msgDepositSuccessful(mc.getDeposit());
		getMyCustomers().remove(mc);
	}
	
	private void withdraw(MyCustomer mc) {
		accountManager.msgWithdrawMoney(this, mc.getBankCustomer(), mc.accountId, mc.withdrawal);
		mc.state = CustomerState.waitingForAccountManager;
	}
	
	private void withdrawCompleted(MyCustomer mc) {
		mc.getBankCustomer().msgWithdrawSuccessful(mc.withdrawal);
		getMyCustomers().remove(mc);
	}
	
	private void sendToLoanManager(MyCustomer mc) {
	    mc.getBankCustomer().msgSpeakToLoanManager(loanManager);
	    getMyCustomers().remove(mc);
	}

	// Accessors, etc.

	private MyCustomer findCustomer(BankCustomer b) {
		for(MyCustomer mc :getMyCustomers()) {
			if(mc.getBankCustomer() == b){
				return mc;
			}
		}
		return null;
	}

	public void setAccountManager(AccountManager am) {
		accountManager = am;
	}
	
	public void setLoanManager(LoanManager lm) {
		loanManager = lm;
	}
	
	public String toString() {
		return "customer " + getName();
	}


	public List<MyCustomer> getMyCustomers() {
		return myCustomers;
	}


	public void setMyCustomers(List<MyCustomer> myCustomers) {
		this.myCustomers = myCustomers;
	}
	
}

