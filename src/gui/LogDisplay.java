package gui;

import gui.trace.TracePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants;

public class LogDisplay extends JPanel implements ActionListener{

	Dimension d;
	JPanel logPanel, buttonsPanel;
	JButton passenger, transportation, restaurant, bank, market, housing;
	TracePanel tracePanel = new TracePanel();
	
	public LogDisplay() {
		d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setLayout(new BorderLayout());
		
		Dimension logDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.80), Constants.INFO_PANEL_HEIGHT);
		logPanel = new JPanel();
		logPanel.setPreferredSize(logDim);
		logPanel.setMaximumSize(logDim);
		logPanel.setMinimumSize(logDim);
		tracePanel.setPreferredSize(logDim);
		logPanel.add(tracePanel);
		
		Dimension buttonDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.20), Constants.INFO_PANEL_HEIGHT);
		buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(buttonDim);
		buttonsPanel.setMaximumSize(buttonDim);
		buttonsPanel.setMinimumSize(buttonDim);
		buttonsPanel.setLayout(new GridLayout(6,1,1,1));
		
		passenger = new JButton("Passenger");
		transportation = new JButton("Transportation");
		restaurant = new JButton("Restaurant");
		bank = new JButton("Bank");
		market = new JButton("Market");
		housing = new JButton("Housing");
		
		passenger.addActionListener(this);
		transportation.addActionListener(this);
		restaurant.addActionListener(this);
		bank.addActionListener(this);
		market.addActionListener(this);
		housing.addActionListener(this);
		
		buttonsPanel.add(passenger);
		buttonsPanel.add(transportation);
		buttonsPanel.add(restaurant);
		buttonsPanel.add(bank);
		buttonsPanel.add(market);
		buttonsPanel.add(housing);
		
		add(logPanel, BorderLayout.WEST);
		add(buttonsPanel, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
