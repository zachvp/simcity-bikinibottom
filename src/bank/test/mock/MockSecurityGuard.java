package bank.test.mock;

import agent.WorkRole;
import bank.gui.SecurityGuardGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;


public class MockSecurityGuard extends Mock implements SecurityGuard {

	public EventLog log = new EventLog();
	
	public MockSecurityGuard(String name) {
		super(name);
	}

	@Override
	public void msgCustomerArrived(BankCustomer bc) {
		log.add("customer arrived" + bc);
	}

	@Override
	public void msgTellerOpen(Teller t) {
		log.add("teller opened" + t);
	}

	@Override
	public void msgLeavingBank(BankCustomer bc) {
		log.add("customer leaving" + bc);
	}

	@Override
	public void addRole(WorkRole r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTeller(Teller t, int deskX) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(SecurityGuardGui g) {
		// TODO Auto-generated method stub
		
	}
	
}
