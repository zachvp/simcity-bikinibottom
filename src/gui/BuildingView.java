package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;



/**
 * Panel that displays zoomed in view of a building
 * @author Victoria Dea
 *
 */
public class BuildingView extends JPanel implements ActionListener{

	private Dimension d;
	private BuildingList buildingList;

	public BuildingView(int w, int h){
		d = new Dimension(w-15, h-70);
		setSize(d);
		setLayout(new CardLayout());
		setVisible(true);

		//TODO change to welcome card?
		JPanel blankCard = new JPanel();
		blankCard.setSize(d);
		blankCard.setPreferredSize(d);
		blankCard.setMaximumSize(d);
		blankCard.setMinimumSize(d);
		blankCard.setBackground(Color.LIGHT_GRAY);
		add(blankCard, "blank");
	}
	
	public void setBuildingList(BuildingList b){
		buildingList = b;
	}

	/**
	 * Adds a new card (building view/JPanel) to the stack.
	 * @param card The JPanel to be added to the stack (ie. a new internal view of a building)
	 * @param name The name to reference the JPanel with (ie. name of the building)
	 */
	public void addCard(JPanel card, String name){
		add(card, name);
		buildingList.addBuilding(name);
	}

	/**
	 * Shows the card
	 * @param name The name to reference the JPanel with (ie. name of the building) 
	 */
	public void showCard(String name) {
		CardLayout cl = (CardLayout)(this.getLayout());
		cl.show(this, name);
	}

	/**
	 * Returns the dimension of the panel. To be used for adding new cards.
	 * @return dimension of the panel
	 */
	public Dimension getDim(){
		return d;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
