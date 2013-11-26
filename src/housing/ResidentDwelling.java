package housing;

import mock.EventLog;
import classifieds.ClassifiedsClass;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.Resident;
import housing.interfaces.PayRecipient;

/**
 * Dwelling is a housing unit that can be slotted into an apartment complex
 * or expanded to be a full home. It contains all of the Role information
 * necessary for a house. HousingGui is the graphical representation of this.
 * @author Zach VP
 */

public class ResidentDwelling implements Dwelling {
	/* --- Data --- */
	public EventLog log = new EventLog();
	
	// building the dwelling belongs to
	private ResidentialBuilding building;
	private ScheduleTask schedule;
	
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
	
	// cost constant depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	/* --- Constructor --- */
	public ResidentDwelling(int ID, Constants.Condition startCondition, ResidentialBuilding building) {
		super();

		this.building = building;
		
		this.payRecipient = building.getPayRecipient();
		this.worker = building.getWorker();
		
		this.resident = new ResidentRole(null, building);
		this.building.addResident(resident);
		
		log.add("Creating dwelling with start condition " + startCondition);
		this.condition = startCondition;
		
		// determine the starting monthly payment for the property
		switch(this.condition){
			case GOOD : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT; break;
			case FAIR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.75; break;
			case POOR : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
			case BROKEN : this.monthlyPaymentAmount = MAX_MONTHLY_PAYMENT * 0.5; break;
			default : this.monthlyPaymentAmount = 0; break;
		}
		
		// Adding to classifieds!
		ClassifiedsClass.getClassifiedsInstance().addDwelling(this);
		
		// degrade condition of dwelling each day
		Runnable command = new Runnable() {
			@Override
			public void run() {
				// TODO make the dwelling degrade gradually
				condition = Constants.Condition.POOR;
			}
		};
				
		// every day at noon
		int hour = 6;
		int minute = 10;
		
		// TODO make this work in a non-role class
		schedule.scheduleDailyTask(command, hour, minute);
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
	
	public String toString() {
		return "Room at " + building;
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
