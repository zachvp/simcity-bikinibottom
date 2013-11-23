package housing.gui;

import housing.Dwelling;
import housing.PayRecipientRole;
import housing.ResidentRole;

import java.awt.Color;
import java.awt.Dimension;
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
    int WINDOW_X = 550;
    int WINDOW_Y = 600;

 // payRecipient for this unit
 	PayRecipientRole payRecipientRole;
    
	// add resident
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui();
	ResidentGui residentGui = new ResidentGui(residentRole);
	
	// housing containers
	List<PersonAgent> people = new ArrayList<PersonAgent>();
	Dwelling dwelling = new Dwelling(residentRole, payRecipientRole, index);
	
	public HousingGui(int index, PayRecipientRole payRecipientRole, int width, int height) {
		this.index = index;
		this.setPreferredSize(new Dimension(width, height));
		
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
			case 0: setBackground(Color.BLACK); break;
			case 1: setBackground(Color.RED); break;
			case 2: setBackground(Color.GREEN); break;
			case 3: setBackground(Color.BLUE); break;
			default: setBackground(Color.WHITE); break;
		}
		// add to animation panel
		housingAnimationPanel.addGui(layoutGui);
		housingAnimationPanel.addGui(residentGui);
		housingAnimationPanel.setBackground(Color.BLACK);
		this.add(housingAnimationPanel);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
		role.activate();
	}
}