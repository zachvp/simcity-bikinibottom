package housing;

import housing.interfaces.Resident;
import housing.interfaces.PayRecipient;

/**
 * Dwelling is an apartment within the larger complex.
 * There will be 4 apartments per complex. 
 * @author Zach VP
 *
 */

public class Dwelling {
	/* --- Data --- */
	private Resident resident;
	private PayRecipient payRecipient;
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	enum Condition { GOOD, FAIR, POOR, BROKEN }
	Condition condition = Condition.GOOD;
	
	/* --- Constructor --- */
	public Dwelling(Resident resident, PayRecipient payRecipient, int ID) {
		super();
		this.resident = resident;
		this.payRecipient = payRecipient;
		this.IDNumber = ID;
	}

	public int getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(int iDNumber) {
		IDNumber = iDNumber;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public PayRecipient getPayRecipient() {
		return payRecipient;
	}

	public void setPayRecipient(PayRecipient payRecipient) {
		this.payRecipient = payRecipient;
	}

	public double getMonthlyPaymentAmount() {
		return monthlyPaymentAmount;
	}

	public void setMonthlyPaymentAmount(double monthlyPaymentAmount) {
		this.monthlyPaymentAmount = monthlyPaymentAmount;
	}
}
