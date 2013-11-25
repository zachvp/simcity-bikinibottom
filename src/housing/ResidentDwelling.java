package housing;

import classifieds.Classifieds;
import classifieds.ClassifiedsClass;
import agent.Constants;
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
	private Constants.Condition condition;
	
	// cost constants depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	/* --- Constructor --- */
	public ResidentDwelling(Resident resident, PayRecipient payRecipient, int ID, Constants.Condition startCondition) {
		super();
		this.resident = resident;
		this.payRecipient = payRecipient;
		this.IDNumber = ID;
		
		// TODO start condition of unit. Could later be randomized.
		log.add("Creating dwelling with start condition " + startCondition);
		this.condition = startCondition;
		
		switch(condition){
			case GOOD : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT; break;
			case FAIR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.75; break;
			case POOR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
			case BROKEN : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
		}
		
		//Adding to classifieds!
		ClassifiedsClass.getClassifiedsInstance().addDwelling(this);
	}
	
	public void setCondition(Constants.Condition condition){
		this.condition = condition;
	}
	
	public Constants.Condition getCondition(){
		return condition;
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
