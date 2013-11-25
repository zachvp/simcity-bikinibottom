package housing.test;

import housing.ResidentRole;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;
import housing.test.mock.MockResident;
import housing.test.mock.MockResidentGui;
import agent.PersonAgent;
import junit.framework.TestCase;

public class MaintenanceWorkerTest extends TestCase {
	// testing Roles and Agents
	PersonAgent person = new PersonAgent("Maintenance Worker");
	ResidentRole worker = new ResidentRole(person);
	ResidentGui gui = new MockResidentGui(worker);
		
	// mock roles
	Resident resident = new MockResident("Resident");

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testFixDwelling() {
		
	}

}
