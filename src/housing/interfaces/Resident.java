package housing.interfaces;

import java.util.Map;

public interface Resident {
	/* ----- Normative Messages  ----- */
	/** From payment collector
	 * @param amount is how much the resident owes
	 */
	public void msgPaymentDue(double amount);

	public void msgAtDestination();
}
