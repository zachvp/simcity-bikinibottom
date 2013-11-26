package housing;

import java.awt.GridLayout;

import housing.gui.HousingInfoPanel;

import javax.swing.JFrame;

public class MainFile extends JFrame {
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
	HousingInfoPanel panel = new HousingInfoPanel(building);
	
	public MainFile() {
		this.setLayout(new GridLayout(2, 1));
		this.add(building.getComplex());
		this.add(panel);
		this.setBounds(50, 50, 600, 600);
	}
	
	// TODO this is simply a test main() method
		public static void main(String[] args) {
			MainFile main = new MainFile();
			main.setTitle("Housing Complex");
			main.setVisible(true);
			main.setResizable(false);
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
}
