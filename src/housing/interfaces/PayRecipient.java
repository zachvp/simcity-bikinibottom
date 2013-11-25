package housing.interfaces;

import housing.ResidentDwelling;

public interface PayRecipient {
	/* ----- Normative Messages ----- */
	/** From Resident */
	public void msgHereIsPayment(double amount, Resident r);

	public void addResident(Dwelling dwelling);
}
