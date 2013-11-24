package housing.test.mock;

import housing.interfaces.Resident;
import agent.PersonAgent;
import agent.mock.EventLog;
import agent.mock.Mock;

public class MockResident extends Mock implements Resident {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	public PersonAgent person = new PersonAgent("Mock Resident");
	public double moneyOwed = 0;
	
	public MockResident(String name) {
		super(name);
	}

	/* ----- Messages ----- */
	public void msgPaymentDue(double amount) {
		this.moneyOwed = amount;
		log.add("Received message 'payment due' amount is " + amount);
	}

	@Override
	public void msgAtDestination() {
		log.add("Resident done waiting for input.");
	}

}
