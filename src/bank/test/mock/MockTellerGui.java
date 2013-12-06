package bank.test.mock;

import bank.interfaces.Teller;
import bank.interfaces.TellerGuiInterface;

public class MockTellerGui implements TellerGuiInterface {
	EventLog log = new EventLog();
	Teller teller;
	
	public MockTellerGui(Teller t) {
		this.teller = t;
	}

	@Override
	public void DoGoToAccountManager() {
		log.add("at account manager");
		teller.msgAtDestination();
		
	}

	@Override
	public void DoGoToLoanManager() {
		log.add("at loan manager");
		teller.msgAtDestination();
		
	}

	@Override
	public void DoGoToDesk(int xFactor) {
		log.add("at desk" + xFactor);
		teller.msgAtDestination();
		
	}

	@Override
	public void DoGoToWorkstation(int xFactor) {
		log.add("at workstation" + xFactor);
		teller.msgAtDestination();
		
	}

	@Override
	public void DoEndWorkDay() {
		log.add("end work day");
		teller.msgAtDestination();
		
	}

	@Override
	public void DoLeaveBank() {
		log.add("leave bank");
		teller.msgAtDestination();
	}
}