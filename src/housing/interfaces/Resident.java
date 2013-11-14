package housing.interfaces;

public interface Resident {
	/** Normative Messages from Payment Collector
	 * Examples: landlord, bank */
	public void msgPaymentDue(double amount, int accountNumber);
}
