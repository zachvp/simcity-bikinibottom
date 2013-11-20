package housing.test;

import agent.PersonAgent;
import housing.PayRecipientRole;
import housing.ResidentRole;
import housing.test.mock.MockPayRecipient;
import junit.framework.TestCase;

public class ResidentTest extends TestCase {
	/* --- Testing Roles and Agents --- */
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole resident = new ResidentRole(residentPerson);
	
	MockPayRecipient mockPayRecipient = new MockPayRecipient("Mock Pay Recipient");
	
	/* --- Constants --- */
	private final double PAYMENT_AMOUNT = 500;
	
	protected void setUp() throws Exception {
		super.setUp();
		resident.activate();
		residentPerson.startThread();
	}
	
	/** Tests the simplest case of the resident paying
	 * PayRecipient will notify the resident that a payment is due
	 * and the resident will have enough money to pay it.
	 * */
	public void testNormativeCaseResidentPaysDues(){
		resident.setPayee(mockPayRecipient);
		resident.getPerson().getWallet().setCashOnHand(PAYMENT_AMOUNT);;
		/* --- Test Preconditions --- */
		assertEquals("Resident should owe $0.0.", resident.getMoneyOwed(), 0.0);
		assertEquals("Resident should have an empty event log before the message is called. Instead, the Resident's event log reads: "
				+ resident.log.toString(), 0, resident.log.size());
		
		/* --- Run Scenario --- */
		resident.msgPaymentDue(PAYMENT_AMOUNT);
		assertEquals("Resident should now owe " + PAYMENT_AMOUNT + ".",
				PAYMENT_AMOUNT, resident.getMoneyOwed());
		//check that the pay recipient received the pay message from the resident
		assertTrue("Resident's scheduler should have returned true, reacting to the payment due message",
				resident.pickAndExecuteAnAction());
		assertEquals("Pay recipient should have the instance of resident.",
				resident, mockPayRecipient.myRes);
	}
	/** Tests the simplest resident cooking and eating case.
	 * Resident will be hungry, have food to use, and will have enough
	 * time to cook.
	 * */
	public void testNormativeCookAndEat(){
		/* --- Test Preconditions --- */
		assertFalse("Resident should not be hungry to start.",
				resident.isHungry());
		assertEquals("Food resident is handling should be null.",
				null, resident.getFood());
		assertTrue("Resident grocery list should be empty.",
				resident.getGroceries().isEmpty());
		assertFalse("Resident should not have an empty fridge.",
				resident.getRefrigerator().isEmpty());
		
		/* --- Run the Scenario --- */
		resident.setHungry(true);
		assertTrue("Scheduler should return true after resident is hungry "
				+ "and there are items in the fridge.", resident.pickAndExecuteAnAction());
		assertTrue("Scheduler should return true after resident puts food on stove.",
				resident.pickAndExecuteAnAction());
		assertFalse("Hungry should be false", resident.isHungry());
		assertEquals("Food should be NULL", null, resident.getFood());
	}
}
