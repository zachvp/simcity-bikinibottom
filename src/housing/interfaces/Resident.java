package housing.interfaces;

public interface Resident {
	/* ----- Normative Messages  ----- */
	/** From payment collector
	 * @param amount is how much the resident owes
	 */
	public void msgPaymentDue(double amount);

	public void msgAtDestination();

	public void msgDwellingFixed();

	void msgDwellingDegraded();
}
