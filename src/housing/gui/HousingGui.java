package housing.gui;

import gui.AnimationPanel;
import housing.backend.ResidentDwelling;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import agent.gui.Gui;

/**
 * HousingGui displays individual housing units. It pulls together all of the
 * GUI and animation elements including the AnimationPanel, role graphics, and
 * layout graphics. 
 * @author Zach VP
 */

@SuppressWarnings("serial")
public class HousingGui extends JPanel {
	/* --- Data --- */

	/** Index is the slot in the complex the gui lies in.  */
	int index;
	
	// set up animation and graphics elements
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui;

	// layout for housingAnimationPanel
	GridLayout layout = new GridLayout(1,1);
	
	public HousingGui(int index, ResidentDwelling dwelling) {
		this.index = index;
		
		this.index %= 4;
		switch(this.index){
			case 0: housingAnimationPanel.setBackground(Color.YELLOW); break;
			case 1: housingAnimationPanel.setBackground(Color.RED); break;
			case 2: housingAnimationPanel.setBackground(Color.GREEN); break;
			case 3: housingAnimationPanel.setBackground(Color.BLUE); break;
			default: housingAnimationPanel.setBackground(Color.BLACK); break;
		}
		
		// add to animation panel
		this.setLayout(layout);
		this.housingAnimationPanel.addGui(dwelling.getLayoutGui());
		this.housingAnimationPanel.addGui(dwelling.getResidentGui());
		this.add(housingAnimationPanel);
	}
	
	/* --- Utilities --- */
	public int getIndex() {
		return index;
	}
	
	public void addGui(Gui gui) {
		housingAnimationPanel.addGui(gui);
	}

	public void removeGui(Gui gui) {
		housingAnimationPanel.removeGui(gui);
	}
}