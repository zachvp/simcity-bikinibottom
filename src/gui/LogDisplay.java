package gui;

import gui.trace.AlertTag;
import gui.trace.TracePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import CommonSimpleClasses.Constants;

public class LogDisplay extends JPanel implements ActionListener{

	Dimension d;
	JPanel logPanel, buttonsPanel, labelPanel;
	JButton passenger, transportation, restaurant, bank, market, housing;
	JLabel passengerLabel, transportationLabel, restaurantLabel, bankLabel, marketLabel, housingLabel;
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
		
		Dimension labelDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.13), Constants.INFO_PANEL_HEIGHT);
		labelPanel = new JPanel();
		labelPanel.setPreferredSize(labelDim);
		labelPanel.setMaximumSize(labelDim);
		labelPanel.setMinimumSize(labelDim);
		labelPanel.setLayout(new GridLayout(6,1,1,1));
		
		passengerLabel = new JLabel("Passenger", JLabel.RIGHT);
		transportationLabel = new JLabel("Transportation", JLabel.RIGHT);
		restaurantLabel = new JLabel("Restaurant", JLabel.RIGHT);
		bankLabel = new JLabel("Bank", JLabel.RIGHT);
		marketLabel = new JLabel("Market", JLabel.RIGHT);
		housingLabel = new JLabel("Housing", JLabel.RIGHT);
		
		Border empty = new EmptyBorder(0, 0, 0, 7);
		passengerLabel.setBorder(empty);
		transportationLabel.setBorder(empty);
		restaurantLabel.setBorder(empty);
		bankLabel.setBorder(empty);
		marketLabel.setBorder(empty);
		housingLabel.setBorder(empty);
		
		labelPanel.add(passengerLabel);
		labelPanel.add(transportationLabel);
		labelPanel.add(restaurantLabel);
		labelPanel.add(bankLabel);
		labelPanel.add(marketLabel);
		labelPanel.add(housingLabel);
		
		Dimension buttonDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.09), Constants.INFO_PANEL_HEIGHT);
		buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(buttonDim);
		buttonsPanel.setMaximumSize(buttonDim);
		buttonsPanel.setMinimumSize(buttonDim);
		buttonsPanel.setLayout(new GridLayout(6,1,1,1));
		
		passenger = new JButton("OFF");
		transportation = new JButton("OFF");
		restaurant = new JButton("OFF");
		bank = new JButton("OFF");
		market = new JButton("OFF");
		housing = new JButton("OFF");
		
		passenger.setBackground(Color.white);
		transportation.setBackground(Color.white);
		restaurant.setBackground(Color.white);
		bank.setBackground(Color.white);
		market.setBackground(Color.white);
		housing.setBackground(Color.white);
		
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
		
		JPanel controls = new JPanel();
		Dimension controlDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.22), Constants.INFO_PANEL_HEIGHT);
		controls.setPreferredSize(controlDim);
		controls.setLayout(new BorderLayout());
		controls.add(labelPanel, BorderLayout.WEST);
		controls.add(buttonsPanel, BorderLayout.EAST);
		
		add(logPanel, BorderLayout.WEST);
		add(controls, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == passenger){
			if (passenger.getText().equals("OFF")){
				passenger.setText("ON");
				passenger.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.PASSENGER);
			}
			else{
				passenger.setText("OFF");
				passenger.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.PASSENGER);
			}
		}
		if (e.getSource() == transportation){
			if (transportation.getText().equals("OFF")){
				transportation.setText("ON");
				transportation.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.TRANSPORTATION);
			}
			else{
				transportation.setText("OFF");
				transportation.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.TRANSPORTATION);
			}
		}
		if (e.getSource() == restaurant){
			if (restaurant.getText().equals("OFF")){
				restaurant.setText("ON");
				restaurant.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
			}
			else{
				restaurant.setText("OFF");
				restaurant.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
			}
		}
		if (e.getSource() == bank){
			if (bank.getText().equals("OFF")){
				bank.setText("ON");
				bank.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.BANK);
			}
			else{
				bank.setText("OFF");
				bank.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.BANK);
			}
		}
		if (e.getSource() == market){
			if (market.getText().equals("OFF")){
				market.setText("ON");
				market.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.MARKET);
			}
			else{
				market.setText("OFF");
				market.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.MARKET);
			}
		}
		if (e.getSource() == housing){
			if (housing.getText().equals("OFF")){
				housing.setText("ON");
				housing.setBackground(Color.LIGHT_GRAY);
				tracePanel.showAlertsWithTag(AlertTag.HOUSING);
			}
			else{
				housing.setText("OFF");
				housing.setBackground(Color.white);
				tracePanel.hideAlertsWithTag(AlertTag.HOUSING);
			}
		}
	}
}
