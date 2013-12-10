package restaurant.vegaperk;

import java.awt.GridLayout;

import javax.swing.JFrame;

import restaurant.vegaperk.backend.RestaurantVegaPerkBuilding;
import CommonSimpleClasses.Constants;

/**
 * Test main method for vegaperk_restaurant
 * @author Zach VP
 *
 */

@SuppressWarnings("serial")
public class Test extends JFrame {
    private int WINDOWX = Constants.ANIMATION_PANEL_WIDTH * 2;
    private int WINDOWY = Constants.ANIMATION_PANEL_HEIGHT;

	private RestaurantVegaPerkBuilding building = new RestaurantVegaPerkBuilding(0, 0, 0, 0);
	
	public Test() {
		this.setLayout(new GridLayout(1, 2));
		
		this.add(building.getAnimationPanel());
		this.add(building.getInfoPanel());
		
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
