package housing.interfaces;

import housing.MaintenanceWorkerRole;

public interface PayRecipient {
	Object myRes = null;

	/* ----- Normative Messages ----- */
	/** From Resident */
	public void msgHereIsPayment(double amount, Resident r);

	public void addResident(Dwelling dwelling);

	public void msgServiceCharge(double charge, MaintenanceWorker role);

}
