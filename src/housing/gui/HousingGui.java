package housing.gui;

import housing.Dwelling;
import housing.PayRecipientRole;
import housing.ResidentRole;
import housing.interfaces.ResidentGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;

/**
 * HousingGui displays individual housing units. It pulls together all of the
 * GUI and animation elements including the AnimationPanel, role graphics, and
 * layout graphics. 
 * @author Zach VP
 *
 */

public class HousingGui extends JPanel {
	/* --- Data --- */

	/** Index is the slot in the complex the gui lies in.  */
	int index;
	
	// payRecipient for this unit
 	PayRecipientRole payRecipientRole;

	// add resident
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui(500, 500);
	ResidentRoleGui residentGui = new ResidentRoleGui(residentRole);

	// back-end housing containers
	List<PersonAgent> people = new ArrayList<PersonAgent>();
	Dwelling dwelling = new Dwelling(residentRole, payRecipientRole, index, "good");

	// layout for housingAnimationPanel
	GridLayout layout = new GridLayout(1,1);

	public HousingGui(int index, PayRecipientRole payRecipientRole) {
		this.index = index;
		
		// set the manager for this housing unit
		this.payRecipientRole = payRecipientRole;
		
		// add the resident to the pay recipient's charges
		residentRole.setPayee(payRecipientRole);
		payRecipientRole.addResident(dwelling);
		
		// add people to the list
		people.add(residentPerson);
		
		// activate roles
		startAndActivate(residentPerson, residentRole);
		
		// assign guis to the resident
		residentRole.setGui(residentGui);
		residentGui.setLayoutGui(layoutGui);
		
		switch(index){
			case 0: housingAnimationPanel.setBackground(Color.YELLOW); break;
			case 1: housingAnimationPanel.setBackground(Color.RED); break;
			case 2: housingAnimationPanel.setBackground(Color.GREEN); break;
			case 3: housingAnimationPanel.setBackground(Color.BLUE); break;
			default: housingAnimationPanel.setBackground(Color.BLACK); break;
		}
		
		// add to animation panel
		this.setLayout(layout);
		housingAnimationPanel.addGui(layoutGui);
		housingAnimationPanel.addGui(residentGui);
		this.add(housingAnimationPanel);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
		role.activate();
	}
}