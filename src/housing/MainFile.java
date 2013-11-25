package housing;

import java.util.HashMap;
import java.util.Map;

import housing.gui.HousingComplex;

import javax.swing.JFrame;

import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

public class MainFile extends JFrame {
	HousingComplex complex = new HousingComplex();
	Map<Person, Role> population;

	public MainFile() {
		this.add(complex);
//		ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
//		this.add(building);
		this.setBounds(50, 50,600, 490);
		
		// TODO test methods
		this.population = new HashMap<Person, Role>();
		this.addResident(new PersonAgent("Squidward"));
	}
	
	public void addResident(PersonAgent agent){
		System.out.println("Added resident");
		ResidentRole resident = new ResidentRole(agent);
		agent.addRole(resident);
		population.put(agent, resident);
		this.startAndActivate(agent, resident);
		
		complex.addResident(resident);
	}
	
	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
//		role.activate();
	}

	// TODO this is simply a test main() method
		public static void main(String[] args) {
			MainFile main = new MainFile();
			main.setTitle("Housing Complex");
			main.setVisible(true);
			main.setResizable(false);
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
}
