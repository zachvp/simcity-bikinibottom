package bank.gui;

import gui.Building;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants;

public class InfoPanel extends JPanel{

	GridLayout layout;
	public InfoPanel(BankBuilding b) {
		Dimension d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		
		layout = new GridLayout(0,1);
		this.setLayout(layout);
		
		JLabel label = new JLabel("<html><div>&nbsp;</div><div> "
				+ "Name: "+ b.getName() +"</div><div>&nbsp;</div>");
		
//		JLabel amLabel = new JLabel("<html><div>&nbsp;</div><div> " + "Security Guard: " + b.getNumberOfTellers());
		JLabel openHours = new JLabel("<html><div>&nbsp;</div><div> "+ "Hours: "+ b.openHour + " - "+ b.closeHour +"</div><div>&nbsp;</div>");
		JLabel amStatus = new JLabel("Account Manager: " + b.accountManagerOnDuty());
		
		add(label);
		add(openHours);
		add(amStatus);
		//add (amLabel);
	}

}
