package housing.gui;

import housing.MaintenanceWorkerRole;
import housing.PayRecipientRole;
import housing.ResidentRole;
import housing.ResidentialBuilding;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import CommonSimpleClasses.CityBuilding;
import agent.Agent;
import agent.Role;
import agent.interfaces.Person;

/**
 * HousingComplex is the equivalent of one building unit. It has 4 subdivisions
 * that each contain a smaller residential unit. 
 * @author Zach VP
 *
 */
public class HousingComplex extends JPanel {
	/* --- Data --- */
	// some configuration constants
	private final int UNIT_COUNT = 4;
	private final int ROWS = 2;
	private final int COLUMNS = 2;
	private final int SPACING = 10;
	
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
			HousingGui gui = new HousingGui(i, building);
			this.add(gui);
			housingUnits.add(gui);
		}

	}
}
