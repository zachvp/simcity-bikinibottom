package housing.interfaces;

public interface PayRecipient {
	Object myRes = null;

	/* ----- Normative Messages ----- */
	/** From Resident */
	public void msgHereIsPayment(double amount, Resident r);

	public void addResident(Dwelling dwelling);
}
