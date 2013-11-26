package housing.gui;

import housing.MaintenanceWorkerRole;
import housing.PayRecipientRole;
import housing.ResidentDwelling;
import housing.ResidentRole;
import housing.ResidentialBuilding;
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
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui(500, 500);

	// back-end housing containers that contains the resident, payRecipient, and worker
	ResidentDwelling dwelling;

	// layout for housingAnimationPanel
	GridLayout layout = new GridLayout(1,1);
	
	public HousingGui(int index, ResidentialBuilding building) {
		this.index = index;
		this.index %= 4;
		
		// initialize the dwelling
		this.dwelling = new ResidentDwelling(index, Condition.GOOD, building);
		
		switch(this.index){
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
	
}