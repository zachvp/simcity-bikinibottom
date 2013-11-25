package housing.gui;

import housing.MaintenanceWorkerRole;
import housing.PayRecipientRole;
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
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;

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
	private final int SPACING = 0;
	
	// layout manager
	private GridLayout complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);

	// payRecipient who manages the complex 
 	private Person payRecipientPerson = new PersonAgent("Pay Recipient");
 	private PayRecipient payRecipientRole = new PayRecipientRole(payRecipientPerson);
 	
 	// maintenance worker who repairs dwellings
 	private Person workerPerson = new PersonAgent("Maintenance Worker");
 	private MaintenanceWorker worker = new MaintenanceWorkerRole((PersonAgent) workerPerson);
 	
	
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
		
		// activate complex manager
		startAndActivate(payRecipientPerson, (Role) payRecipientRole);
		startAndActivate(workerPerson, (Role) worker);
	}
	
	private void startAndActivate(Person person, Role payRecipientRole) {
		((Agent) payRecipientPerson).startThread();
		payRecipientPerson.addRole((Role) payRecipientRole);
		((Role) payRecipientRole).activate();
	}

	public PayRecipient getPayRecipientRole() {
		return payRecipientRole;
	}

	public void setPayRecipientRole(PayRecipientRole payRecipientRole) {
		this.payRecipientRole = payRecipientRole;
	}
}
