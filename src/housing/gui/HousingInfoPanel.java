package housing.gui;


import housing.backend.PayRecipientRole;
import housing.backend.ResidentRole;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.sound.Sound;
import agent.Role;
import agent.interfaces.Person;

@SuppressWarnings("serial")
public class HousingInfoPanel extends JPanel implements ActionListener {
	
	private Map<Role, Person> people;
	
	private JButton chargeRent = new JButton("Charge Rent");
	
	// allows for user to choose a specific unit
	private JComboBox<Integer> chargeUnitNumberOptions;
	private JComboBox<Integer> breakUnitNumberOptions;
	
	// fills the combo boxes
	private Vector<Integer> unitNumbers = new Vector<Integer>(); 
	
	private JButton breakHouse = new JButton("Break House");
	
	private Sound myLeg = Sound.getInstance();
	
	public HousingInfoPanel(CityLocation building) {
		
		this.setLayout(new FlowLayout());
		
		// populate the vector and add numbers to combo box
		for(int i = 0; i < Constants.HOUSING_UNIT_COUNT; i++) { this.unitNumbers.add(i); }
		this.chargeUnitNumberOptions = new JComboBox<Integer>(unitNumbers);
		this.breakUnitNumberOptions = new JComboBox<Integer>(unitNumbers);
		
		// instructions for GUI use
		add(new JLabel("Unit numbers read left-to-right, top-to-bottom."));
		
		// for the scenario GUI
		JPanel panel = new JPanel(new GridLayout(0, 3, 5, 5));
		
		// landlord input label
		panel.add(new JLabel("Ask every resident for rent."));
		
		// landlord input button
		chargeRent.addActionListener(this);
		panel.add(chargeRent);
		panel.add(chargeUnitNumberOptions);

		// maintenance input label
		panel.add(new JLabel("Summon a maintenance worker."));
		
		// worker button
		breakHouse.addActionListener(this);
		panel.add(breakHouse);
		panel.add(breakUnitNumberOptions);
		
		add(panel);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == chargeRent) {
			int chargeUnit = chargeUnitNumberOptions.getSelectedIndex();
			
			for(Map.Entry<Role, Person> entry : people.entrySet()) {
				if(entry.getKey() instanceof PayRecipientRole) {
					PayRecipientRole role = (PayRecipientRole) entry.getValue();
					System.out.println("Charging " + chargeUnit);
					role.msgChargeRent(chargeUnit);
				}
			}
		}
//		else if(evt.getSource() == breakHouse) {
//			myLeg.playSound("hitsound.wav");
//			for(Map.Entry<Person, Role> entry : people.entrySet()) {
//				if(entry.getValue() instanceof ResidentRole) {
//					ResidentRole role = (ResidentRole) entry.getValue();
//					role.getDwelling().degradeCondition();
//				}
//			}
//
//		}
	}
	
	public void updatePanel(){
		
	}
	
	public void name(String name) {
		add(new JLabel(name));
	}

	public void setPopulation(Map<Role, Person> map) {
		this.people = map;
	}
}
