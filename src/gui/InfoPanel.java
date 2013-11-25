package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import market.gui.MarketBuilding;
import agent.PersonAgent;

/**
 * A Panel that displays information about the person or building
 * Also contains some person/building controls
 * @author Victoria Dea
 *
 */
public class InfoPanel extends JPanel implements ActionListener{
	
	private Dimension d;
	private JLabel info;

	public InfoPanel(int w, int h){
		d = new Dimension(w-20, h-25); //700 X 185
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new CardLayout());
		
		JPanel personText = new JPanel();
		personText.setPreferredSize(d);
		personText.setMaximumSize(d);
		personText.setMinimumSize(d);
		addBuildingInfoPanel(personText, "person");
			
		info = new JLabel("");
		//Test text
		/*info.setText("<html><div>&nbsp;</div><div> "
				+ "Name: "+ "Spongebob Squarepants" +"</div><div>&nbsp;</div>"
				+ "<div> Job: "+"Chef" +"</div><div>&nbsp;</div>"
				+ "<div> Residence: "+ "Pineapple" + "</div><div>&nbsp;</div>"
				+ "<div> Money: $"+ "500" +"</div><div>&nbsp;</div>"
				+ "<div> Hunger Level: "+"2" +"</div></html>"
		);*/
		personText.add(info);
	}

	/**
	 * Displays the Person information
	 * @param p Person name
	 */
	public void updatePersonInfoPanel(PersonAgent person){
		CardLayout cl = (CardLayout)(this.getLayout());
		cl.show(this, "person");
		//System.out.println("update info with "+person.getName());
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
	public void updateBuildingInfoPanel(Building b){
		//Building building = b;
		System.out.println(b.getName()+ " update info panel");
		if (b instanceof MarketBuilding){
			((MarketBuilding) b).UpdateInfoPanel();
		}
		
		CardLayout cl = (CardLayout)(this.getLayout());
		if(b.getInfoPanel() == null){
			cl.show(this, "blank");
		}
		else
		{
			cl.show(this, b.getName());
		}
		
		validate();

	}
	
	/**
	 * Adds the Building's control panel to the cardlayout
	 * @param panel Building's info panel
	 * @param name Building's name
	 */
	public void addBuildingInfoPanel(JPanel panel, String name){
		add(panel, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}

}
