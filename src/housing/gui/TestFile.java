package housing.gui;

import housing.roles.ResidentialBuilding;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class TestFile extends JFrame {
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
//	HousingInfoPanel panel = new HousingInfoPanel(building);
	
	public TestFile() {
//		this.setLayout(new GridLayout(2, 1));
		this.add(building.getComplex());
//		this.add(panel);
		this.setBounds(50, 50, 600, 490);
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
