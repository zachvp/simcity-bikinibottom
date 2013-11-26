package bank.test.mock;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankCustomerGuiInterface;

public class MockBankCustomerGui implements BankCustomerGuiInterface {
	EventLog log = new EventLog();
	BankCustomer bc;
	
	public MockBankCustomerGui(BankCustomer bc) {
		this.bc = bc;
	}
	
	@Override
	public void DoGoToTeller(int xLoc, double money, double account) {
		log.add("going to teller" + xLoc + money + account);
		bc.msgAtDestination();
		
	}

	@Override
	public void DoLeaveBank(double money, double account) {
		log.add("leave bank" + money + account);
		bc.msgAtDestination();
	}

	@Override
	public void DoGoToLoanManager(int x, double money, double account) {
		log.add("go to loan" + x + money + account);
		bc.msgAtDestination();
	}

	@Override
	public void DoGoToSecurityGuard(double money, double account) {
		log.add("go to sec" + money + account);
		bc.msgAtDestination();
	}

	@Override
	public void DoEndWorkDay() {
		log.add("end work day");
		bc.msgAtDestination();
	}
	
}