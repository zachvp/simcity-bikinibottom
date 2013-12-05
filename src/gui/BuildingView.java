package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * Panel that displays zoomed in view of a building
 * @author Victoria Dea
 *
 */
public class BuildingView extends JPanel implements ActionListener{

	private Dimension d;
	private BufferedImage image;

	public BuildingView(int w, int h){
		d = new Dimension(w, h);//(w-15, h-70);
		setSize(d);
		setLayout(new CardLayout());
		//setBackground(Color.white);
		setOpaque(false);
		try {
			image = ImageIO.read(getClass().getResource("test//welcomeCard.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		//TODO change to welcome card?
		JPanel welcomeCard = new JPanel();
		welcomeCard.setPreferredSize(d);
		welcomeCard.setMaximumSize(d);
		welcomeCard.setMinimumSize(d);
		welcomeCard.setLayout(new BorderLayout());
		JLabel imageLabel = new JLabel(new ImageIcon(image));
		welcomeCard.add(imageLabel, BorderLayout.CENTER);
		add(welcomeCard, "blank");
	}
	
	
	/**
	 * Adds a new card (building view/JPanel) to the stack.
	 * @param card The JPanel to be added to the stack (ie. a new internal view of a building)
	 * @param name The name to reference the JPanel with (ie. name of the building)
	 */
	public void addCard(JPanel card, String name){
		add(card, name);
	}

	/**
	 * Shows the card
	 * @param name The name to reference the JPanel with (ie. name of the building) 
	 */
	public void showCard(String name) {
		//System.out.println("showing card " + name);
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

	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
