package housing.interfaces;

public interface Resident {
	/* ----- Normative Messages  ----- */
	/** From payment collector
	 * @param amount is how much the resident owes
	 */
	public void msgPaymentDue(double amount, PayRecipient payRecipient);

	public void msgAtDestination();

	public void msgDwellingFixed();

	void msgDwellingDegraded();
	
	public enum FoodState { RAW, COOKED, COOKING };
}
