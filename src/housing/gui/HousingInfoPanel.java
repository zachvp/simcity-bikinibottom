package housing.gui;


import housing.backend.PayRecipientRole;
import housing.backend.ResidentRole;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	
	public void playSound() {
	    try {
	    	// ImageIO.read(getClass().getResource("apartment_background.png"));
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream((getClass().getResource("hitsound.wav")));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
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
			playSound();
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
