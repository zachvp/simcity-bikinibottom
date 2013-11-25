package housing.gui;

import housing.ResidentDwelling;
import housing.ResidentRole;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.PayRecipient;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.PersonAgent;
import agent.Role;
import agent.Constants.Condition;
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
 	PayRecipient payRecipientRole;
 	
 	// worker for this unit
 	MaintenanceWorker worker;

	// add resident
 	ResidentRole resident;
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui(500, 500);

	// back-end housing containers
	ResidentDwelling dwelling = new ResidentDwelling(payRecipientRole, index, Condition.GOOD);

	// layout for housingAnimationPanel
	GridLayout layout = new GridLayout(1,1);

	public HousingGui(int index, PayRecipient payRecipient, MaintenanceWorker worker) {
		this.index = index;
		
		// set the manager for this housing unit
		this.payRecipientRole = payRecipient;
		this.worker = worker;
		
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
		this.add(housingAnimationPanel);
	}
	
	/* --- Utilities --- */
	public void addResidentGui(ResidentRole role){
		this.resident = role;

		// connect resident to proper roles to each other for messaging purposes
		role.setDwelling(dwelling);
		role.setPayee(payRecipientRole);
		role.setWorker(worker);
		
		// connect proper roles to resident
		dwelling.setResident(role);
		
		payRecipientRole.addResident(dwelling);
		
		// set up gui stuff for the role
		ResidentRoleGui residentGui = new ResidentRoleGui(role);
		housingAnimationPanel.addGui(residentGui);
		role.setGui(residentGui);
		role.setLayoutGui(layoutGui);
		
		// finally activate all the roles now that the pointers are sorted out
		role.activate();
		((Role) payRecipientRole).activate();
		((Role) worker).activate();
	}
	
	public ResidentRole getResident(){
		return resident;
	}
}