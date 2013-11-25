package bank.test.mock;

import bank.interfaces.TellerGuiInterface;

public class MockTellerGui implements TellerGuiInterface {
	EventLog log = new EventLog();

	@Override
	public void DoGoToAccountManager() {
		log.add("at account manager");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToLoanManager() {
		log.add("at loan manager");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToDesk(int xFactor) {
		log.add("at desk" + xFactor);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToWorkstation(int xFactor) {
		log.add("at workstation" + xFactor);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoEndWorkDay() {
		log.add("end work day");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoLeaveBank() {
		log.add("leave bank");
		// TODO Auto-generated method stub
		
	}
	
	
}