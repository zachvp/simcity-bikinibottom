package housing;

import javax.swing.JFrame;

public class MainFile extends JFrame {
	ResidentialBuilding building = new ResidentialBuilding(0, 0, 0, 0);
//	HousingComplex complex = new HousingComplex(building);
		

	public MainFile() {
		this.add(building.getComplex());
		this.setBounds(50, 50, 600, 490);
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
