package housing.test;

import CommonSimpleClasses.Constants.Condition;
import agent.PersonAgent;
import housing.roles.PayRecipientRole;
import housing.roles.ResidentialBuilding;
import housing.roles.PayRecipientRole.MyResident;
import housing.test.mock.MockDwelling;
import housing.test.mock.MockResident;
import junit.framework.TestCase;

public class PayRecipientTest extends TestCase {
	/* --- Testing Roles and Agents --- */
	
	// set up mock units
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
	MockResident resident = new MockResident("Mock Resident");
	
	// set up test person/role
	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
	PayRecipientRole payRecipient = new PayRecipientRole(payRecipientPerson, building);
	
	// living space
	MockDwelling dwelling = new MockDwelling(resident, payRecipient, Condition.GOOD);
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Tests the simplest case of the resident paying
	 * PayRecipient will notify the resident that a payment is due
	 * and the resident will have enough money to pay it.
	*/
	public void testNormativeResidentPaysDues(){
		/* --- Check Precondtions --- */
		
		// list and log should be empty to start
		assertTrue("The list of residents should be empty. Instead there are " + payRecipient.getResidents().size(),
				payRecipient.getResidents().isEmpty());
		assertEquals("Resident should have an empty event log before the message is called. Instead, the Resident's event log reads: "
				+ payRecipient.log.toString(), 0, payRecipient.log.size());
		
		// set up scenario data
		payRecipient.addResident(dwelling);
		MyResident mr = payRecipient.getResidents().get(0);
		
		/* --- Run Scenario --- */
		// there should be one resident in the list
		assertEquals("There should be one resident in the list.",
				1, payRecipient.getResidents().size());
		
		// simulate the charging of residents at the proper time
		payRecipient.chargeResident(mr);
		assertEquals("Resident does not owe the proper amount.",
				dwelling.monthlyPaymentAmount, mr.getOwes());
		
		// send the message to the resident
		dwelling.getResident().msgPaymentDue(mr.getOwes());
		
		// simulate message reception from resident
		payRecipient.msgHereIsPayment(resident.oweMoney, resident);
		
		assertEquals("MyResident data does not reflect the money received from the resident.",
				resident.oweMoney, mr.getPaid());
		
		// call the scheduler after receiving the message
		assertTrue("Scheduler should return true after setting the state of the resident.",
				payRecipient.pickAndExecuteAnAction());
		
		assertEquals("Resident should now owe no money.",
				0.0, mr.getOwes());
	}
}
