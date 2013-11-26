package bank.gui;

import gui.Building;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants;

public class InfoPanel extends JPanel{

	public InfoPanel(BankBuilding b) {
		Dimension d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		

		JLabel label = new JLabel("<html><div>&nbsp;</div><div> "
				+ "Name: "+ b.getName() +"</div><div>&nbsp;</div>");
		
		
		add(label);
	}

}
