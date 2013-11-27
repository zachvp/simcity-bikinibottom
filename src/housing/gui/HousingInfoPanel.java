package housing.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import housing.ResidentialBuilding;

import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.Role;
import agent.interfaces.Person;

public class HousingInfoPanel extends JPanel {
	Map<Person, JLabel> labels = new HashMap<Person, JLabel>();
	Map<Person, Role> people;
	Set<Person> population;

	public HousingInfoPanel(Map<Person, Role> people) {
		this.people = people;
		
		this.setLayout(new GridLayout(2, 1));
		
		add(new JLabel("Residence"));
		
		JPanel panel = new JPanel(new GridLayout(3,0));
		
		for(int i = 0; i < 9; i++){
			panel.add(new JLabel("Testicle"));
		}
		add(panel);
}
	
	public void updatePanel(){
		int i = 0;
		population = people.keySet();
		
		for(Person p : population){
			labels.get(i).setText(p.getName());
			i++;
		}
	}
}
