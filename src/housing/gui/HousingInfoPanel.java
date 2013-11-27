package housing.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import housing.ResidentialBuilding;

import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.interfaces.Person;

public class HousingInfoPanel extends JPanel {
	Map<Person, JLabel> labels = new HashMap<Person, JLabel>();
	ResidentialBuilding building;
	Set<Person> people;

	public HousingInfoPanel(ResidentialBuilding building) {
		building.setName("Residence");
		this.building = building;
		people = building.getPopulation();
		
		this.setLayout(new GridLayout(2, 1));
		
		add(new JLabel(building.getName()));
		
		JPanel panel = new JPanel(new GridLayout(3,0));
		
		for(int i = 0; i < 9; i++){
			panel.add(new JLabel("Testicle"));
		}
		add(panel);
}
	
	public void updatePanel(){
		people = building.getPopulation();
		
		int i = 0;
		for(Person p : people){
			labels.get(i).setText(p.getName());
			i++;
		}
	}
}
