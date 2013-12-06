package housing.test.mock;

import mock.EventLog;
import mock.Mock;
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

	@Override
	public void msgAtDestination() {
		log.add("Received message 'at destination'");
	}

	@Override
	public void msgHereIsPayment(double payment) {
		log.add("Received payment from PayRecipient.");
	}

}
