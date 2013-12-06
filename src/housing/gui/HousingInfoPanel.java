package housing.gui;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.Role;
import agent.interfaces.Person;

@SuppressWarnings("serial")
public class HousingInfoPanel extends JPanel implements ActionListener {
	Map<Person, JLabel> labels = new HashMap<Person, JLabel>();
	Map<Person, Role> people;
	Set<Person> population;

	public HousingInfoPanel(Map<Person, Role> people) {
		this.people = people;
		
		this.setLayout(new GridLayout(2, 1));
		
		JPanel panel = new JPanel(new GridLayout(3,0));
		panel.add(new JLabel("Ask every resident for rent."));
		panel.add(new JLabel("Summon a maintenance worker."));
		
		panel.add(new JButton("Charge Rent"));
		panel.add(new JButton("Break House"));
		
//		for(int i = 0; i < 9; i++){
//			JLabel label = new JLabel();
//			panel.add(new JLabel("Resident"));
//			labels.put(null, label);
//		}
		add(new JLabel("Unit numbers read left-to-right, top-to-bottom."));
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
	
	public void name(String name) {
		add(new JLabel(name));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
