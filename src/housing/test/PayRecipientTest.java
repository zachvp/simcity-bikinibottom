package housing.test;

import agent.PersonAgent;
import housing.PayRecipientRole;
import housing.test.mock.MockResident;
import junit.framework.TestCase;

public class PayRecipientTest extends TestCase {
	/* --- Testing Roles and Agents --- */
	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
	PayRecipientRole payRecipient = new PayRecipientRole(payRecipientPerson);
	
	MockResident mockResident = new MockResident("Mock Resident");
	
	/* --- Constants --- */
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/** Tests the simplest case of the resident paying
	 * PayRecipient will notify the resident that a payment is due
	 * and the resident will have enough money to pay it.
	 * */
	public void testNormativeResidentPaysDues(){
		
	}
}
