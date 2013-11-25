package housing.test.mock;

import agent.mock.EventLog;
import agent.mock.Mock;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;

public class MockMaintenanceWorker extends Mock implements MaintenanceWorker {
	/* --- Data --- */
	EventLog log = new EventLog();

	public MockMaintenanceWorker(String name) {
		super(name);
	}

	@Override
	public void msgFileWorkOrder(Dwelling dwelling) {
		log.add("Received message 'file work order.'");
	}

}
