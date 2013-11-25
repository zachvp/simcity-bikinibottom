package housing.gui;

import housing.MaintenanceWorkerRole;
import housing.PayRecipientRole;
import housing.ResidentRole;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

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
	private GridLayout complexLayout = new GridLayout(ROWS, COLUMNS, SPACING, SPACING);

	// payRecipient who manages the complex 
// 	private Person payRecipientPerson; = new PersonAgent("Pay Recipient");
 	
	// stores all of the housing units in the complex
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex() {
		// set the layout manager
		this.setLayout(complexLayout);
		
		/**
		 * Add as many units as specified to the complex. Units will
		 * be partitioned according to the GridLayout.
		 */
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i);
			this.add(gui);
			housingUnits.add(gui);
		}

	}
	
	public void addResident(ResidentRole role){
		for(HousingGui unit : housingUnits){
			if(unit.getResident() == null){
				unit.addResidentGui(role);
				break;
			}
		}
	}
	
	public void addPayRecipient(PayRecipientRole role){
		for(HousingGui unit : housingUnits){
			if(unit.getResident() != null){
				unit.addPayRecipient(role);
				break;
			}
		}
	}
	
	public void addWorker(MaintenanceWorkerRole role){
		for(HousingGui unit : housingUnits){
			if(unit.getResident() != null){
				unit.addWorker(role);
				break;
			}
		}
	}
	
	private void startAndActivate(Person person, Role role) {
		( (Agent) person).startThread();
		person.addRole(role);
		( (Role) role).activate();
	}
}
