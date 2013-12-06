package housing;

import housing.backend.ResidentialBuilding;
import housing.gui.HousingInfoPanel;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import CommonSimpleClasses.Constants;

@SuppressWarnings("serial")
public class TestFile extends JFrame {
	
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
	
	public TestFile() {
		this.setLayout(new GridLayout(1, 2));
		
		this.add(building.getAnimationPanel());
		this.add(building.getInfoPanel());
		
		this.setPreferredSize(new Dimension(Constants.ANIMATION_PANEL_WIDTH * 2,
				Constants.ANIMATION_PANEL_HEIGHT));
		this.pack();
	}
	
	// TODO this is simply a test main() method
		public static void main(String[] args) {
			TestFile main = new TestFile();
			main.setTitle("Housing Complex");
			main.setVisible(true);
			main.setResizable(false);
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
}
