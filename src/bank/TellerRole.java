package bank;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.WorkRole;
import agent.interfaces.Person;
import bank.gui.TellerGui;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class TellerRole extends WorkRole implements Teller {
	private String name;
	
	//need to instantiate the Task Scheduler now. Edited by Zach
	ScheduleTask task = new ScheduleTask();
	
	Semaphore active = new Semaphore(0, true);
	TellerGui tellerGui;
	int myDeskPosition;
	
	int startHour = 8;
	int startMinute = 0;
	int endHour = 6;
	int endMinute = 0;
	
	

	
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
	boolean endWorkShift = false;
	
	public TellerRole(Person person) {
		super(person);
		
		// ask everyone for rent
		Runnable command = new Runnable(){
			@Override
			public void run() {
				//do stuff
				
				msgLeaveWork();
				}
			
		};
		
		// every day at noon
		int hour = 17;
		int minute = 0;
		
		task.scheduleDailyTask(command, hour, minute);
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
	
	public void msgLeaveWork() {
		endWorkShift = true;
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
		
		if(endWorkShift) {
			goOffWork();
			return true;
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
		doGoToDesk();
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
	private void goOffWork() {
		doEndWorkDay();
		acquireSemaphore(active);
		
		this.deactivate();
		
	}

	//ANIMATION
	private void doGoToLoanManager() {
		tellerGui.DoGoToLoanManager();//TODO
	}
	private void doGoToAccountManager() {
		tellerGui.DoGoToAccountManager();
	}
	private void doGoToDesk(){
		tellerGui.DoGoToDesk(myDeskPosition);
	}
	public void doGoToWorkstation() {
		tellerGui.DoGoToWorkstation(myDeskPosition);
	}
	private void doEndWorkDay() {
		tellerGui.DoEndWorkDay();
	}
	
	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
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

