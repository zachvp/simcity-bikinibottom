package housing.gui;

import housing.PayRecipientRole;
import housing.ResidentRole;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;

public class HousingGui extends JFrame {
	/* --- Keeps track of all the elements --- */
	List<PersonAgent> people = new ArrayList<PersonAgent>();
	
	/* -- Add People to test --- */
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	ResidentGui residentGui = new ResidentGui(residentRole);
	
	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
	PayRecipientRole payRecipientRole = new PayRecipientRole(payRecipientPerson);
	
	/* --- Create an instance of the LayoutGui --- */
	LayoutGui layoutGui = new LayoutGui();
	
	public HousingGui() {
		/* --- Set up the dimensions of the frame --- */
        int WINDOWX = 550;
        int WINDOWY = 600;
        setBounds(50, 50, WINDOWX, WINDOWY);
        
		AnimationPanel housingAnimationPanel = new AnimationPanel();
		this.add(housingAnimationPanel);
		
		/* -- Set up people --- */
		people.add(residentPerson);
		people.add(payRecipientPerson);
		
		startAndActivate(residentPerson, residentRole);
		startAndActivate(payRecipientPerson, payRecipientRole);
		
		residentRole.setGui(residentGui);
		residentGui.setLayoutGui(layoutGui);
		
		/* --- Add to Animation Panel --- */
		housingAnimationPanel.addGui(residentGui);
	}
	
	private void startAndActivate(PersonAgent agent, Role role){
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