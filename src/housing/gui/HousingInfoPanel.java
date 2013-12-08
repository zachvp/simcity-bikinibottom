package housing.gui;


import housing.backend.PayRecipientRole;
import housing.backend.ResidentRole;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.sound.Sound;
import agent.Role;
import agent.interfaces.Person;

@SuppressWarnings("serial")
public class HousingInfoPanel extends JPanel implements ActionListener {
	
	private Map<Person, Role> people;
	
	private JButton chargeRent = new JButton("Charge Rent");
	private JButton breakHouse = new JButton("Break House");
	
	private Sound myLeg = Sound.getInstance();
	
	public HousingInfoPanel(CityLocation building, Map<Person, Role> people) {
		
		this.people = people;
		
		this.setLayout(new GridLayout(2, 1));
		
		JPanel panel = new JPanel(new GridLayout(2,0));
		
		// landlord input
		panel.add(new JLabel("Ask every resident for rent."));
		chargeRent.addActionListener(this);
		panel.add(chargeRent);
		
		// maintenance input
		panel.add(new JLabel("Summon a maintenance worker."));
		breakHouse.addActionListener(this);
		panel.add(breakHouse);
		
		add(new JLabel("Unit numbers read left-to-right, top-to-bottom."));
		add(panel);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == chargeRent) {
			for(Map.Entry<Person, Role> entry : people.entrySet()) {
				if(entry.getValue() instanceof PayRecipientRole) {
					PayRecipientRole role = (PayRecipientRole) entry.getValue();
					role.msgChargeRent();
				}
			}
		}
		else if(evt.getSource() == breakHouse) {
			myLeg.playSound("hitsound.wav");
			for(Map.Entry<Person, Role> entry : people.entrySet()) {
				if(entry.getValue() instanceof ResidentRole) {
					ResidentRole role = (ResidentRole) entry.getValue();
					role.getDwelling().degradeCondition();
				}
			}

		}
	}
	
	public void updatePanel(){
		
	}
	
	public void name(String name) {
		add(new JLabel(name));
	}
}
