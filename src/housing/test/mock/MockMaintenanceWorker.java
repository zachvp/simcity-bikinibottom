package housing.test.mock;

import mock.EventLog;
import mock.Mock;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;

public class MockMaintenanceWorker extends Mock implements MaintenanceWorker {
	/* --- Data --- */
	public EventLog log = new EventLog();
	
	public Dwelling dwelling;

	public MockMaintenanceWorker(String name, Dwelling dwelling) {
		super(name);
		
		this.dwelling = dwelling;
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

	@Override
	public void setDwelling(Dwelling dwelling) {
		this.dwelling = dwelling;
	}

}
