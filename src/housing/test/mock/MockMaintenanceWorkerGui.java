package housing.test.mock;

import mock.EventLog;
import housing.interfaces.MaintenanceWorkerGui;
import housing.interfaces.Resident;

public class MockMaintenanceWorkerGui implements MaintenanceWorkerGui {
	EventLog log = new EventLog();
	Resident resident;
	
	boolean hasTool = false;

	public MockMaintenanceWorkerGui(Resident resident) {
		this.resident = resident;
	}

	@Override
	public void setTool(boolean b) {
		hasTool = b;
	}

	@Override
	public void DoGoToDwelling() {
		log.add("Going to dwelling");
	}

	@Override
	public void DoFixProblem() {
		log.add("Going to dwelling");
	}

	@Override
	public void DoReturnHome() {
		log.add("Going to dwelling");
	}
}
