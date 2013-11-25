package housing;

import housing.gui.HousingComplex;

import javax.swing.JFrame;

public class MainFile extends JFrame {

	public MainFile() {
		HousingComplex complex = new HousingComplex();
		add(complex);
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