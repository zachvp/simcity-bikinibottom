package housing;

import agent.mock.EventLog;
import housing.interfaces.Dwelling;
import housing.interfaces.Resident;
import housing.interfaces.PayRecipient;

/**
 * Dwelling is a housing unit that can be slotted into an apartment complex
 * or expanded to be a full home.
 * @author Zach VP
 *
 */

public class ResidentDwelling implements Dwelling {
	/* --- Data --- */
	EventLog log = new EventLog();
	
	// housing slots
	private Resident resident;
	private PayRecipient payRecipient;
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	enum Condition { GOOD, FAIR, POOR, BROKEN }
	Condition condition;
	
	// cost constants depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	/* --- Constructor --- */
	public ResidentDwelling(Resident resident, PayRecipient payRecipient, int ID, String startCondition) {
		super();
		this.resident = resident;
		this.payRecipient = payRecipient;
		this.IDNumber = ID;
		
		// TODO start condition of unit. Could later be randomized.
		log.add("Creating dwelling with start condition " + startCondition);
		startCondition.toLowerCase();
		if(startCondition == "good") {
			condition = Condition.GOOD;
			monthlyPaymentAmount = MAX_MONTHLY_PAYMENT; 
		}
		else if(startCondition == "fair") {
			condition = Condition.FAIR;
			monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 75;
		}
		else if(startCondition == "poor") {
			condition = Condition.POOR;
			monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5;
		}
		else if(startCondition == "broken") {
			condition = Condition.BROKEN;
			monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.25;
		}
		else {
			monthlyPaymentAmount = 0;
		}
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
