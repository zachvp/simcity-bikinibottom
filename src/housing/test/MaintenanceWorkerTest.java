package housing.test;

import housing.MaintenanceWorkerRole;
import housing.interfaces.Dwelling;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import housing.test.mock.MockDwelling;
import housing.test.mock.MockMaintenanceWorkerGui;
import housing.test.mock.MockPayRecipient;
import housing.test.mock.MockResident;
import CommonSimpleClasses.Constants.Condition;
import agent.PersonAgent;
import junit.framework.TestCase;

public class MaintenanceWorkerTest extends TestCase {
	// testing Roles and Agents
	PersonAgent person = new PersonAgent("Maintenance Worker");
	MaintenanceWorkerRole worker = new MaintenanceWorkerRole(person);
	MockMaintenanceWorkerGui gui = new MockMaintenanceWorkerGui(worker); 
		
	// mock roles
	Resident resident = new MockResident("Resident");
	Dwelling dwelling = new MockDwelling(resident, Condition.GOOD);
	PayRecipient payRecipient= new MockPayRecipient("Pay Recipient");

	protected void setUp() throws Exception {
		super.setUp();
		worker.setGui(gui);
	}
	
	/**
	 * Tests the simplest case of MaintenanceWorker receiving a complaint,
	 * filing a work order, and fixing the problem.
	 */
	public void testFixDwelling() {
		
		/* --- Test Preconditions --- */
		assertEquals("Worker should have an empty event log before the message is called. Instead, the Resident's event log reads: "
				+ worker.log.toString(), 0, worker.log.size());
		assertEquals("Worker should have no work orders to start.",
				0, worker.getWorkOrders().size());
		
		/* --- Run the Scenario --- */
		// send the message that would normally be sent from Resident
		worker.msgFileWorkOrder(dwelling);
		
		assertEquals("Worker should now have 1 WorkOrder",
				1, worker.getWorkOrders().size());
		
		assertTrue("Scheduler should return true after creating a new work order",
				worker.pickAndExecuteAnAction());
		
		// post conditions once dwelling is fixed
		assertEquals("Dwelling condition should now be GOOD",
				Condition.GOOD, dwelling.getCondition());
		
		assertEquals("Worker should now have no work orders.",
				0, worker.getWorkOrders().size());
		
		assertFalse("Worker is done with all work orders, scheduler should return false now.",
				worker.pickAndExecuteAnAction());
	}
	
	/**
	 * Just like the above the test, except now testing the charge service b/t MaintenanceWorker
	 * and PayRecipient.
	 * @author Zach VP
	 */
	public void testChargePayRecipientForService() {
		/* --- Test Preconditions --- */
		assertEquals("Worker should have an empty event log before the message is called. Instead, the Resident's event log reads: "
				+ worker.log.toString(), 0, worker.log.size());
		assertEquals("Worker should have no work orders to start.",
				0, worker.getWorkOrders().size());
		
		/* --- Run the Scenario --- */
		// send the message that would normally be sent from Resident
		worker.msgFileWorkOrder(dwelling);
		
		assertEquals("Worker should now have 1 WorkOrder",
				1, worker.getWorkOrders().size());
		
		assertTrue("Scheduler should return true after creating a new work order",
				worker.pickAndExecuteAnAction());
		
		// post conditions once dwelling is fixed
		assertEquals("Dwelling condition should now be GOOD",
				Condition.GOOD, dwelling.getCondition());
		
		
		
		assertEquals("Worker should now have no work orders.",
				0, worker.getWorkOrders().size());
		
		assertFalse("Worker is done with all work orders, scheduler should return false now.",
				worker.pickAndExecuteAnAction());
	}
}
