package housing.gui;

import housing.backend.HousingComplex;
import housing.backend.ResidentDwelling;
import housing.interfaces.Dwelling;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import CommonSimpleClasses.Constants;
import CommonSimpleClasses.Constants.Condition;
import agent.gui.Gui;

@SuppressWarnings("serial")
public class HousingComplexGui extends JPanel {

	HousingComplex complex;
	
	private List<Dwelling> dwellings = new ArrayList<Dwelling>();
	
	// layout manager
	private GridLayout complexLayout;

	// stores all of the housing units in the complex
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	// how many units will be in a row or column within a complex
	private final int SUBDIVISIONS = (int) Math.sqrt(Constants.HOUSING_UNIT_COUNT);
	
	public HousingComplexGui(HousingComplex complex) {
		
		this.complex = complex;
		
		// create layout and set the layout manager
		complexLayout = new GridLayout(SUBDIVISIONS, SUBDIVISIONS,
				Constants.HOUSING_UNIT_SPACING, Constants.HOUSING_UNIT_SPACING);
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < Constants.HOUSING_UNIT_COUNT; i++){
			ResidentDwelling dwelling = new ResidentDwelling(i, Condition.GOOD, complex);
			dwelling.setWorker(complex.getWorker());
			dwelling.setPayRecipient(complex.getPayRecipient());

			// add the resident using the factory
			dwelling.addResident();
			
			// the complex gui needs to keep track of the dwellings so it can
			// swap guis among dwellings
			this.dwellings.add(dwelling);
			
			HousingGui gui = new HousingGui(i, dwelling);
			this.add(gui);
			housingUnits.add(gui);
			this.setPreferredSize(new Dimension(Constants.ANIMATION_PANEL_WIDTH,
					Constants.ANIMATION_PANEL_HEIGHT));
		}
	}


	/* --- Utilities --- */
	public List<Dwelling> getDwellings() {
		return dwellings;
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
