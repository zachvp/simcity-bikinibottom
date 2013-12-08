package housing.test.mock;

import mock.EventLog;
import mock.Mock;
import housing.interfaces.DwellingLayoutGui;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import agent.PersonAgent;

public class MockResident extends Mock implements Resident {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	public PersonAgent person = new PersonAgent("Mock Resident");
	public double oweMoney = 0;
	
	// mock
	DwellingLayoutGui layoutGui = new MockDwellingGui();
	
	public MockResident(String name) {
		super(name);
	}

	/* ----- Messages ----- */
	public void msgPaymentDue(double amount, PayRecipient payRecipient) {
		this.oweMoney = amount;
		log.add("Received message 'payment due' amount is " + amount);
	}

	@Override
	public void msgAtDestination() {
		log.add("Resident done waiting for input.");
	}

	@Override
	public void msgDwellingFixed() {
		log.add("Dwelling is fixed.");
	}

	@Override
	public void msgDwellingDegraded() {
		log.add("Dwelling is degraded.");
	}
}
