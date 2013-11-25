package bank.gui;

import javax.swing.JFrame;

public class MainFile extends JFrame {
	
	BankGui bank;
	public MainFile() {
		bank = new BankGui();
		add(bank);
		this.setBounds(50, 50, 600, 490);
		
//		this.add(bank);
//		this.setTitle("SpongeBank");
//		this.setVisible(true);
//		this.setResizable(false);
			}


	public static void main(String[] args) {
		MainFile main = new MainFile();
		main.setTitle("SpongeBank");
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setResizable(false);
	}
}