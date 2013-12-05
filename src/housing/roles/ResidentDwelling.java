package housing.roles;

import agent.PersonAgent;
import agent.gui.Gui;
import mock.EventLog;
import classifieds.ClassifiedsClass;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import housing.gui.HousingComplex;
import housing.gui.LayoutGui;
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
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	/* --- Housing slots --- */
	
	// roles
	private ResidentRole resident;
	private MaintenanceWorkerRole worker;
	private PayRecipientRole payRecipient;
	
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	private Constants.Condition condition;
	
	// cost constant depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	// TODO just test people
	PersonAgent person;
	PersonAgent workPerson;
	PersonAgent payPerson;
	// end test
	
	/* --- Constructor --- */
	public ResidentDwelling(int ID, Constants.Condition startCondition,
			ResidentialBuilding building, LayoutGui gui, HousingComplex complex) {
		super();
		
		// TODO actual code below
		this.building = building;
		
		if(!Constants.DEBUG){
			this.payRecipient = building.getPayRecipient();
			this.worker = building.getWorker();
			this.resident = new ResidentRole(null, building, this, gui);
		}
		// end actual code
		
		// TODO implemented test hacks
		if(Constants.DEBUG){
			this.person = new PersonAgent("Spongebob");
//			this.workPerson = new PersonAgent("Maintenence Worker");
//			this.payPerson = new PersonAgent("Landlord");
			
			this.resident = new ResidentRole(person, building, this, gui);
			this.person.addRole(resident);
			this.person.startThread();
			this.resident.activate();
			
//			this.worker = new MaintenanceWorkerRole(workPerson, building);
//			this.workPerson.addRole(worker);
//			this.workPerson.startThread();
//			this.worker.activate();
//			
//			this.payRecipient = new PayRecipientRole(payPerson, building);
//			this.payPerson.addRole(payRecipient);
//			this.payPerson.startThread();
//			this.payRecipient.activate();
		}
		// ---- end test hacks
		
		this.building.addResident(resident);
		
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
//		Runnable command = new Runnable() {
//			@Override
//			public void run() {
//				// TODO make the dwelling degrade gradually
//				condition = Constants.Condition.POOR;
//				resident.msgDwellingDegraded();
//			}
//		};
//		// TODO test 
//		condition = Constants.Condition.POOR;
//		resident.msgDwellingDegraded();
//		
//		
//		// degrade condition every day
//		int hour = 16;
//		int minute = 0;
//		schedule.scheduleDailyTask(command, hour, minute);
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
	
	public Gui getResidentGui() {
		return resident.getGui();
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
		this.payRecipient = (PayRecipientRole) payRecipient;
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

	public void setWorker(MaintenanceWorkerRole worker) {
		this.worker = worker;
	}
}
