package housing.gui;

import housing.backend.ResidentialBuilding;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class TestFile extends JFrame {
	
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
	HousingInfoPanel panel = new HousingInfoPanel(building.getPopulation());
	
	public TestFile() {
		this.setLayout(new GridLayout(2, 1));
		
		this.add(building.getComplex());
		this.add(panel);
		
		this.setBounds(50, 50, 600, 600);
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
