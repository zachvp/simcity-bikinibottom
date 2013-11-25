package housing.test;

import housing.MaintenanceWorkerRole;
import housing.interfaces.Dwelling;
import housing.interfaces.Resident;
import housing.test.mock.MockDwelling;
import housing.test.mock.MockResident;
import agent.Constants.Condition;
import agent.PersonAgent;
import junit.framework.TestCase;

public class MaintenanceWorkerTest extends TestCase {
	// testing Roles and Agents
	PersonAgent person = new PersonAgent("Maintenance Worker");
	MaintenanceWorkerRole worker = new MaintenanceWorkerRole(person);
		
	// mock roles
	Resident resident = new MockResident("Resident");
	Dwelling dwelling = new MockDwelling(resident, Condition.GOOD);

	protected void setUp() throws Exception {
		super.setUp();
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
		
		assertFalse("Worker is done with all work orders, scheduler should return false now.",
				worker.pickAndExecuteAnAction());
		
		assertEquals("Worker should have no work orders to start.",
				0, worker.getWorkOrders().size());
	}
}