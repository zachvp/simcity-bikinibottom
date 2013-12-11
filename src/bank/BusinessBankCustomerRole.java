package bank;

import gui.trace.AlertTag;
import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;
import bank.gui.BankBuilding;
import bank.interfaces.BankCustomer;

public class BusinessBankCustomerRole extends BankCustomerRole implements BankCustomer {

	double money;
	int accountId = -1;
	
	public BusinessBankCustomerRole(Person person, CityLocation bank) {
		super(person, bank);
		Do(AlertTag.BANK, "BUSINESSBANKCUSTOMERLIVES");
	}
	
	public void msgUpdateRestaurantMoney(double money) {//sent from CashierRole
		this.money += money;
	}
	
	public void msgAccountOpened(int accountIdNumber) {
		event = Event.accountOpened;
		this.accountId = accountIdNumber;
		stateChanged();
	}
	
	@Override
	protected void speakToTeller() {
		Do(AlertTag.BANK, "BUSINESS speaking to teller");//TODO check that moneyNeeded is money needed ON TOP OF money I have, not just amount of expensive item

		
		if(accountId == -1) {//have not been assigned accountID yet
			Do(AlertTag.BANK, "need to open BUSINESS account");
//			
//			if(money < initialDepositAmount) {//is too poor/worthless to afford initialdeposit
//				teller.msgINeedALoan(this);
//				state= State.gettingLoan;
//				return;
//			}
			teller.msgIWantToOpenAccount(this, money);//TODO does this work or constant?
			state = State.openingAccount;
//			setCashAdjustAmount(-initialDepositAmount);//TODO for testing
//			Do("made it here");
			return;
		}
//		if(money > 0 ) { //if i need money and have enough in my account, i will withdraw it
//			//double moneyNeeded = this.getPerson().getWallet().getMoneyNeeded();
//			Do(AlertTag.BANK, "I'm withdrawing needed money");
//			teller.msgWithdrawMoney(this, accountId, moneyNeeded);//TODO for testing
//			state= State.withdrawing;
//			return;
//		}
//		if(this.getPerson().getWallet().getMoneyNeeded() > 0 && cashInAccount < this.getPerson().getWallet().getMoneyNeeded()) {//if i need money and dont have enough in account, i will take a loan for ALL OF IT (NOTE: possibly make it possible to withdraw some and get rest in loan?
//			teller.msgINeedALoan(this);
//			state= State.gettingLoan;
//			return;
//		}
//		if(this.getPerson().getWallet().getCashOnHand() < myTooLittle() && cashInAccount > ((myTooLittle() + myTooMuch())/2)){
//			Do(AlertTag.BANK, "I'm withdrawing");
//			double withdrawAmount = (myTooLittle() + myTooMuch())/2;
//			teller.msgWithdrawMoney(this, accountId, withdrawAmount);
//			state= State.withdrawing;
//			return;
//		}
		if(money > 0) {
			Do(AlertTag.BANK, "Im depositing");
			double depositAmount = money;
			teller.msgDepositMoney(this, accountId, depositAmount); 
			state = State.depositing;
			return;
		}
//		if(this.getPerson().getWallet().getCashOnHand() < myTooLittle() && cashInAccount< ((myTooLittle() + myTooMuch())/2)) {
////			Do("Im getting Loan");
//			teller.msgINeedALoan(this);
//			state= State.gettingLoan;
//			return;
//		}
		Do(AlertTag.BANK, "ERROR, no condtion met");//not good
	}
	
	@Override
	protected void leaveBank() {
		Do(AlertTag.BANK, "leaving bank " + accountId);
//		this.getPerson().getWallet().setCashOnHand(this.getPerson().getWallet().getCashOnHand() + getCashAdjustAmount());
		this.getPerson().getWallet().addCash(getCashAdjustAmount());
		this.getPerson().getWallet().addCash(loanAmount);
//		this.getPerson().getWallet().setCashOnHand(this.getPerson().getWallet().getCashOnHand() + loanAmount);
//		myCash.amount += loanAmount;
		//cashInAccount -= getCashAdjustAmount();
		//setCashAdjustAmount(0);
		//loanAmount = 0;
		money = 0;
		state = State.leaving;
		securityGuard.msgLeavingBank(this);
		doLeaveBank();
		acquireSemaphore(active);
		
		state = State.enteredBank;
		this.deactivate();
	}
	
	
	@Override
	public void activate() {
		super.activate();
		this.getPerson().setShouldDepositRestaurantMoney(false);
		this.money = this.getPerson().getCashierMoney();
		msgGoToSecurityGuard( ((BankBuilding) getLocation()).getSecurity() );
	}
	
}