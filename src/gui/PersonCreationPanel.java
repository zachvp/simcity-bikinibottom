package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel for creating PersonAgents from UI
 * Treated as a "Hospital" Building
 * @author Victoria Dea
 *
 */
public class PersonCreationPanel extends JPanel implements ActionListener{

	public PersonCreationPanel(Dimension d){
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		//setLayout(new BorderLayout());
		
		JLabel welcomeText = new JLabel("Welcome to the Hospital!");
		Dimension textDim = new Dimension(d.width, (int)(d.height*0.2));
		welcomeText.setPreferredSize(textDim);
		welcomeText.setMaximumSize(textDim);
		welcomeText.setMinimumSize(textDim);
		Font font = new Font("Serif", Font.BOLD, 20);
		
		welcomeText.setFont(font);
		
		JPanel inputPanel = new JPanel();
		Dimension inputDim = new Dimension((int)(d.width*0.5), (int)(d.height*0.3));
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setLayout(new GridLayout(5,2,5,5));
		
		JTextField nameText = new JTextField();
		JComboBox<String> occupations = new JComboBox<String>();
		JComboBox<String> residences = new JComboBox<String>();
		JComboBox<String> wealth = new JComboBox<String>(new String[] {"Rich", "Middle", "Poor"});
		JComboBox<String> car = new JComboBox<String>(new String[] {"Yes", "No"});
		
		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameText);
		inputPanel.add(new JLabel("Select an Occupation: "));
		inputPanel.add(occupations);
		inputPanel.add(new JLabel("Select a Residence: "));
		inputPanel.add(residences);
		inputPanel.add(new JLabel("Select a Status: "));
		inputPanel.add(wealth);
		inputPanel.add(new JLabel("Has a Car: "));
		inputPanel.add(car);
		
		add(welcomeText);
		add(inputPanel);//, BorderLayout.CENTER);
		
	}

	
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
