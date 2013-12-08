package restaurant.vegaperk;

import javax.swing.JFrame;

import restaurant.vegaperk.gui.RestaurantGui;
import CommonSimpleClasses.Constants;

/**
 * Test main method for vegaperk_restaurant
 * @author Zach VP
 *
 */

@SuppressWarnings("serial")
public class Test extends JFrame {
    int WINDOWX = Constants.ANIMATION_PANEL_WIDTH * 2;
    int WINDOWY = Constants.ANIMATION_PANEL_HEIGHT;

	RestaurantGui gui;
	
	public Test() {
		gui = new RestaurantGui();
		this.add(gui);
		
		this.setBounds(50, 50, WINDOWX, WINDOWY);
	}

	/**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
    	Test main = new Test();
    	
        main.setTitle("Krusty Krab");
        main.setVisible(true);
        main.setResizable(false);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        main.setLocationRelativeTo(null);
    }
}
