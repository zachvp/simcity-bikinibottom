package housing.gui;

import housing.MaintenanceWorkerRole;
import housing.PayRecipientRole;
import housing.ResidentRole;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.PayRecipient;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

/**
 * HousingComplex is the equivalent of one building unit. It has 4 subdivisions
 * that each contain a smaller residential unit. 
 * @author Zach VP
 *
 */
public class HousingComplex extends JPanel {
	/* --- Data --- */
	// some configuration constants
	private final int UNIT_COUNT = 1;
	private final int ROWS = 1;
	private final int COLUMNS = 1;
	private final int SPACING = 1;
	
	// layout manager
	private GridLayout complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);

	// payRecipient who manages the complex 
// 	private Person payRecipientPerson; = new PersonAgent("Pay Recipient");
 	private PayRecipient payRecipientRole;
 	
 	// maintenance worker who repairs dwellings
 	private Person workerPerson = new PersonAgent("Maintenance Worker");
 	private MaintenanceWorker worker = new MaintenanceWorkerRole((PersonAgent) workerPerson);
 	
	// stores all of the housing units in the complex
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex() {
		// set the layout manager
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i, payRecipientRole, worker);
			this.add(gui);
			housingUnits.add(gui);
		}

		// activate complex manager and worker
//		startAndActivate(payRecipientPerson, (Role) payRecipientRole);
		startAndActivate(workerPerson, (Role) worker);
	}
	
	public void addResident(ResidentRole role){
		for(HousingGui unit : housingUnits){
			if(unit.getResident() == null){
				unit.addResidentGui(role);
				break;
			}
		}
	}
	
	public void addPayRecipient(PayRecipientRole role){
		for(HousingGui unit : housingUnits){
			unit.addPayRecipient(role);
		}
	}
	
	private void startAndActivate(Person person, Role role) {
		( (Agent) person).startThread();
		person.addRole(role);
		( (Role) role).activate();
	}

	public PayRecipient getPayRecipientRole() {
		return payRecipientRole;
	}

	public void setPayRecipientRole(PayRecipientRole payRecipientRole) {
		this.payRecipientRole = payRecipientRole;
	}
}
