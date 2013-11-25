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
		/*
		try {
			this.addRole(new PersonAgent("Squidward"), "resident");
			this.addRole(new PersonAgent("Mr. Krabs"), "payrecipient");
			this.addRole(new PersonAgent("Fishbait"), "maintenanceworker");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	/*
	// role factory for the HousingBuilding
	public void addRole(PersonAgent agent, String type) throws Exception{
		// normalize the parameter
		type.toLowerCase();
		
		if(type.equals("resident")) addResident(agent);
		else if(type.equals("payrecipient")) addPayRecipient(agent);
		else if(type.equals("maintenanceworker")) addMaintenanceWorker(agent);
		else throw new Exception("Improper housing role type passed.");
	}
	
	public void addResident(PersonAgent agent){
		ResidentRole resident = new ResidentRole(agent);
		agent.addRole(resident);
		population.put(agent, resident);
		this.startAndAddRole(agent, resident);
		
		// pass role to the housing complex to assign the resident to
		// a dwelling and create a gui for it
		complex.addResident(resident);
	}
	
	public void addPayRecipient(PersonAgent agent){
		PayRecipientRole payRecipient = new PayRecipientRole(agent);
		agent.addRole(payRecipient);
		population.put(agent, payRecipient);
//		this.startAndAddRole(agent, payRecipient);
		
		complex.addPayRecipient(payRecipient);
	}
	
	public void addMaintenanceWorker(PersonAgent agent){
		MaintenanceWorkerRole worker = new MaintenanceWorkerRole(agent);
		agent.addRole(worker);
		population.put(agent, worker);
//		this.startAndAddRole(agent, worker);
		
		complex.addWorker(worker);
	}
	*/
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
