package bank.gui;

import gui.Building;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants;

public class InfoPanel extends JPanel implements ActionListener{

	GridLayout layout;
	JButton updateButton;
	JLabel bankMoney;
	BankBuilding bank;
	public InfoPanel(BankBuilding b) {
		bank = b;
		Dimension d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		
		layout = new GridLayout(0,1);
		this.setLayout(layout);
		updateButton = new JButton("UPDATE");
//		updateButton.addActionListener(this);
		JLabel label = new JLabel("<html><div>&nbsp;</div><div> "
				+ "Name: "+ b.getName() +"</div><div>&nbsp;</div>");
		bankMoney = new JLabel("Money in Vault: " + 100000);
//		JLabel amLabel = new JLabel("<html><div>&nbsp;</div><div> " + "Security Guard: " + b.getNumberOfTellers());
		JLabel openHours = new JLabel("<html><div>&nbsp;</div><div> "+ "Hours: "+ b.openHour + " - "+ b.closeHour +"</div><div>&nbsp;</div>");
		JLabel amStatus = new JLabel("Account Manager: " + b.accountManagerOnDuty());
		
		add(label);
		add(bankMoney);
		//add(updateButton);
		add(openHours);
//		add(amStatus);
		//add (amLabel);
	}
	
	/**
	 * update info panel, called by accountManager
	 */
	public void updateInfoPanel() {
		bankMoney.setText("Money in Vault: " + bank.getMoneyInBank());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateButton) {
			bankMoney.setText("" + bank.getMoneyInBank());
		}
	}

}
