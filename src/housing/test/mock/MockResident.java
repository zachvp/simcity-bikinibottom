package housing.test.mock;

import housing.interfaces.Resident;
import agent.PersonAgent;
import agent.mock.EventLog;
import agent.mock.Mock;

public class MockResident extends Mock implements Resident {
	/* ----- Data ----- */
	public EventLog log = new EventLog();

	public MockResident(String name) {
		super(name);
	}

	/* ----- Messages ----- */
	public void msgPaymentDue(double amount) {
		
	}

}
