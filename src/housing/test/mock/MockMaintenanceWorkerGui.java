package housing.test.mock;

import mock.EventLog;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.MaintenanceWorkerGui;

public class MockMaintenanceWorkerGui implements MaintenanceWorkerGui {
	EventLog log = new EventLog();
	MaintenanceWorker worker;
	
	boolean hasTool = false;

	public MockMaintenanceWorkerGui(MaintenanceWorker worker) {
		this.worker = worker;
	}

	@Override
	public void setTool(boolean b) {
		hasTool = b;
	}

	@Override
	public void DoGoToDwelling(int unit) {
		log.add("Going to dwelling");
		worker.msgAtDestination();
	}

	@Override
	public void DoFixProblem() {
		log.add("Going to dwelling");
		worker.msgAtDestination();
	}

	@Override
	public void DoReturnHome() {
		log.add("Going to dwelling");
		worker.msgAtDestination();
	}
}
