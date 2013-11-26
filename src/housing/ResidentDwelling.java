package housing;

import classifieds.ClassifiedsClass;
import CommonSimpleClasses.CityBuilding;
import agent.Constants;
import agent.Constants.Condition;
import agent.mock.EventLog;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
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
	
	/* --- Housing slots --- */
	// roles
	private ResidentRole resident;
	private MaintenanceWorker worker;
	private PayRecipient payRecipient;
	
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	private Constants.Condition condition;
	
	// cost constants depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	/* --- Constructor --- */
	public ResidentDwelling(int ID, Constants.Condition startCondition, ResidentialBuilding building) {
		super();

		this.payRecipient = building.getPayRecipient();
		this.worker = building.getWorker();
		
		this.resident = new ResidentRole(null, building);
		building.addResident(resident);
		
		log.add("Creating dwelling with start condition " + startCondition);
		this.condition = startCondition;
		
		// determine the starting monthly payment for the property
		switch(condition){
			case GOOD : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT; break;
			case FAIR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.75; break;
			case POOR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
			case BROKEN : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
			default : this.monthlyPaymentAmount = 0; break;
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

	public void setResident(ResidentRole resident) {
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

	public MaintenanceWorker getWorker() {
		return worker;
	}

	public void setWorker(MaintenanceWorker worker) {
		this.worker = worker;
	}
}
