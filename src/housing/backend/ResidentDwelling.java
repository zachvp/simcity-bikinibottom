package housing.backend;

import housing.gui.DwellingGui;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import mock.EventLog;
import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import agent.gui.Gui;
import classifieds.ClassifiedsClass;

/**
 * Dwelling is a housing unit that can be slotted into an apartment complex
 * or expanded to be a full home. It contains all of the Role information
 * necessary for a house. HousingGui is the graphical representation of this.
 * @author Zach VP
 */

public class ResidentDwelling implements Dwelling {
	/* --- Data --- */
	public EventLog log = new EventLog();
	
	private HousingComplex complex;
	
	/* --- Housing slots --- */
	
	// roles
	private ResidentRole resident;
	private MaintenanceWorker worker;
	private PayRecipientRole payRecipient;
	
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	private Constants.Condition condition;
	
	// cost constant depending on housing condition
	private final int MAX_MONTHLY_PAYMENT = 64;
	
	// gui slots
	DwellingGui gui;
	
	// TODO just test people
	PersonAgent person;
	PersonAgent workPerson;
	PersonAgent payPerson;
	// end test
	
	/* --- Constructor --- */
	public ResidentDwelling(int ID, Constants.Condition startCondition,
			HousingComplex complex) {
		super();
		
		// so the dwelling knows what housing complex it's in
		this.complex = complex;
		
		// set the position index of this dwelling
		this.IDNumber = ID;
		
		// create instance of the internal dwelling layout graphics
		this.gui = new DwellingGui(ID);
		
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
	}
	
	public void addResident() {
		if(Constants.TEST_POPULATE_HOUSING){
			PersonAgent person = new PersonAgent("Resident");
			
			resident = new ResidentRole(person, complex.getBuilding(), this, gui);
			person.addRole(resident);
			resident.activate();
			this.complex.addResident(resident);
			
			person.startThread();
			
			complex.addRoleToUnit(IDNumber, resident);
		} else {
			this.payRecipient = complex.getPayRecipient();
			this.worker = complex.getWorker();
			this.resident = new ResidentRole(null, complex.getBuilding(), this, gui);
			
			this.complex.addResident(resident);
			complex.addRoleToUnit(IDNumber, resident);
		}
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
	
	public Gui getLayoutGui() {
		return gui;
	}
	
	public String toString() {
		return "Room in building " + complex.getBuilding();
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
	
	public void degradeCondition() {
		// TODO test 
		condition = Constants.Condition.POOR;
		resident.msgDwellingDegraded();
	}

	@Override
	public void setWorker(MaintenanceWorker worker) {
		this.worker = worker;
	}
}
