package housing.gui;

import housing.ResidentialBuilding;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import agent.gui.Gui;

/**
 * HousingComplex is the equivalent of one building unit. It has 4 subdivisions
 * that each contain a smaller residential unit. 
 * @author Zach VP
 *
 */
public class HousingComplex extends JPanel {
	/* --- Data --- */
	// some configuration constants
	private final int UNIT_COUNT = 9;
	private final int ROWS = 3;
	private final int COLUMNS = 3;
	private final int SPACING = 5;
	
	// layout manager
	private GridLayout complexLayout;

	// stores all of the housing units in the complex
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex(ResidentialBuilding building) {
		// create layout and set the layout manager
		complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i, building, this);
			this.add(gui);
			housingUnits.add(gui);
		}

	}
	
	// this is to get a maintenance worker in the right building
	public void addGuiToDwelling(Gui gui, int unitNumber) {
		for(HousingGui unit : housingUnits) {
			if(unit.getIndex() == unitNumber) {
				unit.addGui(gui);
			}
		}
	}
	
	// take the gui out once it's done visiting/fixing
	public void removeGuiFromDwelling(Gui gui, int unitNumber) {
		for(HousingGui unit : housingUnits) {
			if(unit.getIndex() == unitNumber) {
				unit.removeGui(gui);
			}
		}
	}
}
