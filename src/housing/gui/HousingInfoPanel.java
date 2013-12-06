package housing.gui;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.interfaces.Person;

@SuppressWarnings("serial")
public class HousingInfoPanel extends JPanel implements ActionListener {
	
	Map<Person, Role> people;
	
	JButton chargeRent = new JButton("Charge Rent");
	JButton breakHouse = new JButton("Break House");
	
	public HousingInfoPanel(CityLocation building) {
		
		this.setLayout(new GridLayout(2, 1));
		
		JPanel panel = new JPanel(new GridLayout(2,0));
		
		panel.add(new JLabel("Ask every resident for rent."));
		panel.add(chargeRent);
		
		panel.add(new JLabel("Summon a maintenance worker."));
		panel.add(breakHouse);
		
		add(new JLabel("Unit numbers read left-to-right, top-to-bottom."));
		add(panel);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == chargeRent) {
			System.out.println("Charging rent");
		}
	}
	
	public void updatePanel(){
		
	}
	
	public void name(String name) {
		add(new JLabel(name));
	}
}
