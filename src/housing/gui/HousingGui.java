package housing.gui;

import gui.AnimationPanel;
import housing.ResidentDwelling;
import housing.ResidentialBuilding;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants.Condition;

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
	
	// constants
	private final int ROOM_WIDTH = 600;
	private final int ROOM_HEIGHT= 490;
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui;

	// back-end housing containers that contains the resident, payRecipient, and worker
	ResidentDwelling dwelling;

	// layout for housingAnimationPanel
	GridLayout layout = new GridLayout(1,1);
	
	public HousingGui(int index, ResidentialBuilding building) {
		this.index = index;
		this.index %= 4;
		
		// initialize the graphical layout of the dwelling
		layoutGui = new LayoutGui(ROOM_WIDTH, ROOM_HEIGHT, index);
		
		// initialize the dwelling
		this.dwelling = new ResidentDwelling(index, Condition.GOOD, building, layoutGui);
		
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
		housingAnimationPanel.addGui(dwelling.getResidentGui());
		this.add(housingAnimationPanel);
	}
	
	/* --- Utilities --- */
	
}