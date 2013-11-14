package housing.interfaces;

import java.util.Map;

public interface Resident {
	/** Normative Messages from Payment Collector
	 * Examples: landlord, bank */
	public void msgPaymentDue(double amount, Map<Resident, Double> dropBox);
}
