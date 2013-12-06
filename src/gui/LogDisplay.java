package gui;

import gui.trace.AlertTag;
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
		
		Dimension logDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.78), Constants.INFO_PANEL_HEIGHT);
		logPanel = new JPanel();
		logPanel.setPreferredSize(logDim);
		logPanel.setMaximumSize(logDim);
		logPanel.setMinimumSize(logDim);
		tracePanel.setPreferredSize(logDim);
		logPanel.add(tracePanel);
		
		Dimension buttonDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.22), Constants.INFO_PANEL_HEIGHT);
		buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(buttonDim);
		buttonsPanel.setMaximumSize(buttonDim);
		buttonsPanel.setMinimumSize(buttonDim);
		buttonsPanel.setLayout(new GridLayout(6,1,1,1));
		
		passenger = new JButton("Passenger: OFF");
		transportation = new JButton("Transportation: OFF");
		restaurant = new JButton("Restaurant: OFF");
		bank = new JButton("Bank: OFF");
		market = new JButton("Market: OFF");
		housing = new JButton("Housing: OFF");
		
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
		if (e.getSource() == passenger){
			if (passenger.getText().equals("Passenger: OFF")){
				passenger.setText("Passenger: ON");
				
			}
			else{
				passenger.setText("Passenger: OFF");

			}
		}
		if (e.getSource() == transportation){
			if (transportation.getText().equals("Transportation: OFF")){
				transportation.setText("Transportation: ON");

			}
			else{
				transportation.setText("Transportation: OFF");

			}
		}
		if (e.getSource() == restaurant){
			if (restaurant.getText().equals("Restaurant: OFF")){
				restaurant.setText("Restaurant: ON");

			}
			else{
				restaurant.setText("Restaurant: OFF");

			}
		}
		if (e.getSource() == bank){
			if (bank.getText().equals("Bank: OFF")){
				bank.setText("Bank: ON");
				tracePanel.showAlertsWithTag(AlertTag.BANK);
			}
			else{
				bank.setText("Bank: OFF");
				tracePanel.hideAlertsWithTag(AlertTag.BANK);
			}
		}
		if (e.getSource() == market){
			if (market.getText().equals("Market: OFF")){
				market.setText("Market: ON");

			}
			else{
				market.setText("Market: OFF");

			}
		}
		if (e.getSource() == housing){
			if (housing.getText().equals("Housing: OFF")){
				housing.setText("Housing: ON");

			}
			else{
				housing.setText("Housing: OFF");

			}
		}
	}
}
