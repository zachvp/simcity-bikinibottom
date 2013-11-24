package housing.gui;

import housing.PayRecipientRole;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.PersonAgent;
import agent.Role;
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
	GridLayout complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);

	// payRecipient who manages the complex 
 	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
 	private PayRecipientRole payRecipientRole = new PayRecipientRole(payRecipientPerson);
	
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex() {
		// set the layout manager
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i, payRecipientRole);
			this.add(gui);
			housingUnits.add(gui);
		}
		
		// activate complex manager
		startAndActivate(payRecipientPerson, payRecipientRole);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
		role.activate();
	}

	public PayRecipientRole getPayRecipientRole() {
		return payRecipientRole;
	}

	public void setPayRecipientRole(PayRecipientRole payRecipientRole) {
		this.payRecipientRole = payRecipientRole;
	}
}
