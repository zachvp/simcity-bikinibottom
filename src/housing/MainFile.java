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
		this.addPayRecipient(new PersonAgent("Mr. Krabs"));
		this.addMaintenanceWorker(new PersonAgent("Fishbait"));
	}
	
	public void addResident(PersonAgent agent){
		ResidentRole resident = new ResidentRole(agent);
		agent.addRole(resident);
		population.put(agent, resident);
		this.startAndAddRole(agent, resident);
		
		complex.addResident(resident);
	}
	
	public void addPayRecipient(PersonAgent agent){
		PayRecipientRole payRecipient = new PayRecipientRole(agent);
		agent.addRole(payRecipient);
		population.put(agent, payRecipient);
		this.startAndAddRole(agent, payRecipient);
		
		complex.addPayRecipient(payRecipient);
	}
	
	public void addMaintenanceWorker(PersonAgent agent){
		MaintenanceWorkerRole worker = new MaintenanceWorkerRole(agent);
		agent.addRole(worker);
		population.put(agent, worker);
		this.startAndAddRole(agent, worker);
		
		complex.addWorker(worker);
	}
	
	private void startAndAddRole(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
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
