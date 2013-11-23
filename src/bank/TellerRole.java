package bank;

import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import bank.gui.TellerGui;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;
import bank.interfaces.SecurityGuard;
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
public class TellerRole extends Role implements Teller {
	private String name;
	
	Semaphore active = new Semaphore(0, true);
	TellerGui tellerGui;
	int myDeskPosition;
	
	SecurityGuard securityGuard;
	
	int loanManagerXPos = 70;
	
	
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
	
	public TellerRole(PersonAgent person) {
		super(person);
	}
	
//	public TellerRole(PersonAgent person, AccountManager am, LoanManager lm, int deskPosition){
//		super(person);
//		myDeskPosition = deskPosition;
//		accountManager= am;
//		loanManager = lm;
////		this.name = name;
//	}
		

	/**
	 * hack to establish connection to Host agent.
	 */


	public String getCustomerName() {
		return name;
	}
	// Messages
	public void setSecurityGuard(SecurityGuard sc) {
		securityGuard = sc;
	}
	
	public void msgIWantToOpenAccount(BankCustomer bc, double initialDeposit) {
//		Do("opening account");
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
	
	public void msgAtDestination() {
		active.release();
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
		Do("opening account");
		doGoToAccountManager();
		acquireSemaphore(active);
		accountManager.msgOpenNewAccount(this, mc.getBankCustomer(), mc.getDeposit());
	    mc.state = CustomerState.waitingForAccountManager;
	}
	
	private void accountOpened(MyCustomer mc) {
		Do("account has been opened");
		doGoToDesk();//temp, TODO fix for multipl tellers
		acquireSemaphore(active);
		mc.getBankCustomer().msgAccountOpened(mc.accountId);
		getMyCustomers().remove(mc);
		securityGuard.msgTellerOpen(this);
	}
	
	private void deposit(MyCustomer mc) {
		Do("deposit");
		doGoToAccountManager();
		acquireSemaphore(active);
		accountManager.msgDepositMoney(this, mc.getBankCustomer(), mc.accountId, mc.getDeposit());
		mc.state= CustomerState.waitingForAccountManager;
	}
	
	private void depositCompleted(MyCustomer mc) {
		Do("deposit verified");
		doGoToDesk();
		acquireSemaphore(active);
		mc.getBankCustomer().msgDepositSuccessful(mc.getDeposit());
		getMyCustomers().remove(mc);
		securityGuard.msgTellerOpen(this);
	}
	
	private void withdraw(MyCustomer mc) {
		Do("withdraw");
		doGoToAccountManager();
		acquireSemaphore(active);
		accountManager.msgWithdrawMoney(this, mc.getBankCustomer(), mc.accountId, mc.withdrawal);
		mc.state = CustomerState.waitingForAccountManager;
	}
	
	private void withdrawCompleted(MyCustomer mc) {
		Do("withdraw verified");
		doGoToDesk();
		acquireSemaphore(active);
		mc.getBankCustomer().msgWithdrawSuccessful(mc.withdrawal);
		getMyCustomers().remove(mc);
		securityGuard.msgTellerOpen(this);
	}
	
	private void sendToLoanManager(MyCustomer mc) {
	   Do("sending to loan manager");
	   mc.getBankCustomer().msgSpeakToLoanManager(loanManager, loanManagerXPos);
	   doGoToLoanManager();
	   acquireSemaphore(active);
	   doGoToDesk();
	   acquireSemaphore(active);
	   getMyCustomers().remove(mc);
	   securityGuard.msgTellerOpen(this);
	}

	//ANIMATION
	private void doGoToLoanManager() {
		tellerGui.DoGoToLoanManager();//TODO
	}
	private void doGoToAccountManager() {
		tellerGui.DoGoToAccountManager();
	}
	public void doGoToDesk(){
		tellerGui.DoGoToDesk(myDeskPosition);
	}
	private void doEndWorkDay() {
		
	}
	
	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setGui(TellerGui g) {
		tellerGui = g;
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

	public void setDeskPosition(int deskPosition){
		myDeskPosition = deskPosition;
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

