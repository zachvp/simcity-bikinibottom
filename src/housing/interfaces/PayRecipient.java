package housing.interfaces;

public interface PayRecipient {
	/* ----- Normative Messages ----- */
	/** From Resident */
	public void msgHereIsPayment(double amount, Resident r);
}
