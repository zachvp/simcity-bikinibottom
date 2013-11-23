package housing.gui;

import housing.Dwelling;
import housing.PayRecipientRole;
import housing.ResidentRole;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;

/**
 * HousingGui pulls together all of the GUI and animation elements including
 * the AnimationPanel 
 * @author Zach VP
 *
 */

public class HousingGui extends JFrame {
	/* --- Data --- */
	
	/** Index is the slot in the complex the gui lies in.  */
	int index;
	
	// TODO these are temporary dimensions that should not be hardcoded
    int WINDOWX = 550;
    int WINDOWY = 600;
	
	// add resident
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	
	// add payRecipient
	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
	PayRecipientRole payRecipientRole = new PayRecipientRole(payRecipientPerson);
	
	// set up animation
	AnimationPanel housingAnimationPanel = new AnimationPanel();
	LayoutGui layoutGui = new LayoutGui();
	ResidentGui residentGui = new ResidentGui(residentRole);
	
	// housing containers
	List<PersonAgent> people = new ArrayList<PersonAgent>();
	Dwelling dwelling = new Dwelling(residentRole, payRecipientRole, index);
	
	public HousingGui() {
        setBounds(50, 50, WINDOWX, WINDOWY);
        
		this.add(housingAnimationPanel);
		
		payRecipientRole.addResident(dwelling);
		
		/* -- Set up people --- */
		people.add(residentPerson);
		people.add(payRecipientPerson);
		
		startAndActivate(residentPerson, residentRole);
		startAndActivate(payRecipientPerson, payRecipientRole);
		
		residentRole.setGui(residentGui);
		residentGui.setLayoutGui(layoutGui);
		
		/* --- Add to Animation Panel --- */
		housingAnimationPanel.addGui(layoutGui);
		housingAnimationPanel.addGui(residentGui);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(residentRole);
		role.activate();
	}
	
	public static void main(String[] args) {
		HousingGui gui = new HousingGui();
		gui.setTitle("Housing Panel");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}