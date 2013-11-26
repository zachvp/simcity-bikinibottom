package bank.test.mock;

import bank.interfaces.SecurityGuardGuiInterface;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;
import bank.interfaces.TellerGuiInterface;

public class MockSecurityGuardGui implements SecurityGuardGuiInterface {
	EventLog log = new EventLog();
	SecurityGuard sg;
	
	public MockSecurityGuardGui(SecurityGuard g) {
		this.sg = g;
	}

	@Override
	public void DoGoToDesk() {
		// TODO Auto-generated method stub
		sg.msgAtDestination();
		
	}

	@Override
	public void DoEndWorkDay() {
		// TODO Auto-generated method stub
		sg.msgAtDestination();
	}

	@Override
	public void DoLeaveBank() {
		// TODO Auto-generated method stub
		sg.msgAtDestination();
	}

	@Override
	public void atDestination() {
		// TODO Auto-generated method stub
		sg.msgAtDestination();
	}


}