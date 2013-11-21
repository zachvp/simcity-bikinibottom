package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.PersonAgent;

/**
 * A Panel that displays information about the person or building
 * Also contains some person/building controls
 * @author Victoria Dea
 *
 */
public class InfoPanel extends JPanel implements ActionListener{
	
	private CitizenRecords citizenRecords;
	private Dimension d;
	private JLabel info;
	private JPanel textPanel; //info display
	private JPanel controlPanel; //gui controls

	public InfoPanel(int w, int h){
		d = new Dimension(w-20, h-25);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new BorderLayout());
		
		textPanel = new JPanel();
		Dimension textDim = new Dimension(w/5, h-25);
		textPanel.setPreferredSize(textDim);
		textPanel.setMaximumSize(textDim);
		textPanel.setMinimumSize(textDim);
		
		info = new JLabel();
		//TODO test text
		info.setText("<html><div>&nbsp;</div><div> "
				+ "Name: "+ "Spongebob Squarepants" +"</div><div>&nbsp;</div>"
				+ "<div> Job: "+"Chef" +"</div><div>&nbsp;</div>"
				+ "<div> Residence: "+ "Pineapple" + "</div><div>&nbsp;</div>"
				+ "<div> Money: $"+ "500" +"</div><div>&nbsp;</div>"
				+ "<div> Hunger Level: "+"2" +"</div></html>"
		);
		textPanel.add(info);
		add(textPanel, BorderLayout.WEST);
	}

	/**
	 * Displays the Person information
	 * @param p Person name
	 */
	public void updatePersonInfoPanel(String name){
		System.out.println(name);
		PersonAgent person = citizenRecords.findPerson(name);
		
		
		//TODO Fix nullPointerException w/ person
		info.setText("<html><div>&nbsp;</div><div> "
						+ "Name: "+ person.getName() +"</div><div>&nbsp;</div>"
						//+ "<div> Job: "+ person.getJob() +"</div><div>&nbsp;</div>"
						//+ "<div> Residence: "+ person.getResidence + "</div><div>&nbsp;</div>"
						//+ "<div> Money: $"+ person.getMoney() +"</div><div>&nbsp;</div>"
						//+ "<div> Hunger Level: "+ person.getHungerLevel +"</div></html>"
				);



		validate();
	}
	
	/**
	 * Displays the Building information
	 * @param b Building name
	 */
	public void updateBuildingInfoPanel(String name){
		//Building building = b;
		System.out.println(name+ " update info panel");
		
	}
	
	/**
	 * Sets reference to citizen records
	 * @param records CitizenRecords
	 */
	public void setCitizenRecords(CitizenRecords records){
		citizenRecords = records;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
