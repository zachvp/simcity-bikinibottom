package housing.gui;

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
	
	PersonAgent residentPerson = new PersonAgent("Resident");
	ResidentRole residentRole = new ResidentRole(residentPerson);
	ResidentGui residentGui = new ResidentGui(residentRole);
	
	public HousingGui() {
		// TODO Auto-generated constructor stub
//		this.setSize(main.getBuildingViewPanel().getDim());
		AnimationPanel housingAnimationPanel = new AnimationPanel();
		this.add(housingAnimationPanel);
		
		/* -- Add to Lists --- */
		people.add(residentPerson);
		startAndActivate(residentPerson, residentRole);
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