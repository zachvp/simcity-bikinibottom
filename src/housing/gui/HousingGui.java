package housing.gui;

import housing.Dwelling;
import housing.PayRecipientRole;
import housing.ResidentRole;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;

/**
 * HousingGui pulls together all of the GUI and animation elements including
 * the AnimationPanel, role graphics, and layout graphics. 
 * @author Zach VP
 *
 */

public class HousingGui extends JPanel {
	/* --- Data --- */
	
	/** Index is the slot in the complex the gui lies in.  */
	int index;
	
	// TODO these are temporary dimensions that should not be hardcoded
    int WINDOW_X = 550/4;
    int WINDOW_Y = 600/4;
	
	// add resident
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	
	// add payRecipient
	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
	PayRecipientRole payRecipientRole = new PayRecipientRole(payRecipientPerson);
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui();
	ResidentGui residentGui = new ResidentGui(residentRole);
	
	// housing containers
	List<PersonAgent> people = new ArrayList<PersonAgent>();
	Dwelling dwelling = new Dwelling(residentRole, payRecipientRole, index);
	
	public HousingGui(int index) {
		this.index = index;
		setBounds(WINDOW_X*index + WINDOW_Y%50, index*50, WINDOW_X, WINDOW_Y);
		
		this.add(housingAnimationPanel);
		
		payRecipientRole.addResident(dwelling);
		
		// add people to the list
		people.add(residentPerson);
		people.add(payRecipientPerson);
		
		// activate roles
		startAndActivate(residentPerson, residentRole);
		startAndActivate(payRecipientPerson, payRecipientRole);
		
		// assign guis to the resident
		residentRole.setGui(residentGui);
		residentGui.setLayoutGui(layoutGui);
		
		// add to animation panel
		housingAnimationPanel.addGui(layoutGui);
		housingAnimationPanel.addGui(residentGui);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(residentRole);
		role.activate();
	}
}