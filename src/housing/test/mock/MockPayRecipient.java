package housing.test.mock;

import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import agent.PersonAgent;
import agent.mock.EventLog;
import agent.mock.Mock;

public class MockPayRecipient extends Mock implements PayRecipient {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	public PersonAgent person = new PersonAgent("Mock Pay Recipient");
	public Resident myRes;
	public MockPayRecipient(String name) {
		super(name);
	}

	/* ----- Messages ----- */
	public void msgHereIsPayment(double amount, Resident r) {
		myRes = r;
		if(r == null) return;
		log.add("Received message 'here is payment' " + amount + " from " + myRes);
	}
}